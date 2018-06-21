package persistence;

import model.Ingredient;
import model.RecipeIngredient;
import model.UserIngredient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class UserIngredientPostgresDaoImpl extends PostgresBaseDao implements UserIngredientDao{


    private ArrayList<UserIngredient> selectIngredients(String query){

        ArrayList<UserIngredient> ingredients = new ArrayList<>();

        try (Connection connection = super.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet dbResultSet = stmt.executeQuery(query);

            while (dbResultSet.next()) {
                int id = dbResultSet.getInt("id");
                double quantity = dbResultSet.getDouble("hoeveelheid");
                int ingredientId = dbResultSet.getInt("ingrediëntid");

                IngredientPostgresDaoImpl ingredientPostgresDao = new IngredientPostgresDaoImpl();
                Ingredient ingredient = ingredientPostgresDao.findById(ingredientId);

                UserIngredient userIngredient = new UserIngredient(id,ingredient,quantity,new Date());

                ingredients.add(userIngredient);
            }

            connection.close();
        }
        catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return ingredients;
    }

    public ArrayList<UserIngredient> findAll() {

        return selectIngredients("SELECT * FROM \"recept-ingrediënt\"");
    }

    public ArrayList<UserIngredient> findByUserId(int id) {
        return selectIngredients(String.format("SELECT \"recept-ingrediënt\".id,ingrediënt.id AS ingrediëntid,\"recept-ingrediënt\".hoeveelheid FROM \"recept-ingrediënt\" INNER JOIN ingrediënt ON \"recept-ingrediënt\".ingrediëntid=ingrediënt.id WHERE userid = %d;",id));
    }

    public UserIngredient findById(int id) {
        return selectIngredients(String.format("SELECT \"recept-ingrediënt\".id,ingrediënt.id AS ingrediëntid,\"recept-ingrediënt\".hoeveelheid FROM \"recept-ingrediënt\" INNER JOIN ingrediënt ON \"recept-ingrediënt\".ingrediëntid=ingrediënt.id WHERE id = %d;",id)).get(0);
    }

}
