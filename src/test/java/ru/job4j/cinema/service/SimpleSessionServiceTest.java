package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.SessionRepository;
import ru.job4j.cinema.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleSessionServiceTest {

    private SessionRepository sessionRepository;
    private SessionService sessionService;
    private HallService hallService;
    private FilmRepository filmRepository;
    private TicketRepository ticketRepository;

    @BeforeEach
    public void initServices() {
        filmRepository = mock(FilmRepository.class);
        sessionRepository = mock(SessionRepository.class);
        hallService = mock(HallService.class);
        ticketRepository = mock(TicketRepository.class);
        sessionService = new SimpleSessionService(sessionRepository, hallService, filmRepository, ticketRepository);
    }

    /**
     * Тест на метод findAll().
     * Сервис находит все сеансы и возвращает Collection<SessionDto>.
     */
    @Test
    public void whenRequestSessionListPageThenGetCollectionSessionDto() {
        var dateFirst1 = LocalDateTime.of(2024, 2, 16, 17, 30);
        var dateSecond1 = LocalDateTime.of(2024, 2, 16, 19, 0);
        var session1 = new Session(4, 1, 1, dateFirst1, dateSecond1, 400);
        var film1 = new Film(1, "test1", "desc1",
                2022, 3, 16, 120, 1);
        var hallDto1 = new HallDto(new Hall(1, "testHall1", 5, 5, "descHall1"));
        var sessionDto1 = new SessionDto(
                4, film1, hallDto1, "пятница 16 февраля 17:30", 400);

        var dateFirst2 = LocalDateTime.of(2024, 2, 16, 14, 30);
        var dateSecond2 = LocalDateTime.of(2024, 2, 16, 17, 0);
        var session2 = new Session(4, 2, 2, dateFirst2, dateSecond2, 400);
        var film2 = new Film(2, "test2", "desc2",
                2022, 3, 16, 120, 1);
        var hallDto2 = new HallDto(new Hall(2, "testHall2", 5, 5, "descHall2"));
        var sessionDto2 = new SessionDto(
                4, film2, hallDto2, "пятница 16 февраля 14:30", 400);
        var expectedSessionsDto = List.of(sessionDto1, sessionDto2);
        var expectedSessions = List.of(session1, session2);
        when(sessionRepository.findById(session1.getId())).thenReturn(session1);
        when(sessionRepository.findById(session2.getId())).thenReturn(session2);
        when(sessionRepository.findAll()).thenReturn(expectedSessions);
        when(hallService.findById(session1.getHallId())).thenReturn(hallDto1);
        when(hallService.findById(session2.getHallId())).thenReturn(hallDto2);
        when(filmRepository.findById(session1.getFilmId())).thenReturn(Optional.of(film1));
        when(filmRepository.findById(session2.getFilmId())).thenReturn(Optional.of(film2));

        var sessionsDto = sessionService.findAll();

        assertThat(sessionsDto).usingRecursiveComparison().isEqualTo(expectedSessionsDto);
    }

    /**
     * Тест на метод findById().
     * Находит сеанс по id и возвращает SessionDto.
     */
    @Test
    public void whenFindByIdSessionThenGetOptionalSessionDto() {
        var dateFirst = LocalDateTime.of(2024, 2, 16, 14, 30);
        var dateSecond = LocalDateTime.of(2024, 2, 16, 17, 0);
        var session = new Session(4, 1, 2, dateFirst, dateSecond, 400);
        var film = new Film(1, "test1", "desc1",
                2022, 3, 16, 120, 1);
        var hallDto = new HallDto(new Hall(1, "testHall1", 5, 5, "descHall1"));
        var sessionDto = new SessionDto(
                4, film, hallDto, "пятница 16 февраля 14:30", 400);
        when(sessionRepository.findById(session.getId())).thenReturn(session);
        when(hallService.findById(session.getHallId())).thenReturn(hallDto);
        when(filmRepository.findById(session.getFilmId())).thenReturn(Optional.of(film));

        var optionalSessionDto = sessionService.findById(session.getId());

        assertThat(optionalSessionDto.get()).usingRecursiveComparison().isEqualTo(sessionDto);
    }
}