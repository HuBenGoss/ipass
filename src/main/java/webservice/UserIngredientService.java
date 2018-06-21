package webservice;


import model.RecipeIngredient;
import model.UserIngredient;
import persistence.RecipeIngredientDao;
import persistence.RecipeIngredientPostgresDaoImpl;
import persistence.UserIngredientDao;
import persistence.UserIngredientPostgresDaoImpl;

import java.util.ArrayList;

public class UserIngredientService {

    protected ArrayList<UserIngredient> allIngredients = null;
    UserIngredientDao dao = new UserIngredientPostgresDaoImpl();

    public UserIngredientService(){
        allIngredients = dao.findAll();
    }

    public ArrayList<UserIngredient> getAllIngredients() {
        return allIngredients;
    }

    public UserIngredient getIngredientById(int id) {
        return dao.findById(id);
    }
}
