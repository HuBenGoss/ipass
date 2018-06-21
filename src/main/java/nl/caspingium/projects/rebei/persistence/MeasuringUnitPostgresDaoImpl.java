package nl.caspingium.projects.rebei.persistence;

import nl.caspingium.projects.rebei.model.MeasuringUnit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MeasuringUnitPostgresDaoImpl extends PostgresBaseDao{
    public static ArrayList<MeasuringUnit> measuringUnits = new ArrayList<>();

    public void setMeasuringUnits(){
        String query = "SELECT * FROM \"Eenheid\"";
        try (Connection connection = super.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet dbResultSet = stmt.executeQuery(query);
            measuringUnits.clear();

            while (dbResultSet.next()) {
                String name = dbResultSet.getString("naam");
                int id = dbResultSet.getInt("id");
                measuringUnits.add(new MeasuringUnit(id,name));
            }

            connection.close();


        }
        catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }
}
