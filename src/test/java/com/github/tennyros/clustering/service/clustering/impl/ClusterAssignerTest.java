package com.github.tennyros.clustering.service.clustering.impl;

import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import com.github.tennyros.clustering.entity.ImageClusterLink;
import com.github.tennyros.clustering.repository.ImageClusterRepository;
import com.github.tennyros.clustering.service.context.ClusteringContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static com.github.tennyros.clustering.util.TestEntityFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClusterAssigner unit tests")
class ClusterAssignerTest {

    @Mock
    private ClusterMatcher matcher;

    @Mock
    private ImageClusterRepository clusterRepository;

    @Mock
    private ClusteringContext ctx;

    @InjectMocks
    private ClusterAssigner assigner;

    @Test
    @DisplayName("assign should return links")
    void assign_shouldReturnLinks() {
        Image img = createImage();
        when(ctx.isAlreadyClustered(any())).thenReturn(false);
        when(matcher.match(any(), eq(ctx))).thenReturn(Optional.empty());

        ImageCluster cluster = createCluster();
        when(clusterRepository.save(any(ImageCluster.class))).thenReturn(cluster);
        when(ctx.addClusteredImage(any(), any())).thenReturn(true);

        List<ImageClusterLink> links = assigner.assign(List.of(img), ctx);
        assertThat(links).hasSize(1);
        assertThat(links.get(0).getImage()).isEqualTo(img);
    }

    @Test
    @DisplayName("assign should not add link if addClusteredImage returns false")
    void assign_shouldNotAddLinkIfAddClusteredImageFails() {
        Image img = createImage();
        when(ctx.isAlreadyClustered(img.getId())).thenReturn(false);
        when(matcher.match(eq(img), any())).thenReturn(Optional.empty());
        when(clusterRepository.save(any())).thenReturn(createCluster());
        when(ctx.addClusteredImage(any(), any())).thenReturn(false);

        List<ImageClusterLink> links = assigner.assign(List.of(img), ctx);

        assertThat(links).isEmpty();
    }
}