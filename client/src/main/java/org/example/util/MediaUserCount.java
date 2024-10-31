package org.example.util;

import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class MediaUserCount {
    private String title;
    private long userCount;
    private final List<User> users=new ArrayList<>();

    public MediaUserCount() {

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public void addUser(User user) {
        users.add(user); // Adiciona um usuário
    }

    public List<User> getUsers() {
        return users;
    }

}
