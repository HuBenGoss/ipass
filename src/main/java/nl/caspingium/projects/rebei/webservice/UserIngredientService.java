package nl.caspingium.projects.rebei.webservice;


import nl.caspingium.projects.rebei.model.UserIngredient;
import nl.caspingium.projects.rebei.persistence.UserIngredientDao;
import nl.caspingium.projects.rebei.persistence.UserIngredientPostgresDaoImpl;

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
