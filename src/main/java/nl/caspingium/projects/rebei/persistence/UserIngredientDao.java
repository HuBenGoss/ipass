package nl.caspingium.projects.rebei.persistence;

import nl.caspingium.projects.rebei.model.UserIngredient;

import java.util.ArrayList;

public interface UserIngredientDao {
    ArrayList<UserIngredient> findAll();
    ArrayList<UserIngredient> findByUserId(int id);
    UserIngredient findById(int id);
}
