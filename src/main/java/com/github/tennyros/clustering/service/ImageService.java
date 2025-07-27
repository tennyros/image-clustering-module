package com.github.tennyros.clustering.service;

import com.github.tennyros.clustering.entity.Image;
import com.github.tennyros.clustering.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageHashService imageHashService;

    @Transactional
    public void addTestImages() {
        List<String> fileNames = List.of("cat1.jpg", "cat2.jpg", "cat3.png", "cat4.png", "cat5.png");

        for (String fileName : fileNames) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("images/" + fileName)) {
                if (is == null) {
                    log.warn("Изображение не найдено: {}", fileName);
                    continue;
                }

                BufferedImage image = ImageIO.read(is);
                String hash = imageHashService.calculateHash(image);

                Image img = new Image();
                img.setPHash(hash);
                img.setUrl("images/" + fileName);

                imageRepository.save(img);
            } catch (IOException e) {
                log.error("Ошибка при обработке изображения {}: {}", fileName, e.getMessage(), e);
            }
        }
    }
}
