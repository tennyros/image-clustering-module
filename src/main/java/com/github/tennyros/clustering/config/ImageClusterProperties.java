package com.github.tennyros.clustering.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "image-cluster")
public class ImageClusterProperties {

    private Lsh lsh = new Lsh();
    private int hammingThreshold;

    @Getter
    @Setter
    public static class Lsh {

        private int numBands;
        private int bandSize;
    }
}
