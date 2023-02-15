package com.example.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Отвечает за работу с файлами данных
 */
@Service
public class RoutesLoader {

    @Value("${source_routes_path}")
    private String source;
    @Value("${split_routes_path}")
    private String splitFolder;

    /**
     * Разбивает исходный файл с маршрутами на разные файлы
     */
    public void splitSource() {
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(source))) {
                String nextLine = reader.readLine();
                while (nextLine != null) {
                    if (!nextLine.isEmpty()) {
                        String routeId = nextLine.substring(0, nextLine.indexOf(' '));
                        Files.write(Path.of(splitFolder + routeId + ".txt"), nextLine.getBytes());
                    }
                    nextLine = reader.readLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedReader getRoutesReader() throws IOException {
        return new BufferedReader(new FileReader(source));
    }

    /**
     * Возвращает содержимое маршрута из файла по его id
     *
     * @param routeId   id маршрута
     * @return          строка с содержимым маршрута
     */
    public String getRoute(String routeId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(splitFolder + routeId + ".txt"))) {
            return reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
