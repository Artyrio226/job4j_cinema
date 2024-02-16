package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.HallDto;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.SessionRepository;
import ru.job4j.cinema.repository.TicketRepository;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SimpleSessionService implements SessionService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEEE dd MMMM HH:mm");
    private final SessionRepository sessionRepository;
    private final HallService hallService;
    private final FilmRepository filmRepository;
    private final TicketRepository ticketRepository;

    public SimpleSessionService(SessionRepository sessionRepository,
                                HallService hallService,
                                FilmRepository filmRepository,
                                TicketRepository ticketRepository) {
        this.sessionRepository = sessionRepository;
        this.hallService = hallService;
        this.filmRepository = filmRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Optional<SessionDto> findById(int id) {
        var session = sessionRepository.findById(id);
        return Optional.of(getSessionDto(session));
    }

    @Override
    public Collection<SessionDto> findAll() {
        List<SessionDto> list = new ArrayList<>();
        var sessions = sessionRepository.findAll();
        for (Session session : sessions) {
            var sessionDto = getSessionDto(session);
            list.add(sessionDto);
        }
        return list;
    }

    private SessionDto getSessionDto(Session session) {
        var hallDto = hallService.findById(session.getHallId());
        markPlaces(hallDto, session);
        return new SessionDto(session.getId(),
                filmRepository.findById(session.getFilmId()).get(),
                hallDto, FORMATTER.format(session.getStartTime()), session.getPrice());
    }

    private void markPlaces(HallDto hall, Session session) {
        int[][] tmp = hall.getSchema();
        Collection<Ticket> tickets = ticketRepository.findAllTicket(session.getId());
        if (!tickets.isEmpty()) {
            for (Ticket ticket : tickets) {
                tmp[ticket.getRowNumber() - 1][ticket.getPlaceNumber() - 1] = 1;
            }
        }
        hall.setSchema(tmp);
    }
}
