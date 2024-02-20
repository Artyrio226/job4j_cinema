package ru.job4j.cinema.service.ticket;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.ticket.TicketRepository;

import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {
    private final TicketRepository ticketRepository;

    public SimpleTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket save(Ticket ticket, int sessionId, int userId) {
        ticket.setSessionId(sessionId);
        ticket.setUserId(userId);
        return ticketRepository.save(ticket);
    }

    @Override
    public Optional<Ticket> findUniqueTicket(int sessionId, int row, int place) {
        return ticketRepository.findUniqueTicket(sessionId, row, place);
    }
}