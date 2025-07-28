package com.github.tennyros.clustering.service.context;

import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tennyros.clustering.util.TestEntityFactory.createCluster;
import static com.github.tennyros.clustering.util.TestEntityFactory.createImage;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ClusteringContext unit tests")
class ClusteringContextTest {

    @Test
    @DisplayName("should not add image if pHash is null")
    void shouldNotAddImageIfPHashIsNull() {
        ClusteringContext context = new ClusteringContext(5, 8);

        Image image = new Image();
        image.setId(1L);
        image.setPHash(null);

        ImageCluster cluster = createCluster();

        boolean added = context.addClusteredImage(image, cluster);

        assertThat(added).isFalse();
    }

    @Test
    @DisplayName("should not add image if already clustered")
    void shouldNotAddImageIfAlreadyClustered() {
        ClusteringContext context = new ClusteringContext(5, 8);

        Image image = createImage();

        ImageCluster cluster = createCluster();

        boolean firstAdded = context.addClusteredImage(image, cluster);
        assertThat(firstAdded).isTrue();

        boolean secondAdded = context.addClusteredImage(image, cluster);
        assertThat(secondAdded).isFalse();
    }

    @Test
    @DisplayName("should add image if valid and not clustered")
    void shouldAddImageIfValidAndNotClustered() {
        ClusteringContext context = new ClusteringContext(5, 8);

        Image image = createImage();

        ImageCluster cluster = createCluster();

        boolean added = context.addClusteredImage(image, cluster);

        assertThat(added).isTrue();
        assertThat(context.isAlreadyClustered(image.getId())).isTrue();
        assertThat(context.getClusterForImage(image.getId())).contains(cluster);
    }
}
