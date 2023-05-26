package com.example.fitness;

import java.util.ArrayList;

public class FoodList {
    private ArrayList<Food> foodList = new ArrayList<>();

    public FoodList(ArrayList<Food> wordList) {
        this.foodList = wordList;
    }

    public FoodList() {
    }

    public ArrayList<Food> getWordList() {
        return foodList;
    }

    public void setWordList(ArrayList<Food> denemeArrayList) {
        this.foodList = denemeArrayList;
    }
}
