package com.github.tennyros.clustering;

import com.github.tennyros.clustering.config.DotenvLoader;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class DotenvTestConfig {

    static {
        DotenvLoader.load();
    }
}