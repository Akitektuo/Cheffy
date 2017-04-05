package com.akitektuo.cheffy.model;

import java.util.ArrayList;

public class Recipe {

    // Converted int duration into long for hours to fit
    // String[] converted to ArrayList<String>
    private int id;
    private String name;
    private String content;
    private long duration;
    private ArrayList<String> ingredients;
    private ArrayList<String> weights;
    private String picture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<String> weights) {
        this.weights = weights;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", duration=" + duration +
                ", ingredients=" + ingredients.toString() +
                ", weights=" + weights.toString() +
                ", picture='" + picture + '\'' +
                '}';
    }
}
