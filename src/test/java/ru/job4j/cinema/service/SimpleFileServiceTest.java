package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.repository.FileRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFileServiceTest {

    private FileRepository fileRepository;
    private FileService fileService;

    @BeforeEach
    public void initServices() {
        fileRepository = mock(FileRepository.class);
        fileService = new SimpleFileService(fileRepository);
    }

    /**
     * Тест на метод findById().
     * Сервис находит файл по id и
     * возвращает FileDto.
     */
    @Test
    public void whenFindFileThenGetFile() {
        var file = new File("Капитан Марвел 2", "files/CapMarvel2.jpg");
        file.setId(1);
        when(fileRepository.findById(file.getId())).thenReturn(file);

        var foundFileDto = fileService.getFileById(file.getId());

        assertThat(foundFileDto.getName()).isEqualTo(file.getName());
        assertThat(foundFileDto).isInstanceOfAny(FileDto.class);
    }
}