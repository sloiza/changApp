package com.example.android.materialdesigncodelab;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicomoccagatta on 1/8/17.
 */

@IgnoreExtraProperties
public class Changa {
    public String id;
    public String uid;
    public String author;
    public String title;
    public String body;
    public String price;
    public String date;
    public String picture;
    public String status;   // "activa" - "enProceso" - "finalizada"
    public int category;
    public Map<String, Object> postulantes;

    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Changa() {
        // Default constructor required for calls to DataSnapshot.getValue(Changa.class)
    }

    public Changa(String id, String uid, String author, String title, String body, String price,
                            String date, String picture, int category, String status) {
        this.id = id;
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
        this.price = price;
        this.date = date;
        this.picture = picture;
        this.status = status;
        this.category = category;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("price", price);
        result.put("date", date);
        result.put("picture", picture);
        result.put("status", status);
        result.put("category", category);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }

    public boolean setChangaInProcess(){
        if (status.equals("activa")) {
            status = "enProceso";
            return true;
        }
        return false;
    }

    public boolean setChangaFinished(){
        if (status.equals("enProceso")) {
            status = "finalizada";
            return true;
        }
        return false;
    }
}
