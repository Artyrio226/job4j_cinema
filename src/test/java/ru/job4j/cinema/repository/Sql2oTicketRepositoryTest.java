package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

class Sql2oTicketRepositoryTest {
    private static Sql2oTicketRepository sql2oTicketRepository;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFileRepository.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);
        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
    }

    @AfterEach
    public void clearTickets() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("TRUNCATE TABLE tickets RESTART IDENTITY");
            query.executeUpdate();
        }
    }

    /**
     * Тест на метод findUniqueTicket().
     * Возвращает билет, если он куплен, по id сеанса, ряду и месту.
     * Обёрнут в Optional.
     */
    @Test
    public void whenSaveTicketThenGetSame() {
        var ticket = new Ticket(1, 1, 2, 2, 1);

        var saveTicket = sql2oTicketRepository.save(ticket);
        var resultTicket = sql2oTicketRepository.findUniqueTicket(
                saveTicket.getSessionId(),
                saveTicket.getRowNumber(),
                saveTicket.getPlaceNumber());

        Assertions.assertTrue(resultTicket.isPresent());

        assertThat(saveTicket).usingRecursiveComparison().isEqualTo(resultTicket.orElseThrow());
    }

    /**
     * Тест на метод findAllTicket().
     * Возвращает список всех купленных билетов по id сеанса.
     */
    @Test
    public void whenSaveSeveralThenGetAll() {
        var ticket1 = sql2oTicketRepository.save(new Ticket(0, 1, 1, 1, 1));
        var ticket2 = sql2oTicketRepository.save(new Ticket(0, 1, 2, 2, 2));
        var ticket3 = sql2oTicketRepository.save(new Ticket(0, 1, 3, 3, 3));

        var result = sql2oTicketRepository.findAllTicket(1);

        assertThat(result).isEqualTo(List.of(ticket1, ticket2, ticket3));
    }
}