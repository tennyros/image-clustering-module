package com.github.tennyros.clustering.service.context;

import com.github.tennyros.clustering.config.ImageClusterProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClusteringContextFactory {

    private final ImageClusterProperties properties;

    public ClusteringContext create() {
        int numBands = properties.getLsh().getNumBands();
        int bandSize = properties.getLsh().getBandSize();
        return new ClusteringContext(numBands, bandSize);
    }
}