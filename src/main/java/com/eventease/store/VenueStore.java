package com.eventease.store;

import com.eventease.model.Venue;

public class VenueStore {
    private static final int MAX = 20;
    private final Venue[] venues = new Venue[MAX];
    private int count = 0;

    public void add(Venue v) {
        if (count < MAX) {
            venues[count++] = v;
        }
    }

    public Venue findById(int id) {
        for (int i = 0; i < count; i++) {
            if (venues[i].getId() == id) return venues[i];
        }
        return null;
    }

    public Venue[] getAll() {
        Venue[] result = new Venue[count];
        for (int i = 0; i < count; i++) result[i] = venues[i];
        return result;
    }
}
