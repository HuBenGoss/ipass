package webservice;


import model.Ingredient;

import persistence.IngredientDao;
import persistence.IngredientPostgresDaoImpl;


import java.util.ArrayList;

public class IngredientService {

    protected ArrayList<Ingredient> allIngredients = null;
    IngredientDao dao = new IngredientPostgresDaoImpl();

    public IngredientService(){
        allIngredients = dao.findAll();
    }

    public ArrayList<Ingredient> getAllIngredients() {
        return allIngredients;
    }

    public Ingredient getIngredientById(int id) {
        return dao.findById(id);
    }

    public Ingredient getIngredientByName(String name) {
        return dao.findByName(name);
    }
}
