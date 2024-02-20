package ru.job4j.cinema.repository.genre;

import ru.job4j.cinema.model.Genre;

public interface GenreRepository {

    Genre findById(int id);
}
