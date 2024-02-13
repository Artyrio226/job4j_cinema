package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Session;

import java.util.Collection;

public interface SessionRepository {

    Session findById(int id);

    Collection<Session> findAll();
}
