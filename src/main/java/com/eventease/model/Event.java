package com.eventease.model;

import com.eventease.exceptions.CapacityFullException;
import com.eventease.exceptions.DuplicateRegistrationException;
import com.eventease.exceptions.EventCancelledException;

public class Event {
    private int id;
    private String title;
    private String description;
    private int startHour;
    private int endHour;
    private int venueId;
    private int capacity;
    private boolean cancelled = false;

    private int[] attendeeIds;
    private int attendeeCount = 0;

    public Event(int id, String title, String description,
                 int startHour, int endHour, int venueId, int capacity) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.startHour = startHour;
        this.endHour = endHour;
        this.venueId = venueId;
        this.capacity = capacity;
        this.attendeeIds = new int[capacity]; // simple fixed-size
    }

    public int getId() { return id; }
    public int getVenueId() { return venueId; }
    public int getStartHour() { return startHour; }
    public int getEndHour() { return endHour; }
    public boolean isCancelled() { return cancelled; }

    public void cancel() { this.cancelled = true; }

    public void register(int userId)
            throws DuplicateRegistrationException, CapacityFullException, EventCancelledException {

        if (cancelled) throw new EventCancelledException("Event is cancelled");

        for (int i = 0; i < attendeeCount; i++) {
            if (attendeeIds[i] == userId)
                throw new DuplicateRegistrationException("User already registered");
        }

        if (attendeeCount >= capacity)
            throw new CapacityFullException("Event capacity full");

        attendeeIds[attendeeCount++] = userId;
    }

    @Override
    public String toString() {
        return id + " | " + title + " | " + startHour + "-" + endHour +
                " | venue=" + venueId +
                " | cap=" + capacity +
                " | reg=" + attendeeCount +
                (cancelled ? " | CANCELLED" : "");
    }
}
