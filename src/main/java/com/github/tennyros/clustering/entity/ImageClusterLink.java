package com.github.tennyros.clustering.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image_cluster_link")
public class ImageClusterLink {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "link_seq")
    @SequenceGenerator(name = "link_seq", sequenceName = "link_seq", allocationSize = 50)
    private Long id;

    @JoinColumn(name = "cluster_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ImageCluster cluster;

    @JoinColumn(name = "image_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Image image;
}