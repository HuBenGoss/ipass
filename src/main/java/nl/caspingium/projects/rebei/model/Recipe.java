package nl.caspingium.projects.rebei.model;

import java.util.ArrayList;

public class Recipe {

    private int id;

    private String title;
    private int cookingTime;

    private ArrayList<RecipeIngredient> ingredients;
    private ArrayList<Step> steps;
    private byte[] imageData;
    private User writer;

    public Recipe(int id, String title, int cookingTime, User writer,byte[] imageData) {
        this.id = id;
        this.title = title;
        this.cookingTime = cookingTime;
        this.ingredients = new ArrayList<RecipeIngredient>();
        this.writer = writer;
        this.imageData = imageData;
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

    public int getCookingTime() {
        return cookingTime;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public void addStep(Step step) {
        this.steps.add(step);
    }

    public User getWriter() {
        return writer;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setId(int id) {
        this.id = id;
    }
}
