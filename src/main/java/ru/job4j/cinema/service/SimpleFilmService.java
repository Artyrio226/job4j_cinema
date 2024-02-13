package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FilmRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimpleFilmService implements FilmService {

    private final ConcurrentHashMap<Integer, FilmDto> dtoFilms = new ConcurrentHashMap<>();
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
        var films = filmRepository.findAll();
        for (Film film : films) {
            FilmDto filmDto = getFilmDto(film);
            dtoFilms.putIfAbsent(film.getId(), filmDto);
        }
        return dtoFilms.values();
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
