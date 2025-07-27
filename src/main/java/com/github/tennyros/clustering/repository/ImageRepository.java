package com.github.tennyros.clustering.repository;

import com.github.tennyros.clustering.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
