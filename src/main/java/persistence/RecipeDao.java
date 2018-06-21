package persistence;

import model.Ingredient;
import model.Recipe;
import model.UserIngredient;

import java.util.ArrayList;

public interface RecipeDao {
    ArrayList<Recipe> findAll(boolean showIngredients);
    ArrayList<Recipe> findbyIngredients(ArrayList<UserIngredient> ingredients, boolean showIngredients);
    Recipe findById(int id,boolean showIngredients);
}
