package nl.caspingium.projects.rebei.persistence;

import nl.caspingium.projects.rebei.model.Ingredient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
//                int measuringUnitId = dbResultSet.getInt("eenheidid");

//                MeasuringUnit measuringUnit = null;
//                for (MeasuringUnit measuringUnitLoop :MeasuringUnitPostgresDaoImpl.measuringUnits) {
//                    if (measuringUnitLoop.getId() == measuringUnitId) {
//                        measuringUnit = measuringUnitLoop;
//                        break;
//                    }
//                }

                ingredients.add(new Ingredient(id,name,/*measuringUnit*/null));
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
        return selectIngredients(String.format("SELECT * FROM ingrediënt WHERE id=%d",id)).get(0);
    }

    public Ingredient findByName(String name) {
        return selectIngredients("SELECT * FROM ingrediënt WHERE naam LIKE '%"+name+"%'").get(0);
    }

}
