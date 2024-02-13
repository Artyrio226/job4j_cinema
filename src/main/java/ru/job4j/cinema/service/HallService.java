package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.HallDto;

public interface HallService {

    HallDto findById(int id);
}
