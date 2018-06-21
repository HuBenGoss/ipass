package persistence;

import model.Ingredient;
import model.Recipe;

import java.util.ArrayList;

public interface IngredientDao {
    ArrayList<Ingredient> findAll();
    Ingredient findById(int id);
    Ingredient findByName(String name);
}
