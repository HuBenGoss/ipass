package nl.caspingium.projects.rebei.webservice;


import nl.caspingium.projects.rebei.model.Recipe;
import nl.caspingium.projects.rebei.model.UserIngredient;
import nl.caspingium.projects.rebei.persistence.RecipeDao;
import nl.caspingium.projects.rebei.persistence.RecipePostgresDaoImpl;

import java.util.ArrayList;

public class RecipeService {

    protected ArrayList<Recipe> allRecipes = null;
    RecipeDao dao = new RecipePostgresDaoImpl();

    public RecipeService(){
        allRecipes = dao.findAll(false);
    }

    public ArrayList<Recipe> getAllRecipes() {
        return allRecipes;
    }

    public Recipe getRecipeById(int id) {
        return dao.findById(id,true);
    }

    public ArrayList<Recipe> getRecipeByIngredients(ArrayList<UserIngredient> ingredients) {
        return dao.findbyIngredients(ingredients,false);
    }
}
