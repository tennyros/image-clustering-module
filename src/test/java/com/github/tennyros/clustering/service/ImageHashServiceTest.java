package com.github.tennyros.clustering.service;

import com.github.tennyros.clustering.service.hash.ImageHashService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ImageHashService unit tests")
class ImageHashServiceTest {

    private final ImageHashService hashService = new ImageHashService();

    @Test
    @DisplayName("hammingDistance should return correct value")
    void hammingDistance_shouldReturnCorrectValue() {
        String hash1 = "ff00ff00ff00ff00";
        String hash2 = "ff00ff00ff00ff01";
        int dist = hashService.hammingDistance(hash1, hash2);
        assertThat(dist).isEqualTo(1);
    }

    @Test
    @DisplayName("calculateHash should return non-null value")
    void calculateHash_shouldReturnNonNull() {
        BufferedImage img = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
        String hash = hashService.calculateHash(img);
        assertThat(hash).isNotNull();
    }
} 