package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.SessionService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class FilmControllerTest {
    private FilmService filmService;
    private SessionService sessionService;
    private FilmController filmController;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        sessionService = mock(SessionService.class);
        filmController = new FilmController(filmService, sessionService);
    }

    /**
     * Тест на метод getAll().
     * Метод выводит список фильмов сохраненных в базу данных на странице /films
     */
    @Test
    public void whenRequestFilmListPageThenGetPageWithFilms() {
        var film1 = new FilmDto(1, "test1", "desc1", 2022,
                16, 120, "genre1", 1);
        var film2 = new FilmDto(2, "test2", "desc2", 2023,
                18, 240, "genre2", 2);
        var expectedFilms = List.of(film1, film2);
        when(filmService.findAll()).thenReturn(expectedFilms);

        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        var actualFilms = model.getAttribute("films");

        assertThat(view).isEqualTo("films/list");
        assertThat(actualFilms).isEqualTo(expectedFilms);
    }

    /**
     * Тест на метод getById().
     * Выводит найденный по id фильм на страницу фильма - films/one.
     */
    @Test
    public void whenRequestFilmByIdThenGetOneFilmPage() {
        var film2 = new FilmDto(2, "test2", "desc2", 2023,
                18, 240, "genre2", 2);
        var expectedFilm = Optional.of(film2);
        when(filmService.findById(2)).thenReturn(expectedFilm);

        var model = new ConcurrentModel();
        var view = filmController.getById(model, film2.getId());
        var actualFilm = model.getAttribute("film");

        assertThat(view).isEqualTo("films/one");
        assertThat(actualFilm).isEqualTo(expectedFilm.get());
    }

    /**
     * Тест на метод getById().
     * Выводит страницу с ошибкой, при сценарии, когда фильм не найден по id.
     */
    @Test
    public void whenRequestNonExistingFilmByIdThenGetMessageAnd404Page() {
        var expectedException = new RuntimeException("Фильм с указанным идентификатором не найден");
        when(filmService.findById(0)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = filmController.getById(model, 0);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }
}