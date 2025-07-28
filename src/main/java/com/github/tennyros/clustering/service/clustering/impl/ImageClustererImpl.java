package com.github.tennyros.clustering.service.clustering.impl;

import com.github.tennyros.clustering.dto.ImageClusterDto;
import com.github.tennyros.clustering.dto.ImageDto;
import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import com.github.tennyros.clustering.entity.ImageClusterLink;
import com.github.tennyros.clustering.repository.ImageClusterLinkRepository;
import com.github.tennyros.clustering.repository.ImageClusterRepository;
import com.github.tennyros.clustering.repository.ImageRepository;
import com.github.tennyros.clustering.service.clustering.ImageClusterer;
import com.github.tennyros.clustering.service.context.ClusteringContext;
import com.github.tennyros.clustering.service.context.ClusteringContextFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageClustererImpl implements ImageClusterer {

    private final ImageRepository imageRepository;
    private final ImageClusterRepository clusterRepository;
    private final ImageClusterLinkRepository linkRepository;
    private final ClusteringContextFactory contextFactory;

    private final ClusterInitializer initializer;
    private final ClusterAssigner assigner;

    @Override
    @Transactional
    public void startClustering() {
        ClusteringContext ctx = contextFactory.create();

        List<Image> imagesToCluster = imageRepository.findAllWithNonNullPHash();
        List<ImageCluster> allClusters = clusterRepository.findAllWithImages();

        initializer.init(allClusters, ctx);
        List<ImageClusterLink> links = assigner.assign(imagesToCluster, ctx);

        linkRepository.saveAll(links);
    }

    @Transactional(readOnly = true)
    public Page<ImageClusterDto> listClusters(Pageable pageable) {
        Page<ImageCluster> page = clusterRepository.findAll(pageable);
        return page.map(cluster -> new ImageClusterDto(
                cluster.getId(),
                cluster.getLinks().stream()
                        .map(link -> {
                            Image img = link.getImage();
                            return new ImageDto(img.getId(), img.getUrl());
                        })
                        .toList()
        ));
    }
}
