package com.github.tennyros.clustering.service;

import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.math.BigInteger;

@Service
public class ImageHashService {

    private final HashingAlgorithm hasher = new PerceptiveHash(64);

    public String calculateHash(BufferedImage image) {
        return hasher.hash(image).getHashValue().toString(16);
    }

    public int hammingDistance(String hash1, String hash2) {
        BigInteger b1 = new BigInteger(hash1, 16);
        BigInteger b2 = new BigInteger(hash2, 16);
        return b1.xor(b2).bitCount();
    }
}