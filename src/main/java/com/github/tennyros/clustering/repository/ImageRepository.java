package com.github.tennyros.clustering.repository;

import com.github.tennyros.clustering.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.pHash IS NOT NULL AND i.pHash <> ''")
    List<Image> findAllWithNonNullPHash();
}
