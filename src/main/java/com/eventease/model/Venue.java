package com.eventease.model;

public class Venue {
    private int id;
    private String name;
    private int capacity;

    public Venue(int id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() {
        return id + " | " + name + " | cap=" + capacity;
    }
}
