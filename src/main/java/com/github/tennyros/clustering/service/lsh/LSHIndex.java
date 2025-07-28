package com.github.tennyros.clustering.service.lsh;

import com.github.tennyros.clustering.entity.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LSHIndex {

    private final int numBands;
    private final int bandSize;
    private final Map<String, List<Image>> buckets = new HashMap<>();

    public LSHIndex(int numBands, int bandSize) {
        this.numBands = numBands;
        this.bandSize = bandSize;
    }

    public void add(Image img) {
        String hash = img.getPHash();
        if (hash == null) return;

        for (String bucketKey : extractBandKeys(hash)) {
            buckets.computeIfAbsent(bucketKey, k -> new ArrayList<>()).add(img);
        }
    }

    public Set<Image> queryCandidates(Image img) {
        Set<Image> candidates = new HashSet<>();
        String hash = img.getPHash();
        if (hash == null) return candidates;

        for (String bucketKey : extractBandKeys(hash)) {
            List<Image> bucketImages = buckets.get(bucketKey);
            if (bucketImages != null) {
                candidates.addAll(bucketImages);
            }
        }

        return candidates;
    }

    private List<String> extractBandKeys(String hexHash) {
        String binHash = hexToBinary(hexHash);
        List<String> keys = new ArrayList<>();

        for (int band = 0; band < numBands; band++) {
            int start = band * bandSize;
            int end = Math.min(start + bandSize, binHash.length());
            if (start >= binHash.length()) break;

            String bandHash = binHash.substring(start, end);
            String bucketKey = band + ":" + bandHash;
            keys.add(bucketKey);
        }

        return keys;
    }

    private String hexToBinary(String hex) {
        StringBuilder bin = new StringBuilder();
        for (char c : hex.toCharArray()) {
            int i = Integer.parseInt(String.valueOf(c), 16);
            bin.append(String.format("%4s", Integer.toBinaryString(i)).replace(' ', '0'));
        }
        return bin.toString();
    }
}
