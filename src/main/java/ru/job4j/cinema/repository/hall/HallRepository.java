package ru.job4j.cinema.repository.hall;

import ru.job4j.cinema.model.Hall;

public interface HallRepository {

    Hall findById(int id);
}