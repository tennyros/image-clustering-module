package com.github.tennyros.clustering.service.clustering.impl;

import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import com.github.tennyros.clustering.entity.ImageClusterLink;
import com.github.tennyros.clustering.repository.ImageClusterRepository;
import com.github.tennyros.clustering.service.context.ClusteringContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ClusterAssigner {

    private final ClusterMatcher matcher;
    private final ImageClusterRepository clusterRepository;

    public List<ImageClusterLink> assign(List<Image> images, ClusteringContext ctx) {
        List<ImageClusterLink> linksToAdd = new ArrayList<>();

        for (Image img : images) {
            Long id = img.getId();
            if (ctx.getClusteredIds().contains(id)) {
                continue;
            }

            ImageCluster cluster = matcher.match(img, ctx)
                    .orElseGet(() -> clusterRepository.save(new ImageCluster()));

            ImageClusterLink link = ImageClusterLink.builder()
                    .image(img)
                    .cluster(cluster)
                    .build();

            cluster.getLinks().add(link);
            ctx.getLshIndex().add(img);
            ctx.getImageIdToCluster().put(id, cluster);
            ctx.getClusteredIds().add(id);
            linksToAdd.add(link);
        }

        return linksToAdd;
    }
}