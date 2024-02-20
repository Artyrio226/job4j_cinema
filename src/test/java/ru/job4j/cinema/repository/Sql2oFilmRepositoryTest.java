package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.file.Sql2oFileRepository;
import ru.job4j.cinema.repository.film.Sql2oFilmRepository;

import java.util.Properties;

class Sql2oFilmRepositoryTest {
    private static Sql2oFilmRepository sql2oFilmRepository;

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
        var sql2o = configuration.databaseClient(datasource);
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
    }

    /**
     * Тест на метод findAll().
     * Находит все фильмы в базе.
     */
    @Test
    public void whenFindAllFilmsThenGetSame() {
        var film = new Film(4, "Наполеон", "История тяжёлого пути Бонапарта к власти через "
                + "призму его изменчивых отношений с любовью всей жизни — Жозефиной.",
                2023, 4, 18, 158, 4);
        assertThat(sql2oFilmRepository.findAll())
                .hasSize(6)
                .element(3)
                .isEqualTo(film);
    }

    /**
     * Тест на метод findById().
     * Находит фильм в базе по id.
     */
    @Test
    public void whenFindByIdFilmThenGetSame() {
        var film = new Film(2, "Бременские музыканты",
                "Трубадур и его друзья-самозванцы — Пес, Кошка, Осел и самовлюбленный Петух — объединились, "
                        + "чтобы совершить подвиг.", 2024, 2, 6, 115, 2);
        assertThat(sql2oFilmRepository.findById(film.getId()).get()).isEqualTo(film);
    }
}