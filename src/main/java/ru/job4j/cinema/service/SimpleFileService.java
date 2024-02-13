package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.repository.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SimpleFileService implements FileService {
    private final FileRepository fileRepository;

    public SimpleFileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    private byte[] readFileAsBytes(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileDto getFileById(int id) {
        var file = fileRepository.findById(id);
        var content = readFileAsBytes(file.getPath());
        return new FileDto(file.getName(), content);
    }
}
