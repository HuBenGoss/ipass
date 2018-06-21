package persistence;

import model.Ingredient;
import model.RecipeIngredient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientPostgresDaoImpl extends PostgresBaseDao implements RecipeIngredientDao{


    private ArrayList<RecipeIngredient> selectIngredients(String query){

        ArrayList<RecipeIngredient> ingredients = new ArrayList<RecipeIngredient>();

        try (Connection connection = super.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet dbResultSet = stmt.executeQuery(query);

            while (dbResultSet.next()) {
                int id = dbResultSet.getInt("id");
                double quantity = dbResultSet.getDouble("hoeveelheid");
                int ingredientId = dbResultSet.getInt("ingrediëntid");

                IngredientPostgresDaoImpl ingredientPostgresDao = new IngredientPostgresDaoImpl();
                Ingredient ingredient = ingredientPostgresDao.findById(ingredientId);

                RecipeIngredient recipeIngredient = new RecipeIngredient(id,ingredient,quantity);

                ingredients.add(recipeIngredient);
            }

            connection.close();
        }
        catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return ingredients;
    }

    public ArrayList<RecipeIngredient> findAll() {

        return selectIngredients("SELECT * FROM \"recept-ingrediënt\"");
    }

    public ArrayList<RecipeIngredient> findByRecipeId(int id) {
        return selectIngredients(String.format("SELECT \"recept-ingrediënt\".id,ingrediënt.id AS ingrediëntid,\"recept-ingrediënt\".hoeveelheid FROM \"recept-ingrediënt\" INNER JOIN ingrediënt ON \"recept-ingrediënt\".ingrediëntid=ingrediënt.id WHERE receptid = %d;",id));
    }

    public RecipeIngredient findById(int id) {
        return selectIngredients(String.format("SELECT \"recept-ingrediënt\".id,ingrediënt.id AS ingrediëntid,\"recept-ingrediënt\".hoeveelheid FROM \"recept-ingrediënt\" INNER JOIN ingrediënt ON \"recept-ingrediënt\".ingrediëntid=ingrediënt.id WHERE id = %d;",id)).get(0);
    }

}
