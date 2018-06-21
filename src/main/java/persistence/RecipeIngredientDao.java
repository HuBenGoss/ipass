package persistence;

import model.Ingredient;
import model.Recipe;
import model.RecipeIngredient;

import java.util.ArrayList;

public interface RecipeIngredientDao {
    ArrayList<RecipeIngredient> findAll();
    ArrayList<RecipeIngredient> findByRecipeId(int id);
    RecipeIngredient findById(int id);
}
