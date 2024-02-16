package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FilmRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final GenreService genreService;

    public SimpleFilmService(FilmRepository filmRepository, GenreService genreService) {
        this.filmRepository = filmRepository;
        this.genreService = genreService;
    }

    @Override
    public Optional<FilmDto> findById(int id) {
        var filmOptional = filmRepository.findById(id);
        if (filmOptional.isEmpty()) {
            return Optional.empty();
        }
        var film = filmOptional.get();
        return Optional.of(getFilmDto(film));
    }

    @Override
    public Collection<FilmDto> findAll() {
        List<FilmDto> list = new ArrayList<>();
        var films = filmRepository.findAll();
        for (Film film : films) {
            FilmDto filmDto = getFilmDto(film);
            list.add(filmDto);
        }
        return list;
    }

    private FilmDto getFilmDto(Film film) {
        return new FilmDto(film.getId(),
                film.getName(), film.getDescription(),
                film.getYear(), film.getMinimalAge(),
                film.getDurationInMinutes(),
                genreService.findById(film.getGenreId()).getName(),
                film.getFileId());
    }
}
