package ru.job4j.cinema.repository.ticket;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {
    Ticket save(Ticket ticket);
    Optional<Ticket> findUniqueTicket(int sessionId, int row, int place);
    Collection<Ticket> findAllTicket(int sessionId);
}
