package com.github.tennyros.clustering.service.clustering.impl;

import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import com.github.tennyros.clustering.entity.ImageClusterLink;
import com.github.tennyros.clustering.service.context.ClusteringContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClusterInitializer {

    public void init(List<ImageCluster> clusters, ClusteringContext ctx) {
        for (ImageCluster cluster : clusters) {
            for (ImageClusterLink link : cluster.getLinks()) {
                Image img = link.getImage();
                Long id = img.getId();
                if (img.getPHash() != null && ctx.getClusteredIds().add(id)) {
                    ctx.getLshIndex().add(img);
                    ctx.getImageIdToCluster().put(id, cluster);
                }
            }
        }
    }
}