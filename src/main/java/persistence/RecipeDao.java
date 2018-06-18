package persistence;

import model.Ingredient;
import model.Recipe;

import java.util.ArrayList;

public interface RecipeDao {
    ArrayList<Recipe> findAll(boolean showIngredients);
    ArrayList<Recipe> findbyIngredients(ArrayList<Ingredient> ingredients,boolean showIngredients);
    Recipe findById(int id,boolean showIngredients);
}
