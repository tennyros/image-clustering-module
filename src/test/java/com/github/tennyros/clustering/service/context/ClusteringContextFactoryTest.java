package com.github.tennyros.clustering.service.context;

import com.github.tennyros.clustering.config.ImageClusterProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("ClusteringContextFactory unit tests")
class ClusteringContextFactoryTest {

    @Test
    @DisplayName("create should return context with correct params")
    void create_shouldReturnContextWithCorrectParams() {
        ImageClusterProperties.Lsh lsh = mock(ImageClusterProperties.Lsh.class);
        when(lsh.getNumBands()).thenReturn(5);
        when(lsh.getBandSize()).thenReturn(8);

        ImageClusterProperties props = mock(ImageClusterProperties.class);
        when(props.getLsh()).thenReturn(lsh);

        ClusteringContextFactory factory = new ClusteringContextFactory(props);
        ClusteringContext ctx = factory.create();
        assertThat(ctx).isNotNull();
    }
}