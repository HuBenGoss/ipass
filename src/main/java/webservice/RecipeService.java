package webservice;


import model.Recipe;
import persistence.RecipeDao;
import persistence.RecipePostgresDaoImpl;

import java.util.ArrayList;

public class RecipeService {

    protected ArrayList<Recipe> allRecipes = null;
    RecipeDao dao = new RecipePostgresDaoImpl();

    public RecipeService(){
        allRecipes = dao.findAll(true);
    }

    public ArrayList<Recipe> getAllRecipes() {
        return allRecipes;
    }

    public Recipe getRecipeById(int id) {
        return dao.findById(id,true);
    }
}
