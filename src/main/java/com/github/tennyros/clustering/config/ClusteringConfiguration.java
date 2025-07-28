package com.github.tennyros.clustering.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ImageClusterProperties.class)
public class ClusteringConfiguration {
}