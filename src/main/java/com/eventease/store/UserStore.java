package com.eventease.store;

import com.eventease.model.User;

public class UserStore {
    private static final int MAX = 50;
    private final User[] users = new User[MAX];
    private int count = 0;

    public void add(User u) {
        if (count < MAX) {
            users[count++] = u;
        }
    }

    public User findById(int id) {
        for (int i = 0; i < count; i++) {
            if (users[i].getId() == id) return users[i];
        }
        return null;
    }

    public User[] getAll() {
        User[] result = new User[count];
        for (int i = 0; i < count; i++) result[i] = users[i];
        return result;
    }
}
