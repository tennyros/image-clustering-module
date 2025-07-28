package com.github.tennyros.clustering.service;

import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.repository.ImageRepository;
import com.github.tennyros.clustering.service.hash.ImageHashService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageHashService imageHashService;

    @Transactional
    public void addImages() {
        List<String> imageUrls = List.of(
                "https://raw.githubusercontent.com/tennyros/image-storage/master/image-clusterer-assets/cat1.jpg",
                "https://raw.githubusercontent.com/tennyros/image-storage/master/image-clusterer-assets/cat2.jpg",
                "https://raw.githubusercontent.com/tennyros/image-storage/master/image-clusterer-assets/cat3.jpg",
                "https://raw.githubusercontent.com/tennyros/image-storage/master/image-clusterer-assets/cat4.png",
                "https://raw.githubusercontent.com/tennyros/image-storage/master/image-clusterer-assets/cat5.png",
                "https://raw.githubusercontent.com/tennyros/image-storage/master/image-clusterer-assets/cat6.png"
        );

        for (String imageUrl : imageUrls) {
            processAndSaveImage(imageUrl);
        }
    }

    private void processAndSaveImage(String imageUrl) {
        try {
            BufferedImage image = downloadImage(imageUrl);
            if (image == null) {
                log.warn("Не удалось прочитать изображение по ссылке: {}", imageUrl);
                return;
            }

            String hash = imageHashService.calculateHash(image);

            Image img = new Image();
            img.setPHash(hash);
            img.setUrl(imageUrl);

            imageRepository.save(img);

        } catch (IOException e) {
            log.error("Ошибка при загрузке изображения по URL {}: {}", imageUrl, e.getMessage(), e);
        }
    }

    protected BufferedImage downloadImage(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        try (InputStream is = connection.getInputStream()) {
            return ImageIO.read(is);
        }
    }
}