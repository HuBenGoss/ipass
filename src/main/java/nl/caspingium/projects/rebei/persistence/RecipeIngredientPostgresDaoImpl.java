package nl.caspingium.projects.rebei.persistence;

import nl.caspingium.projects.rebei.model.Ingredient;
import nl.caspingium.projects.rebei.model.RecipeIngredient;

import java.sql.*;
import java.util.ArrayList;

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
        if(selectIngredients(String.format("SELECT \"recept-ingrediënt\".id,ingrediënt.id AS ingrediëntid,\"recept-ingrediënt\".hoeveelheid FROM \"recept-ingrediënt\" INNER JOIN ingrediënt ON \"recept-ingrediënt\".ingrediëntid=ingrediënt.id WHERE id = %d;",id)).size()>0)
            return selectIngredients(String.format("SELECT \"recept-ingrediënt\".id,ingrediënt.id AS ingrediëntid,\"recept-ingrediënt\".hoeveelheid FROM \"recept-ingrediënt\" INNER JOIN ingrediënt ON \"recept-ingrediënt\".ingrediëntid=ingrediënt.id WHERE id = %d;",id)).get(0);
        else
            return null;
    }

    public boolean delete(RecipeIngredient ingredient) {
        boolean result = true;
        boolean ingredientExists = findById(ingredient.getId()) != null;

        if(ingredientExists) {
            String query = "DELETE FROM \"recept-ingrediënt\" WHERE id=%d";

            try(Connection connection = getConnection()) {
                Statement stmt = connection.createStatement();
                result = stmt.executeUpdate(String.format(query,ingredient.getId())) == 1;
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    public boolean delete(int recipeId) {

        boolean result = true;
        String query = "DELETE FROM \"recept-ingrediënt\" WHERE receptid=%d";

        try(Connection connection = getConnection()) {
            Statement stmt = connection.createStatement();
            result = stmt.executeUpdate(String.format(query,recipeId)) == 1;
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean deleteSingle(int ingredientId) {

        boolean result = true;
        String query = "DELETE FROM \"recept-ingrediënt\" WHERE id=%d";

        try(Connection connection = getConnection()) {
            Statement stmt = connection.createStatement();
            result = stmt.executeUpdate(String.format(query,ingredientId)) == 1;
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean save(RecipeIngredient ingredient,int recipeId) {
        PreparedStatement preparedStatement = null;
        if(findById(ingredient.getId()) == null) {
            try {

                String select = "INSERT INTO \"recept-ingrediënt\" (hoeveelheid,ingrediëntid,receptid)  VALUES (?,?,?)";
                preparedStatement = super.getConnection().prepareStatement(select);


                preparedStatement.setDouble(1, ingredient.getQuantity());
                preparedStatement.setInt(2, ingredient.getIngredient().getId());
                preparedStatement.setInt(3, recipeId);

                preparedStatement.executeUpdate();

                return true;


            } catch (Exception e) {

                e.printStackTrace();
                return false;

            } finally {
                if (preparedStatement != null) {

                    try {
                        preparedStatement.close();

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }
        return true;
    }

    public boolean update(RecipeIngredient ingredient) {
        PreparedStatement preparedStatement = null;

        try {

            String select = "UPDATE \"recept-ingrediënt\" SET hoeveelheid = ?, ingrediëntid = ? WHERE id = ?";


            preparedStatement = super.getConnection().prepareStatement(select);

            preparedStatement.setDouble(1, ingredient.getQuantity());
            preparedStatement.setInt(2,ingredient.getIngredient().getId());
            preparedStatement.setInt(3,ingredient.getId());

            preparedStatement.executeUpdate();

            // we don't have to remove from our list as it's already set to new name in main.java
            return true;


        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }finally {
            if(preparedStatement != null) {

                try {
                    preparedStatement.close();

                }catch (SQLException ex){
                    ex.printStackTrace();
                }

            }
        }
    }

}
