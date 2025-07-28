package com.github.tennyros.clustering.service.context;

import com.github.tennyros.clustering.service.lsh.LSHIndex;
import com.github.tennyros.clustering.entity.ImageCluster;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class ClusteringContext {

    private final LSHIndex lshIndex;
    private final Set<Long> clusteredIds = new HashSet<>();
    private final Map<Long, ImageCluster> imageIdToCluster = new HashMap<>();

    public ClusteringContext(int numBands, int bandSize) {
        this.lshIndex = new LSHIndex(numBands, bandSize);
    }
}