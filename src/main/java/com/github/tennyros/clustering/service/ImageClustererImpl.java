package com.github.tennyros.clustering.service;

import com.github.tennyros.clustering.dto.ImageClusterDto;
import com.github.tennyros.clustering.dto.ImageDto;
import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import com.github.tennyros.clustering.entity.ImageClusterLink;
import com.github.tennyros.clustering.repository.ImageClusterLinkRepository;
import com.github.tennyros.clustering.repository.ImageClusterRepository;
import com.github.tennyros.clustering.repository.ImageRepository;
import com.github.tennyros.clustering.util.LSHIndex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageClustererImpl implements ImageClusterer {

    private static final int HAMMING_THRESHOLD = 10;

    private final ImageRepository imageRepository;
    private final ImageClusterRepository clusterRepository;
    private final ImageClusterLinkRepository clusterLinkRepository;
    private final ImageHashService hashService;

    @Transactional
    public void startClustering() {
        List<Image> imagesToCluster = imageRepository.findAllWithNonNullPHash();
        List<ImageCluster> allClusters = clusterRepository.findAllWithImages();

        LSHIndex lshIndex = new LSHIndex(8, 8);
        Set<Long> clusteredIds = new HashSet<>();
        Map<Long, ImageCluster> imageIdToCluster = new HashMap<>();

        initIndexWithExistingClusters(allClusters, lshIndex, clusteredIds, imageIdToCluster);
        List<ImageClusterLink> linksToAdd = clusterNewImages(imagesToCluster, lshIndex, clusteredIds, imageIdToCluster);
        clusterLinkRepository.saveAll(linksToAdd);
    }

    private void initIndexWithExistingClusters(List<ImageCluster> allClusters, LSHIndex lshIndex,
                                               Set<Long> clusteredIds, Map<Long, ImageCluster> imageIdToCluster) {
        for (ImageCluster cluster : allClusters) {
            for (ImageClusterLink link : cluster.getLinks()) {
                Image img = link.getImage();
                Long id = img.getId();
                if (img.getPHash() != null && clusteredIds.add(id)) {
                    lshIndex.add(img);
                    imageIdToCluster.put(id, cluster);
                }
            }
        }
    }

    private List<ImageClusterLink> clusterNewImages(List<Image> imagesToCluster, LSHIndex lshIndex,
                                                    Set<Long> clusteredIds, Map<Long, ImageCluster> imageIdToCluster) {
        List<ImageClusterLink> linksToAdd = new ArrayList<>();

        for (Image img : imagesToCluster) {
            Long id = img.getId();
            if (clusteredIds.contains(id)) continue;

            ImageCluster matchedCluster = findMatchingCluster(img, lshIndex, imageIdToCluster)
                    .orElseGet(() -> clusterRepository.save(new ImageCluster()));

            ImageClusterLink link = ImageClusterLink.builder()
                    .image(img)
                    .cluster(matchedCluster)
                    .build();

            linksToAdd.add(link);
            matchedCluster.getLinks().add(link);
            lshIndex.add(img);
            imageIdToCluster.put(id, matchedCluster);
            clusteredIds.add(id);
        }

        return linksToAdd;
    }

    private Optional<ImageCluster> findMatchingCluster(Image img, LSHIndex lshIndex,
                                                       Map<Long, ImageCluster> imageIdToCluster) {
        for (Image candidate : lshIndex.queryCandidates(img)) {
            if (candidate.getId().equals(img.getId())) continue;

            int dist = hashService.hammingDistance(img.getPHash(), candidate.getPHash());
            if (dist <= HAMMING_THRESHOLD) {
                return Optional.ofNullable(imageIdToCluster.get(candidate.getId()));
            }
        }
        return Optional.empty();
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
