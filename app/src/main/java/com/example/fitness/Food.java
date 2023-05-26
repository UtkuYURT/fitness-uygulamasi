package com.example.fitness;

import java.util.HashMap;
import java.util.Map;

public class Food {
    private String date,meal,food;
    static Map<String,Object> map = new HashMap<>();

    public String getDate() {
        return date;
    }

    public void setDate(String word) {
        this.date = date;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }
    public String getFood(){
        return food;
    }

    public void setFood(String food){
        this.food=food;
    }

    public Food(){

    }

    public Food(String date, String meal, String food) {
        this.date = date;
        this.meal = meal;
        this.food = food;
    }

    static public Map<String,Object> convertToMap(Food food){
        map.put("Date", food.date);
        map.put("Meal", food.meal);
        map.put("Food", food.food);
        return map;
    }
}
