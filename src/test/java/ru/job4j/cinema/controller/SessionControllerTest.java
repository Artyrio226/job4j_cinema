package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.service.SessionService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SessionControllerTest {
    private SessionService sessionService;
    private SessionController sessionController;

    @BeforeEach
    public void initService() {
        sessionService = mock(SessionService.class);
        sessionController = new SessionController(sessionService);
    }

    /**
     * Тест на метод getAll().
     * Метод выводит список сеансов сохраненных в базу данных на странице /sessions
     */
    @Test
    public void whenRequestSessionListThenGetPageWithSessions() {
        var film1 = new Film(1, "test1", "desc1", 2022, 1,
                16, 120, 1);
        var film2 = new Film(2, "test2", "desc2", 2022, 2,
                16, 120, 2);
        var hallDto1 = new HallDto(new Hall(1, "testHall1", 5, 5, "descHall1"));
        var hallDto2 = new HallDto(new Hall(2, "testHall2", 7, 7, "descHall2"));
        var session1 = new SessionDto(
                1, film1, hallDto1, "15-02-2024", 300);
        var session2 = new SessionDto(
                2, film2, hallDto2, "16-02-2024", 500);
        var expectedSession = List.of(session1, session2);
        when(sessionService.findAll()).thenReturn(expectedSession);

        var model = new ConcurrentModel();
        var view = sessionController.getAll(model);
        var actualSessions = model.getAttribute("filmSessions");

        assertThat(view).isEqualTo("sessions/list");
        assertThat(actualSessions).isEqualTo(expectedSession);
    }

    /**
     * Тест на метод findById().
     * Выводит найденный по id сеанс на страницу сеанса - sessions/one.
     */
    @Test
    public void whenRequestSessionByIdThenGetOneSessionPage() {
        var film1 = new Film(1, "test1", "desc1", 2022, 1,
                16, 120, 1);
        var hallDto1 = new HallDto(new Hall(1, "testHall1", 5, 5, "descHall1"));
        var session1 = new SessionDto(
                1, film1, hallDto1, "15-02-2024", 300);
        var expectedSession = Optional.of(session1);
        when(sessionService.findById(1)).thenReturn(expectedSession);

        var model = new ConcurrentModel();
        var view = sessionController.findById(model, session1.getId(), new MockHttpServletRequest());
        var actualSession = model.getAttribute("filmSession");

        assertThat(view).isEqualTo("sessions/one");
        assertThat(actualSession).isEqualTo(expectedSession.get());
    }

    /**
     * Тест на метод findById().
     * Выводит страницу с ошибкой, при сценарии, когда сеанс не найден по id.
     */
    @Test
    public void whenRequestNonExistingSessionByIdThenGetMessageAnd404Page() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        var expectedException = new RuntimeException("Сеанс с указанным идентификатором не найден");
        when(sessionService.findById(0)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = sessionController.findById(model, 0, request);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

}