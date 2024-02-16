package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class IndexControllerTest {

    /**
     * Тест на метод getIndex().
     * При обращении возвращает страницу "index".
     */
    @Test
    public void whenRequestIndexPageThenGetPageWithHome() {
        IndexController indexController = new IndexController();
        var view = indexController.getIndex();
        assertThat(view).isEqualTo("index");
    }
}