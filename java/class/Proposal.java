package com.pnk.trainme_java;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Proposal {
    public String price;
    public String message;

    public Proposal() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Proposal(String price, String message) {
        this.price = price;
        this.message = message;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Proposal{" +
                "price='" + price + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("price", price);
        result.put("message", message);

        return result;
    }
}
