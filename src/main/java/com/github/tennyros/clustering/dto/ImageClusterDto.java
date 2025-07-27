package com.github.tennyros.clustering.dto;

import java.util.List;

public record ImageClusterDto(Long clusterId, List<ImageDto> images) {}