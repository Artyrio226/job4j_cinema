package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

public interface TicketService {
    Ticket save(Ticket ticket, int sessionId, int userId);
    Optional<Ticket> findUniqueTicket(int sessionId, int row, int place);
}