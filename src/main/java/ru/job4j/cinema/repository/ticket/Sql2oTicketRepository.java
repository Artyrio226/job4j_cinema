package ru.job4j.cinema.repository.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.user.Sql2oUserRepository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oTicketRepository implements TicketRepository {
    private static final Logger LOG = LoggerFactory.getLogger(Sql2oUserRepository.class.getName());
    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO tickets(session_id, row_number, place_number, user_id)
                    VALUES (:sessionId, :rowNumber, :placeNumber, :userId)
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("sessionId", ticket.getSessionId())
                    .addParameter("rowNumber", ticket.getRowNumber())
                    .addParameter("placeNumber", ticket.getPlaceNumber())
                    .addParameter("userId", ticket.getUserId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);
            return Optional.of(ticket);
        } catch (Exception exception) {
            LOG.error("Неудачная покупка билета. Exception in log example", exception);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> findUniqueTicket(int sessionId, int rowNumber, int placeNumber) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery(
                    "SELECT * FROM tickets WHERE session_id = :sessionId and row_number = :rowNumber and place_number = :placeNumber");
            query.addParameter("sessionId", sessionId);
            query.addParameter("rowNumber", rowNumber);
            query.addParameter("placeNumber", placeNumber);
            var ticket = query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetchFirst(Ticket.class);
            return Optional.ofNullable(ticket);
        }
    }

    @Override
    public Collection<Ticket> findAllTicket(int sessionId) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM tickets WHERE session_id = :sessionId");
            query.addParameter("sessionId", sessionId);
            return query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetch(Ticket.class);
        }
    }
}
