package webservice;


import model.Recipe;
import model.RecipeIngredient;
import persistence.RecipeDao;
import persistence.RecipeIngredientDao;
import persistence.RecipeIngredientPostgresDaoImpl;
import persistence.RecipePostgresDaoImpl;

import java.util.ArrayList;

public class RecipeIngredientService {

    protected ArrayList<RecipeIngredient> allIngredients = null;
    RecipeIngredientDao dao = new RecipeIngredientPostgresDaoImpl();

    public RecipeIngredientService(){
        allIngredients = dao.findAll();
    }

    public ArrayList<RecipeIngredient> getAllIngredients() {
        return allIngredients;
    }

    public RecipeIngredient getIngredientById(int id) {
        return dao.findById(id);
    }
}
