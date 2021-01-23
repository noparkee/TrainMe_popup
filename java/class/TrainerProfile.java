package com.pnk.trainme_java;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class TrainerProfile {
    public String name;
    public String age;
    public String sex;
    public String program;
    public String exercise;


    public TrainerProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public TrainerProfile(String name, String age, String sex, String program, String exercise) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.program = program;
        this.exercise = exercise;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("age", age);
        result.put("sex", sex);
        result.put("program", program);
        result.put("exercise", exercise);

        return result;
    }
}
