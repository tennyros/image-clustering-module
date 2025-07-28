package com.github.tennyros.clustering.controller;

import com.github.tennyros.clustering.dto.ImageClusterDto;
import com.github.tennyros.clustering.service.clustering.ImageClusterer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clusters")
public class ImageClusterController {

    private final ImageClusterer imageClusterer;

    @GetMapping
    public Page<ImageClusterDto> listClusters(Pageable pageable) {
        return imageClusterer.listClusters(pageable);
    }
}