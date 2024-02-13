package ru.job4j.cinema.dto;

import ru.job4j.cinema.model.Hall;

import java.util.ArrayList;
import java.util.List;

public class HallDto {
    private int id;
    private String name;
    private int[][] schema;
    private List<Integer> rows;
    private List<Integer> places;
    private String description;

    public HallDto(Hall hall) {
        this.id = hall.getId();
        this.name = hall.getName();
        this.schema = createSchema(hall.getRowCount(), hall.getPlaceCount());
        this.rows = createRows();
        this.description = hall.getDescription();
    }

    private int[][] createSchema(int row, int place) {
        return new int[row][place];
    }

    private List<Integer> createRows() {
        List<Integer> rsl = new ArrayList<>();
        for (int i = 0; i < schema.length; i++) {
            rsl.add(i);
        }
        return rsl;
    }

    private List<Integer> createPlaces(int row) {
        List<Integer> rsl = new ArrayList<>();
        for (int i = 0; i < schema[row].length; i++) {
            rsl.add(schema[row][i]);
        }
        return rsl;
    }

    public List<Integer> getNumberPlaces(int row) {
        List<Integer> rsl = new ArrayList<>();
        for (int i = 0; i < schema[row].length; i++) {
            rsl.add(i + 1);
        }
        return rsl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[][] getSchema() {
        return schema;
    }

    public void setSchema(int[][] schema) {
        this.schema = schema;
    }

    public List<Integer> getRows() {
        return rows;
    }

    public void setRows(List<Integer> rows) {
        this.rows = rows;
    }

    public List<Integer> getPlaces(int row) {
        return createPlaces(row);
    }

    public void setPlaces(List<Integer> places) {
        this.places = places;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
