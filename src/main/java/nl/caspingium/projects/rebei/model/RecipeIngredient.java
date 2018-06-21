package nl.caspingium.projects.rebei.model;

public class RecipeIngredient {

    private int id;
    private Ingredient ingredient;
    private double quantity;

    public RecipeIngredient(int id, Ingredient ingredient, double quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

}
