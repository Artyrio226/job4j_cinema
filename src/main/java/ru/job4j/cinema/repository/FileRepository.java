package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.File;

public interface FileRepository {
    File findById(int id);

}