package com.github.tennyros.clustering.service.clustering.impl;

import com.github.tennyros.clustering.dto.ImageClusterDto;
import com.github.tennyros.clustering.dto.ImageDto;
import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.entity.ImageCluster;
import com.github.tennyros.clustering.entity.ImageClusterLink;
import com.github.tennyros.clustering.repository.ImageClusterLinkRepository;
import com.github.tennyros.clustering.repository.ImageClusterRepository;
import com.github.tennyros.clustering.repository.ImageRepository;
import com.github.tennyros.clustering.service.context.ClusteringContext;
import com.github.tennyros.clustering.service.context.ClusteringContextFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static com.github.tennyros.clustering.util.TestEntityFactory.createCluster;
import static com.github.tennyros.clustering.util.TestEntityFactory.createImage;
import static com.github.tennyros.clustering.util.TestEntityFactory.createLink;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImageClustererImpl unit tests")
class ImageClustererImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageClusterRepository clusterRepository;

    @Mock
    private ImageClusterLinkRepository linkRepository;

    @Mock
    private ClusteringContextFactory contextFactory;

    @Mock
    private ClusterInitializer initializer;

    @Mock
    private ClusterAssigner assigner;

    @InjectMocks
    private ImageClustererImpl imageClusterer;

    @Test
    void startClustering_shouldCallDependencies() {
        ClusteringContext ctx = mock(ClusteringContext.class);
        when(contextFactory.create()).thenReturn(ctx);
        when(imageRepository.findAllWithNonNullPHash()).thenReturn(Collections.emptyList());
        when(clusterRepository.findAllWithImages()).thenReturn(Collections.emptyList());
        when(assigner.assign(any(), any())).thenReturn(Collections.emptyList());

        imageClusterer.startClustering();

        verify(contextFactory).create();
        verify(imageRepository).findAllWithNonNullPHash();
        verify(clusterRepository).findAllWithImages();
        verify(initializer).init(anyList(), eq(ctx));
        verify(assigner).assign(anyList(), eq(ctx));
        verify(linkRepository).saveAll(anyList());
    }

    @Test
    void listClusters_shouldReturnClusterDtos() {
        Image image = createImage(1L, "url", "hash");
        ImageCluster cluster = createCluster();
        cluster.setId(10L);
        ImageClusterLink link = createLink(100L, image, cluster);
        cluster.setLinks(List.of(link));
        Page<ImageCluster> page = new PageImpl<>(List.of(cluster));
        when(clusterRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ImageClusterDto> result = imageClusterer.listClusters(PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        ImageClusterDto dto = result.getContent().get(0);
        assertThat(dto.clusterId()).isEqualTo(10L);
        assertThat(dto.images()).hasSize(1);
        ImageDto imgDto = dto.images().get(0);
        assertThat(imgDto.id()).isEqualTo(1L);
        assertThat(imgDto.url()).isEqualTo("url");
    }
}