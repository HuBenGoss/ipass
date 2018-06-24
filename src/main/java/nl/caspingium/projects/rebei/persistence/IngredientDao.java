package nl.caspingium.projects.rebei.persistence;

import nl.caspingium.projects.rebei.model.Ingredient;

import java.util.ArrayList;

public interface IngredientDao {
    ArrayList<Ingredient> findAll();
    Ingredient findById(int id);
    Ingredient findByName(String name);
    boolean save(Ingredient ingredient);
    boolean update(Ingredient ingredient);
    boolean delete(Ingredient ingredient);
}
