package com.github.tennyros.clustering.service.clustering;

import com.github.tennyros.clustering.dto.ImageClusterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ImageClusterer {

    void startClustering();

    Page<ImageClusterDto> listClusters(Pageable pageable);
}
