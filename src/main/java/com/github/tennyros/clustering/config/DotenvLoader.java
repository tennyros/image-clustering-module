package com.github.tennyros.clustering.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class DotenvLoader {

    public static void load() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .filename(".env")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            dotenv.entries().forEach(entry ->
                    System.setProperty(entry.getKey(), entry.getValue())
            );

            log.info(".env загружен и передан в System properties.");
        } catch (Exception e) {
            log.warn(".env не найден, полагаемся на переменные окружения.");
        }
    }
}