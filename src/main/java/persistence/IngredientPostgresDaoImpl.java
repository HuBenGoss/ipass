package persistence;

import model.Ingredient;
import model.MeasuringUnit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IngredientPostgresDaoImpl extends PostgresBaseDao {

    private ArrayList<Ingredient> selectIngredients(String query){

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        try (Connection connection = super.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet dbResultSet = stmt.executeQuery(query);

            while (dbResultSet.next()) {
                String name = dbResultSet.getString("naam");
                int id = dbResultSet.getInt("id");
                int measuringUnitId = dbResultSet.getInt("eenheid");

                MeasuringUnit measuringUnit = null;
                for (MeasuringUnit measuringUnitLoop :MeasuringUnitPostgresDaoImpl.measuringUnits) {
                    if (measuringUnitLoop.getId() == measuringUnitId) {
                        measuringUnit = measuringUnitLoop;
                        break;
                    }
                }

                ingredients.add(new Ingredient(id,name,measuringUnit));
            }

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
        return selectIngredients(String.format("SELECT * FROM ingrediënt WHERE id=(%d)",id)).get(0);
    }

}
