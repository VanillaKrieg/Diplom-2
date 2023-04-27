package ru.yandex.praktikum.model;


import java.util.Arrays;

public class Order {
    private String[] ingredients;
    private String token;

    public Order(String[] ingredients, String token) {
        this.ingredients = ingredients;
        this.token = token;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ingredients='" + Arrays.toString(ingredients) + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
