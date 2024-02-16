package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleTicketServiceTest {

    private TicketRepository ticketRepository;
    private TicketService ticketService;

    @BeforeEach
    public void initServices() {
        ticketRepository = mock(TicketRepository.class);
        ticketService = new SimpleTicketService(ticketRepository);
    }

    /**
     * Тест на метод save().
     * При покупке билета, билет сохраняется в БД,
     * возвращается Optional<Ticket>.
     */
    @Test
    public void whenSaveTicketThenGetTicket() {
        var ticket = new Ticket(1, 1, 3, 3, 1);
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        var savedTicket = ticketService.save(ticket, ticket.getSessionId(), ticket.getUserId());

        assertThat(savedTicket).isEqualTo(ticket);
    }

    /**
     * Тест на метод findUniqueTicket().
     * Сервис находит билет в БД по id сеанса, ряду и месту,
     * возвращается Optional<Ticket>.
     */
    @Test
    public void whenFindTicketThenGetOptionalTicket() {
        var ticket = new Ticket(1, 1, 3, 3, 1);
        when(ticketRepository.findUniqueTicket(ticket.getSessionId(),
                ticket.getRowNumber(), ticket.getPlaceNumber())).thenReturn(Optional.of(ticket));

        var optionalTicket = ticketService.findUniqueTicket(ticket.getSessionId(),
                ticket.getRowNumber(), ticket.getPlaceNumber());

        assertThat(optionalTicket.get()).isEqualTo(ticket);
    }

    /**
     * Тест на метод findUniqueTicket().
     * Сервис не находит билет в БД по id сеанса, ряду и месту,
     * возвращается пустой Optional<Ticket>.
     */
    @Test
    public void whenFindTicketThenGetEmptyOptionalTicket() {
        var ticket = new Ticket(1, 1, 3, 3, 1);
        when(ticketRepository.findUniqueTicket(ticket.getSessionId(),
                ticket.getRowNumber(), ticket.getPlaceNumber())).thenReturn(Optional.empty());

        var optionalTicket = ticketService.findUniqueTicket(ticket.getSessionId(),
                ticket.getRowNumber(), ticket.getPlaceNumber());

        assertThat(optionalTicket).isEmpty();
    }
}