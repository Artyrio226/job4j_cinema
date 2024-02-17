package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketControllerTest {
    private TicketService ticketService;
    private TicketController ticketController;

    @BeforeEach
    public void initServices() {
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(ticketService);
    }

    /**
     * Тест на метод register().
     * При успешном сохранении билета в БД метод возвращает страницу с сообщением
     * об успешной покупке билета.
     */
    @Test
    public void whenCreateTicketThenGetSuccessPageWithMessage() {
        var model = new ConcurrentModel();
        var ticket = new Ticket(1, 1, 1, 1, 1);
        var film1 = new Film(1, "test1", "desc1", 2022, 1,
                16, 120, 1);
        var hallDto1 = new HallDto(new Hall(1, "testHall1", 5, 5, "descHall1"));
        var sessionDto1 = new SessionDto(
                1, film1, hallDto1, "15-02-2024", 300);
        var user = new User(1, "test", "qw@mail.ru", "qwerty");

        var mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("sessionDto", sessionDto1);
        mockHttpSession.setAttribute("user", user);

        when(ticketService.findUniqueTicket(any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(Optional.of(ticket));

        var view = ticketController.register(model, mockHttpSession, ticket);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("tickets/failure");
        assertThat(actualMessage).isEqualTo("Не удалось приобрести билет на заданное место. "
                + "Вероятно оно уже занято. Перейдите на страницу бронирования билетов "
                + "и попробуйте снова.");
    }

    /**
     * Тест на метод register().
     * При ошибке сохранения билета в БД метод возвращает страницу с сообщением
     * об ошибке.
     */
    @Test
    public void whenCreateTicketThenGetFailurePageWithMessage() {
        var ticket = new Ticket(1, 1, 1, 1, 1);
        var film1 = new Film(1, "test1", "desc1", 2022, 1,
                16, 120, 1);
        var hallDto1 = new HallDto(new Hall(1, "testHall1", 5, 5, "descHall1"));
        var sessionDto1 = new SessionDto(
                1, film1, hallDto1, "15-02-2024", 300);
        var user = new User(1, "test", "qw@mail.ru", "qwerty");

        var mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("sessionDto", sessionDto1);
        mockHttpSession.setAttribute("user", user);

        var model = new ConcurrentModel();
        var ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        var sessionIdArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        var userArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        when(ticketService.findUniqueTicket(any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(Optional.empty());
        when(ticketService.save(ticketArgumentCaptor.capture(),
                sessionIdArgumentCaptor.capture(), userArgumentCaptor.capture()))
                .thenReturn(ticket);

        var view = ticketController.register(model, mockHttpSession, ticket);
        var actualMessage = model.getAttribute("message");
        var actualTicket = ticketArgumentCaptor.getValue();
        var actualSessionId = sessionIdArgumentCaptor.getValue();
        var actualUserId = userArgumentCaptor.getValue();

        assertThat(actualSessionId).isEqualTo(sessionDto1.getId());
        assertThat(actualUserId).isEqualTo(user.getId());
        assertThat(actualTicket).isEqualTo(ticket);
        assertThat(view).isEqualTo("tickets/success");
        assertThat(actualMessage).isEqualTo("Вы успешно приобрели билет. Ряд: 1, место: 1");
    }
}