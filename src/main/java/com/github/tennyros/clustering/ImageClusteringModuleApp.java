package com.github.tennyros.clustering;

import com.github.tennyros.clustering.util.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageClusteringModuleApp {

    public static void main(String[] args) {
        DotenvLoader.load();
        SpringApplication.run(ImageClusteringModuleApp.class, args);
    }

}
