package com.eventease.controller;

import com.eventease.exceptions.CapacityFullException;
import com.eventease.exceptions.DuplicateRegistrationException;
import com.eventease.exceptions.EventCancelledException;
import com.eventease.exceptions.VenueBookedException;
import com.eventease.model.Event;
import com.eventease.model.User;
import com.eventease.model.Venue;
import com.eventease.store.EventStore;
import com.eventease.store.UserStore;
import com.eventease.store.VenueStore;

import java.util.Scanner;

public class EventSystem {

    private final Scanner sc = new Scanner(System.in);

    private final VenueStore venueStore = new VenueStore();
    private final EventStore eventStore = new EventStore();
    private final UserStore userStore = new UserStore();

    public EventSystem() {
        // some seed data so app is not empty
        venueStore.add(new Venue(1, "Main Hall", 100));
        venueStore.add(new Venue(2, "Conference Room", 40));

        userStore.add(new User(1, "Rohan"));
        userStore.add(new User(2, "Arjun"));
    }

    public void start() {
        while (true) {
            System.out.println("\n===== EventEase =====");
            System.out.println("1. Event Manager");
            System.out.println("2. Attendee");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> managerMenu();
                case 2 -> attendeeMenu();
                case 0 -> {
                    System.out.println("Exiting... Bye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ---------------- MANAGER MENU ----------------

    private void managerMenu() {
        while (true) {
            System.out.println("\n--- Manager Menu ---");
            System.out.println("1. Create Venue");
            System.out.println("2. List Venues");
            System.out.println("3. Create Event");
            System.out.println("4. List Events");
            System.out.println("5. Cancel Event");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> createVenue();
                case 2 -> listVenues();
                case 3 -> createEvent();
                case 4 -> listEvents();
                case 5 -> cancelEvent();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createVenue() {
        System.out.print("Venue ID (int): ");
        int id = readInt();
        System.out.print("Venue name: ");
        String name = sc.nextLine();
        System.out.print("Capacity: ");
        int capacity = readInt();

        Venue existing = venueStore.findById(id);
        if (existing != null) {
            System.out.println("Venue with this ID already exists.");
            return;
        }

        venueStore.add(new Venue(id, name, capacity));
        System.out.println("Venue created.");
    }

    private void listVenues() {
        System.out.println("\n--- Venues ---");
        Venue[] venues = venueStore.getAll();
        for (Venue v : venues) {
            System.out.println(v);
        }
        if (venues.length == 0) {
            System.out.println("(no venues yet)");
        }
    }

    private void createEvent() {
        System.out.print("Event ID: ");
        int id = readInt();
        if (eventStore.findById(id) != null) {
            System.out.println("Event ID already used.");
            return;
        }

        System.out.print("Title: ");
        String title = sc.nextLine();
        System.out.print("Description: ");
        String desc = sc.nextLine();

        System.out.print("Start hour (0-23): ");
        int sh = readInt();
        System.out.print("End hour (1-24): ");
        int eh = readInt();

        if (sh < 0 || sh > 23 || eh < 1 || eh > 24 || eh <= sh) {
            System.out.println("Invalid time range.");
            return;
        }

        listVenues();
        System.out.print("Venue ID: ");
        int vid = readInt();

        Venue venue = venueStore.findById(vid);
        if (venue == null) {
            System.out.println("Venue not found.");
            return;
        }

        try {
            if (eventStore.isVenueBooked(vid, sh, eh)) {
                throw new VenueBookedException("Venue is already booked in this time slot.");
            }

            System.out.print("Event capacity (<= venue capacity " + venue.getCapacity() + "): ");
            int cap = readInt();
            if (cap <= 0 || cap > venue.getCapacity()) {
                System.out.println("Invalid capacity.");
                return;
            }

            Event e = new Event(id, title, desc, sh, eh, vid, cap);
            eventStore.add(e);
            System.out.println("Event created.");
        } catch (VenueBookedException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }

    private void listEvents() {
        System.out.println("\n--- Events ---");
        Event[] events = eventStore.getAll();
        for (Event e : events) {
            System.out.println(e);
        }
        if (events.length == 0) {
            System.out.println("(no events yet)");
        }
    }

    private void cancelEvent() {
        System.out.print("Event ID to cancel: ");
        int id = readInt();
        Event e = eventStore.findById(id);
        if (e == null) {
            System.out.println("Event not found.");
            return;
        }
        e.cancel();
        System.out.println("Event cancelled.");
    }

    // ---------------- ATTENDEE MENU ----------------

    private void attendeeMenu() {
        while (true) {
            System.out.println("\n--- Attendee Menu ---");
            System.out.println("1. List Events");
            System.out.println("2. Register for Event");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> listEvents();
                case 2 -> registerForEvent();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void registerForEvent() {
        listEvents();
        System.out.print("Event ID: ");
        int eid = readInt();

        Event e = eventStore.findById(eid);
        if (e == null) {
            System.out.println("Event not found.");
            return;
        }

        listUsers();
        System.out.print("Your User ID: ");
        int uid = readInt();

        User u = userStore.findById(uid);
        if (u == null) {
            System.out.println("User not found.");
            return;
        }

        try {
            e.register(uid);
            System.out.println("Registered successfully.");
        } catch (DuplicateRegistrationException | CapacityFullException | EventCancelledException ex) {
            System.out.println("Cannot register: " + ex.getMessage());
        }
    }

    private void listUsers() {
        System.out.println("\n--- Users ---");
        User[] users = userStore.getAll();
        for (User u : users) {
            System.out.println(u);
        }
        if (users.length == 0) {
            System.out.println("(no users yet)");
        }
    }

    // ---------------- Helper ----------------

    private int readInt() {
        while (true) {
            String s = sc.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}
