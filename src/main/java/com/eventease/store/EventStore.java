package com.eventease.store;

import com.eventease.model.Event;

public class EventStore {
    private static final int MAX = 50;
    private final Event[] events = new Event[MAX];
    private int count = 0;

    public void add(Event e) {
        if (count < MAX) {
            events[count++] = e;
        }
    }

    public Event findById(int id) {
        for (int i = 0; i < count; i++) {
            if (events[i].getId() == id) return events[i];
        }
        return null;
    }

    public Event[] getAll() {
        Event[] result = new Event[count];
        for (int i = 0; i < count; i++) result[i] = events[i];
        return result;
    }

    // check overlapping times for same venue
    public boolean isVenueBooked(int venueId, int startHour, int endHour) {
        for (int i = 0; i < count; i++) {
            Event e = events[i];
            if (e.getVenueId() != venueId) continue;
            if (e.isCancelled()) continue;
            // overlap condition
            if (startHour < e.getEndHour() && e.getStartHour() < endHour) {
                return true;
            }
        }
        return false;
    }
}
