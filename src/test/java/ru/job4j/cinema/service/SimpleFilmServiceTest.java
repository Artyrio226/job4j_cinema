package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.film.FilmRepository;
import ru.job4j.cinema.service.film.FilmService;
import ru.job4j.cinema.service.film.SimpleFilmService;
import ru.job4j.cinema.service.genre.GenreService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFilmServiceTest {

    private FilmRepository filmRepository;
    private GenreService genreService;
    private FilmService filmService;

    @BeforeEach
    public void initServices() {
        filmRepository = mock(FilmRepository.class);
        genreService = mock(GenreService.class);
        filmService = new SimpleFilmService(filmRepository, genreService);
    }

    /**
     * Тест на метод findAll().
     * Сервис находит все фильмы и возвращает Collection<FilmDto>.
     */
    @Test
    public void whenRequestFilmListPageThenGetCollectionFilmDto() {
        var filmDto1 = new FilmDto(1, "test1", "desc1", 2022,
                16, 120, "Фэнтези", 1);
        var filmDto2 = new FilmDto(2, "test2", "desc2", 2023,
                18, 158, "Фэнтези", 2);
        var film1 = new Film(1, "test1", "desc1",
                2022, 3, 16, 120, 1);
        var film2 = new Film(2, "test2", "desc2",
                2023, 3, 18, 158, 2);
        var genre = new Genre(3, "Фэнтези");
        var expectedFilms = List.of(film1, film2);
        var expectedFilmsDto = List.of(filmDto1, filmDto2);
        when(filmRepository.findAll()).thenReturn(expectedFilms);
        when(genreService.findById(any(Integer.class))).thenReturn(genre);

        var filmsDto = filmService.findAll();

        assertThat(filmsDto).isEqualTo(expectedFilmsDto);
    }

    /**
     * Тест на метод findById().
     * Находит фильм по id и возвращает Optional<FilmDto>.
     */
    @Test
    public void whenFindByIdFilmThenGetOptionalFilmDto() {
        var film1 = new Film(1, "test1", "desc1",
                2022, 3, 16, 120, 1);
        var filmDto1 = new FilmDto(1, "test1", "desc1", 2022,
                16, 120, "Фэнтези", 1);
        var genre = new Genre(3, "Фэнтези");
        when(filmRepository.findById(film1.getId())).thenReturn(Optional.of(film1));
        when(genreService.findById(any(Integer.class))).thenReturn(genre);

        var optionalFilmDto = filmService.findById(film1.getId());

        assertThat(optionalFilmDto.get()).isEqualTo(filmDto1);
    }

    /**
     * Тест на метод findById().
     * Не находит фильм по id и возвращает пустой Optional<FilmDto>.
     */
    @Test
    public void whenFindByIdFilmThenGetEmptyFilmDto() {
        var film1 = new Film(1, "test1", "desc1",
                2022, 3, 16, 120, 1);
        when(filmRepository.findById(film1.getId())).thenReturn(Optional.empty());

        var optionalFilmDto = filmService.findById(film1.getId());

        assertThat(optionalFilmDto).isEmpty();
    }
}