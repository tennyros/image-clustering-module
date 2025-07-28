package com.github.tennyros.clustering.service.clustering.impl;

import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import com.github.tennyros.clustering.entity.ImageClusterLink;
import com.github.tennyros.clustering.service.context.ClusteringContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static com.github.tennyros.clustering.util.TestEntityFactory.*;

@DisplayName("ClusterInitializer unit tests")
class ClusterInitializerTest {

    @Test
    @DisplayName("init should add all images to context")
    void init_shouldAddAllImagesToContext() {
        Image img = createImage();
        ImageCluster cluster = createCluster();
        ImageClusterLink link = createLink(img, cluster);

        cluster.setLinks(List.of(link));
        ClusteringContext ctx = mock(ClusteringContext.class);
        ClusterInitializer initializer = new ClusterInitializer();
        initializer.init(List.of(cluster), ctx);
        verify(ctx).addClusteredImage(img, cluster);
    }
}