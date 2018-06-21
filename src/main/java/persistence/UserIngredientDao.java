package persistence;

import model.RecipeIngredient;
import model.UserIngredient;

import java.util.ArrayList;

public interface UserIngredientDao {
    ArrayList<UserIngredient> findAll();
    ArrayList<UserIngredient> findByUserId(int id);
    UserIngredient findById(int id);
}
