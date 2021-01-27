package com.pnk.trainme_java;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Proposal {
    public String price;
    public String perweek;
    public String total;
    public String message;

    public Proposal() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Proposal(String price, String perweek, String total, String message) {
        this.price = price;
        this.message = message;
        this.perweek = perweek;
        this.total = total;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("price", price);
        result.put("message", message);
        result.put("perweek", perweek);
        result.put("total", total);

        return result;
    }
}
