package com.github.tennyros.clustering.repository;

import com.github.tennyros.clustering.entity.ImageCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageClusterRepository extends JpaRepository<ImageCluster, Long> {

    @Query("SELECT c FROM ImageCluster c LEFT JOIN FETCH c.links l LEFT JOIN FETCH l.image")
    List<ImageCluster> findAllWithImages();
}