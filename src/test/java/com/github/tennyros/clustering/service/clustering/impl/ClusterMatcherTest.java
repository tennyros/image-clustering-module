package com.github.tennyros.clustering.service.clustering.impl;

import com.github.tennyros.clustering.config.ImageClusterProperties;
import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import com.github.tennyros.clustering.service.context.ClusteringContext;
import com.github.tennyros.clustering.service.hash.ImageHashService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static com.github.tennyros.clustering.util.TestEntityFactory.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClusterMatcher unit tests")
class ClusterMatcherTest {

    @Mock
    private ImageHashService hashService;

    @Mock
    private ImageClusterProperties props;

    @Mock
    private ClusteringContext ctx;

    @InjectMocks
    private ClusterMatcher matcher;

    @BeforeEach
    void setUp() {
        when(props.getHammingThreshold()).thenReturn(5);
    }

    @Test
    @DisplayName("match should return cluster if distance is low")
    void match_shouldReturnClusterIfDistanceLow() {
        Image img = createImage(1L, "url", "a");
        Image candidate = createImage(2L, "url2", "b");
        ImageCluster cluster = createCluster();

        when(ctx.getCandidates(img)).thenReturn(Set.of(candidate));
        when(hashService.hammingDistance(any(), any())).thenReturn(3);
        when(ctx.getClusterForImage(candidate.getId())).thenReturn(Optional.of(cluster));

        Optional<ImageCluster> result = matcher.match(img, ctx);
        assertThat(result).contains(cluster);
    }

    @Test
    @DisplayName("match should return empty if distance is high")
    void match_shouldReturnEmptyIfDistanceHigh() {
        Image img = createImage(1L, "url", "a");
        Image candidate = createImage(2L, "url2", "b");

        when(ctx.getCandidates(img)).thenReturn(Set.of(candidate));
        when(hashService.hammingDistance(any(), any())).thenReturn(10);

        Optional<ImageCluster> result = matcher.match(img, ctx);
        assertThat(result).isEmpty();
    }
}