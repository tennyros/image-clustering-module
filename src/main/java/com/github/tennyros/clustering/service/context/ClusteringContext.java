package com.github.tennyros.clustering.service.context;

import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import com.github.tennyros.clustering.service.lsh.LSHIndex;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ClusteringContext {

    @Getter
    private final LSHIndex lshIndex;

    private final Set<Long> clusteredIds = new HashSet<>();
    private final Map<Long, ImageCluster> imageIdToCluster = new HashMap<>();

    public ClusteringContext(int numBands, int bandSize) {
        this.lshIndex = new LSHIndex(numBands, bandSize);
    }

    public boolean isAlreadyClustered(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return clusteredIds.contains(id);
    }

    public Optional<ImageCluster> getClusterForImage(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(imageIdToCluster.get(id));
    }

    public Set<Image> getCandidates(Image image) {
        Objects.requireNonNull(image, "image must not be null");
        return lshIndex.queryCandidates(image);
    }

    public boolean addClusteredImage(Image img, ImageCluster cluster) {
        Objects.requireNonNull(img, "img must not be null");
        Objects.requireNonNull(cluster, "cluster must not be null");

        if (img.getPHash() == null || !clusteredIds.add(img.getId())) {
            return false;
        }

        lshIndex.add(img);
        imageIdToCluster.put(img.getId(), cluster);
        return true;
    }
}