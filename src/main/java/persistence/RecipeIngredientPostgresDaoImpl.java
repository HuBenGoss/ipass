package persistence;

import model.Ingredient;
import model.RecipeIngredient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientPostgresDaoImpl extends PostgresBaseDao{


    private ArrayList<RecipeIngredient> selectIngredients(String query){

        ArrayList<RecipeIngredient> ingredients = new ArrayList<RecipeIngredient>();

        try (Connection connection = super.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet dbResultSet = stmt.executeQuery(query);

            while (dbResultSet.next()) {
                String name = dbResultSet.getString("naam");
                int id = dbResultSet.getInt("id");
                double quantity = dbResultSet.getDouble("hoeveelheid");

                IngredientPostgresDaoImpl ingredientPostgresDao= new IngredientPostgresDaoImpl();
                Ingredient ingredient = ingredientPostgresDao.findById(id);

                RecipeIngredient recipeIngredient = new RecipeIngredient(id,ingredient,quantity);

                ingredients.add(recipeIngredient);
            }

        }
        catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return ingredients;
    }

    public ArrayList<RecipeIngredient> findAll() {

        return selectIngredients("SELECT * FROM ingrediënt");
    }

    public ArrayList<RecipeIngredient> findByRecipeId(int id) {
        return selectIngredients(String.format("SELECT * FROM WHERE receptid = (%d);",id));
    }

}
