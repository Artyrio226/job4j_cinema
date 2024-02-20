package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.hall.HallRepository;
import ru.job4j.cinema.service.hall.HallService;
import ru.job4j.cinema.service.hall.SimpleHallService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleHallServiceTest {

    private HallRepository hallRepository;
    private HallService hallService;

    @BeforeEach
    public void initServices() {
        hallRepository = mock(HallRepository.class);
        hallService = new SimpleHallService(hallRepository);
    }

    /**
     * Тест на метод findById().
     * Сервис находит зал по id и
     * возвращает HallDto.
     */
    @Test
    public void whenFindHallThenGetHall() {
        var hall = new Hall(2, "Главный зал", 15, 12, "Основной");
        when(hallRepository.findById(hall.getId())).thenReturn(hall);

        var foundHallDto = hallService.findById(hall.getId());

        assertThat(foundHallDto.getName()).isEqualTo(hall.getName());
        assertThat(foundHallDto).isInstanceOfAny(HallDto.class);
    }
}