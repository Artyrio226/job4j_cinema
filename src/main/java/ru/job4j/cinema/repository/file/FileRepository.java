package ru.job4j.cinema.repository.file;

import ru.job4j.cinema.model.File;

public interface FileRepository {
    File findById(int id);

}