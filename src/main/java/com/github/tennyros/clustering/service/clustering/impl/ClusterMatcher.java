package com.github.tennyros.clustering.service.clustering.impl;

import com.github.tennyros.clustering.config.ImageClusterProperties;
import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import com.github.tennyros.clustering.service.context.ClusteringContext;
import com.github.tennyros.clustering.service.hash.ImageHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClusterMatcher {

    private final ImageHashService hashService;
    private final ImageClusterProperties props;

    public Optional<ImageCluster> match(Image img, ClusteringContext ctx) {
        for (Image candidate : ctx.getCandidates(img)) {
            if (candidate.getId().equals(img.getId())) {
                continue;
            }

            int dist = hashService.hammingDistance(img.getPHash(), candidate.getPHash());
            if (dist <= props.getHammingThreshold()) {
                return ctx.getClusterForImage(candidate.getId());
            }
        }
        return Optional.empty();
    }
}