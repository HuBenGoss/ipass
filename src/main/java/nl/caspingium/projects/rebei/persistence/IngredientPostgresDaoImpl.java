package nl.caspingium.projects.rebei.persistence;

import nl.caspingium.projects.rebei.model.Ingredient;

import java.sql.*;
import java.util.ArrayList;

public class IngredientPostgresDaoImpl extends PostgresBaseDao implements IngredientDao{

    private ArrayList<Ingredient> selectIngredients(String query){

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        try (Connection connection = super.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet dbResultSet = stmt.executeQuery(query);

            while (dbResultSet.next()) {
                String name = dbResultSet.getString("naam");
                int id = dbResultSet.getInt("id");
                String  measuringUnit = dbResultSet.getString("eenheid");

                ingredients.add(new Ingredient(id,name,measuringUnit));
            }

            connection.close();


        }
        catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return ingredients;
    }

    public ArrayList<Ingredient> findAll() {

        return selectIngredients("SELECT * FROM ingrediënt");
    }

    public Ingredient findById(int id) {
        if(selectIngredients(String.format("SELECT * FROM ingrediënt WHERE id=%d",id)).size() > 0)
            return selectIngredients(String.format("SELECT * FROM ingrediënt WHERE id=%d",id)).get(0);
        else
            return null;
    }

    public Ingredient findByName(String name) {
        return selectIngredients("SELECT * FROM ingrediënt WHERE naam LIKE '%"+name+"%'").get(0);
    }

    public boolean delete(Ingredient ingredient) {
        boolean result = true;
        boolean ingredientExists = findById(ingredient.getId()) != null;

        if(ingredientExists) {
            String query = "DELETE FROM ingrediënt WHERE id=%d";

            try(Connection connection = getConnection()) {
                Statement stmt = connection.createStatement();
                result = stmt.executeUpdate(String.format(query,ingredient.getId())) == 1;
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    public boolean save(Ingredient ingredient) {
        PreparedStatement preparedStatement = null;
        if(findById(ingredient.getId()) == null) {
            try {

                String select = "INSERT INTO ingrediënt (naam,eenheidid)  VALUES (?,?)";
                preparedStatement = super.getConnection().prepareStatement(select);


                preparedStatement.setString(1, ingredient.getName());
                preparedStatement.setString(2, ingredient.getMeasuringUnit());

                preparedStatement.executeUpdate();



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

    public boolean update(Ingredient ingredient) {
        PreparedStatement preparedStatement = null;

        try {

            String select = "UPDATE ingrediënt SET naam = ?, eenheidid = ? WHERE id = ?";


            preparedStatement = super.getConnection().prepareStatement(select);

            preparedStatement.setString(1, ingredient.getName());
            preparedStatement.setString(2, ingredient.getMeasuringUnit());
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
