package com.github.tennyros.clustering.repository;

import com.github.tennyros.clustering.entity.ImageCluster;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageClusterRepository extends JpaRepository<ImageCluster, Long> {

    @NotNull
    @EntityGraph(attributePaths = {"links", "links.image"})
    Page<ImageCluster> findAll(@NotNull Pageable pageable);

    @Query("SELECT DISTINCT c FROM ImageCluster c LEFT JOIN FETCH c.links l LEFT JOIN FETCH l.image")
    List<ImageCluster> findAllWithImages();
}