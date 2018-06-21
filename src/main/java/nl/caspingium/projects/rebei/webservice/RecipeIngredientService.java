package nl.caspingium.projects.rebei.webservice;


import nl.caspingium.projects.rebei.model.RecipeIngredient;
import nl.caspingium.projects.rebei.persistence.RecipeIngredientDao;
import nl.caspingium.projects.rebei.persistence.RecipeIngredientPostgresDaoImpl;

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
