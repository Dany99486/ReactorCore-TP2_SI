package org.example.util;

import java.util.ArrayList;
import java.util.List;

public class UserMediaInfo {
    private final String name;
    private final int age;
    private final String gender;

    private final List<String> mediaTitles;

    public UserMediaInfo(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.mediaTitles = new ArrayList<>();
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public List<String> getMediaTitles() { return mediaTitles; }

    public void addMediaTitle(String title) { this.mediaTitles.add(title); }
}
