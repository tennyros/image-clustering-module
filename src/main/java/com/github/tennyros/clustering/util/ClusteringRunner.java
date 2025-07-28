package com.github.tennyros.clustering.util;

import com.github.tennyros.clustering.dto.ImageClusterDto;
import com.github.tennyros.clustering.dto.ImageDto;
import com.github.tennyros.clustering.service.ImageClusterer;
import com.github.tennyros.clustering.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClusteringRunner implements CommandLineRunner {

    private final ImageService imageService;
    private final ImageClusterer imageClusterer;

    @Override
    public void run(String... args) throws Exception {
        imageService.addTestImages();
        imageClusterer.startClustering();

        Pageable pageable = PageRequest.of(0, 100);
        Page<ImageClusterDto> clustersPage = imageClusterer.listClusters(pageable);

        int clusterNumber = 1;
        for (ImageClusterDto cluster : clustersPage.getContent()) {
            log.info("Cluster #{}:", clusterNumber++);
            for (ImageDto image : cluster.images()) {
                log.info("  - Image ID: {}, URL: {}", image.id(), image.url());
            }
        }
    }
}
