package nl.caspingium.projects.rebei.persistence;

import nl.caspingium.projects.rebei.model.Recipe;
import nl.caspingium.projects.rebei.model.UserIngredient;

import java.util.ArrayList;

public interface RecipeDao {
    ArrayList<Recipe> findAll(boolean showIngredients);
    ArrayList<Recipe> findbyIngredients(ArrayList<UserIngredient> ingredients, boolean showIngredients);
    Recipe findById(int id,boolean showIngredients);
}
