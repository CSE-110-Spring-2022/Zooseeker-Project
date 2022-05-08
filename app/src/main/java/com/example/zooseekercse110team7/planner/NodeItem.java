package com.example.zooseekercse110team7.planner;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;


/**
 * String   id          - unique id
 * String   name        - name of the exhibit/intersection/etc. Example: "Gorillas"
 * String   kind        - category the node belongs to, Example: "exhibit"
 * String   tags        - string of comma separated tags, Example: "mammal, furry"
 * bool     completed   - flag that determines if the user completed/visited the item
 * bool     onPlanner   - flag that determines if item was added to the planner by the user
 * */
@Entity(tableName = "node_items")
public class NodeItem {
    @PrimaryKey (autoGenerate = false) @NonNull
    public String id;
    @NonNull
    public String name, kind;

    @TypeConverters(StringListToGsonConverter.class)
    public List<String> tags;
    /**
     * This subclass parses an array of strings into a comma separated string and parses comma
     * separated strings into a list. This is mainly used by the `NodeDatabase`.
     * */
    public static class StringListToGsonConverter{
        @TypeConverter
        public static List<String> restoreList(String tagListString){
            Log.d("RESTORE", tagListString);
            if(tagListString.equals("\"NULL\"")){
                return Collections.emptyList();
            }
            return new Gson().fromJson(tagListString, new TypeToken<List<String>>(){}.getType());
        }
        @TypeConverter
        public static String saveList(List<String> tags) {
            if(tags.isEmpty()){ return new Gson().toJson("NULL"); }
            return new Gson().toJson(tags);
        }
    }

    public boolean completed, onPlanner;

    //constructor
    public NodeItem(String id, String name, String kind, List<String> tags){
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.tags = tags;
        this.completed = false; // by default it's an uncompleted task
        this.onPlanner = false; // by default it's not added to the planner
    }

    /**
     * Converts any NodeItem object into a string
     * */
    @Override
    public String toString() {
        return "NodeItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", tags='" + tags + '\'' +
                ", completed=" + completed +
                ", onPlanner=" + onPlanner +
                '}';
    }

    /**
     * Parses the JSON file
     *
     * @param context the context within the application -- activity
     * @param path location of the JSON file
     *
     * @return list of `NodeItem`s
     * */
    public static List<NodeItem> loadJSON(Context context, String path){
        Log.d("NodeItem_Load_JSON", path);
        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<NodeItem>>(){}.getType();
            return gson.fromJson(reader,type);
        }catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

    /**
     * Determines if THIS and another NodeItem are equal in terms of strings.
     * Note: it checks if the id's are the same!
     *
     * @param nodeItem a `NodeItem` to compare
     * @return True (they are equal) False (they are NOT equal)
     * */
    public boolean equals(NodeItem nodeItem){
        return this.toString().equals(nodeItem.toString());
    }
}
