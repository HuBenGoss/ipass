package nl.caspingium.projects.rebei.persistence;

import nl.caspingium.projects.rebei.model.RecipeIngredient;

import java.util.ArrayList;

public interface RecipeIngredientDao {
    ArrayList<RecipeIngredient> findAll();
    ArrayList<RecipeIngredient> findByRecipeId(int id);
    RecipeIngredient findById(int id);
}
