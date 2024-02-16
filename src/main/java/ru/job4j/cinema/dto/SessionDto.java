package ru.job4j.cinema.dto;

import ru.job4j.cinema.model.Film;

import java.util.Objects;

public class SessionDto {
    private int id;
    private Film film;
    private HallDto hallDto;
    private String startTime;
    private int price;

    public SessionDto(int id, Film film, HallDto hallDto, String startTime, int price) {
        this.id = id;
        this.film = film;
        this.hallDto = hallDto;
        this.startTime = startTime;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public HallDto getHall() {
        return hallDto;
    }

    public void setHall(HallDto hallDto) {
        this.hallDto = hallDto;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SessionDto that = (SessionDto) o;
        return id == that.id && Objects.equals(hallDto, that.hallDto) && Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hallDto, startTime);
    }
}
