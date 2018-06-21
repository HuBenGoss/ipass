package model;

import java.util.Date;

public class UserIngredient extends RecipeIngredient {

    private Date lastUpdated;

    public UserIngredient(int id, Ingredient ingredient, double quantity,Date lastUpdated) {
        super(id, ingredient, quantity);
        this.lastUpdated = lastUpdated;
    }
}
