package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.repository.HallRepository;

@Service
public class SimpleHallService implements HallService {
    private final HallRepository hallRepository;

    public SimpleHallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    @Override
    public HallDto findById(int id) {
        return new HallDto(hallRepository.findById(id));
    }
}