package model;

import java.util.ArrayList;

public class Recipe {

    private int id;

    private String title;
    private double cookingTime;
    private String description;

    private ArrayList<RecipeIngredient> ingredients;
    private User writer;

    public Recipe(int id, String title, double cookingTime, User writer,String description) {
        this.id = id;
        this.title = title;
        this.cookingTime = cookingTime;
        this.ingredients = new ArrayList<RecipeIngredient>();
        this.writer = writer;
        this.description = description;
    }

    public void addIngredient(RecipeIngredient recipeIngredient) {
        ingredients.add(recipeIngredient);
    }

    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getCookingTime() {
        return cookingTime;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public User getWriter() {
        return writer;
    }
}
