package com.github.tennyros.clustering.util;

import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import com.github.tennyros.clustering.entity.ImageClusterLink;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestEntityFactory {

    public static Image createImage(Long id, String url, String hash) {
        return new Image(id, url, hash);
    }

    public static Image createImage() {
        return createImage(1L, "url", "abc123");
    }

    public static ImageCluster createCluster() {
        return new ImageCluster();
    }

    public static ImageClusterLink createLink(Long id, Image img, ImageCluster cluster) {
        return ImageClusterLink.builder().id(id).image(img).cluster(cluster).build();
    }

    public static ImageClusterLink createLink(Image img, ImageCluster cluster) {
        return createLink(1L, img, cluster);
    }
} 