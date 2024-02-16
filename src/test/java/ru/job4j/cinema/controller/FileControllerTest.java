package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.service.FileService;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class FileControllerTest {
    private FileService fileService;
    private FileController fileController;
    private MultipartFile testFile;

    @BeforeEach
    public void initServices() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
        testFile = new MockMultipartFile("testFile.img", new byte[]{1, 2, 3});
    }

    /**
     * Тест на метод getById().
     * Возвращает статус 200 с телом ответа в виде содержимого файла.
     */
    @Test
    public void whenFileGetByIdThenStatus200() throws IOException {
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        var idArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        when(fileService.getFileById(idArgumentCaptor.capture())).thenReturn(fileDto);

        var rsl = fileController.getById(3).getStatusCodeValue();
        var actualId = idArgumentCaptor.getValue();

        assertThat(rsl).isEqualTo(200);
        assertThat(actualId).isEqualTo(3);
    }

    /**
     * Тест на метод getById().
     * Возвращает статус 404.
     */
    @Test
    public void whenFileGetByIdThenStatus404() {
        var idArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        when(fileService.getFileById(idArgumentCaptor.capture())).thenReturn(null);

        var rsl = fileController.getById(5).getStatusCodeValue();
        var actualId = idArgumentCaptor.getValue();

        assertThat(rsl).isEqualTo(404);
        assertThat(actualId).isEqualTo(5);
    }
}