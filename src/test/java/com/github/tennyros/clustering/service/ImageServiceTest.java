package com.github.tennyros.clustering.service;

import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.repository.ImageRepository;
import com.github.tennyros.clustering.service.hash.ImageHashService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageHashService imageHashService;

    @InjectMocks
    private ImageService imageService;

    private ImageService spyService;

    @BeforeEach
    void setUp() {
        spyService = Mockito.spy(imageService);
    }

    @Test
    void addImages_shouldSaveImages() throws Exception {
        BufferedImage dummyImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        doReturn(dummyImage).when(spyService).downloadImage(anyString());

        when(imageHashService.calculateHash(dummyImage)).thenReturn("mockedHash");
        when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> invocation.getArgument(0));

        spyService.addImages();

        verify(imageRepository, times(6)).save(any(Image.class));
        verify(imageHashService, times(6)).calculateHash(dummyImage);

        ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);
        verify(imageRepository, times(6)).save(captor.capture());
        for (Image savedImage : captor.getAllValues()) {
            assertEquals("mockedHash", savedImage.getPHash());
            assertNotNull(savedImage.getUrl());
        }
    }

    @Test
    void addImages_shouldSkipInvalidImages() throws Exception {
        doAnswer(invocation -> {
            String url = invocation.getArgument(0);
            if (url.contains("cat1.jpg")) return null;
            return new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        }).when(spyService).downloadImage(anyString());

        when(imageHashService.calculateHash(any())).thenReturn("mockedHash");
        when(imageRepository.save(any(Image.class))).thenAnswer(invocation -> invocation.getArgument(0));

        spyService.addImages();

        verify(imageRepository, times(5)).save(any(Image.class));
    }
}