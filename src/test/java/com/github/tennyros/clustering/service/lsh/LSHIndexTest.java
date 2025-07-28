package com.github.tennyros.clustering.service.lsh;

import com.github.tennyros.clustering.entity.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static com.github.tennyros.clustering.util.TestEntityFactory.*;

@DisplayName("LSHIndex unit tests")
class LSHIndexTest {

    @Test
    @DisplayName("add and queryCandidates should work")
    void addAndQueryCandidates_shouldWork() {
        LSHIndex index = new LSHIndex(2, 4);
        Image img1 = createImage(1L, "url1", "abcd");
        Image img2 = createImage(2L, "url2", "abcf");
        index.add(img1);
        index.add(img2);
        Set<Image> candidates = index.queryCandidates(img1);
        assertThat(candidates).contains(img1, img2);
    }
} 