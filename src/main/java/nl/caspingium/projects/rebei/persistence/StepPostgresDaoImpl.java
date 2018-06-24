package nl.caspingium.projects.rebei.persistence;

import nl.caspingium.projects.rebei.model.Step;

import java.sql.*;
import java.util.ArrayList;

public class StepPostgresDaoImpl extends PostgresBaseDao implements StepDao{
    private ArrayList<Step> selectSteps(String query){

        ArrayList<Step> steps = new ArrayList<Step>();

        try (Connection connection = super.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet dbResultSet = stmt.executeQuery(query);

            while (dbResultSet.next()) {
                String description = dbResultSet.getString("omschrijving");
                int id = dbResultSet.getInt("id");
                int stepnr = dbResultSet.getInt("stap");

                steps.add(new Step(id,stepnr,description));
            }

            connection.close();


        }
        catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return steps;
    }

    public ArrayList<Step> findByRecipe(int id) {
        return selectSteps(String.format("SELECT * FROM stap WHERE receptid=%d ORDER BY stap ASC",id));
    }

    public Step findById(int id) {
        if(selectSteps(String.format("SELECT * FROM stap WHERE id=%d",id)).size() > 0)
            return selectSteps(String.format("SELECT * FROM stap WHERE id=%d", id)).get(0);
        else
            return null;
    }

    public boolean delete(int recipeId) {

        boolean result = true;
        String query = "DELETE FROM \"stap\" WHERE receptid=%d";

        try(Connection connection = getConnection()) {
            Statement stmt = connection.createStatement();
            result = stmt.executeUpdate(String.format(query,recipeId)) == 1;
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    public boolean delete(Step step) {
        boolean result = true;
        boolean ingredientExists = findById(step.getId()) != null;

        if(ingredientExists) {
            String query = "DELETE FROM \"stap\" WHERE id=%d";

            try(Connection connection = getConnection()) {
                Statement stmt = connection.createStatement();
                result = stmt.executeUpdate(String.format(query,step.getId())) == 1;
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


    public boolean save(Step step,int recipeId) {
        PreparedStatement preparedStatement = null;
        if(findById(step.getId()) == null) {
            try {

                String select = "INSERT INTO \"stap\" (stap,omschrijving,receptid)  VALUES (?,?,?)";
                preparedStatement = super.getConnection().prepareStatement(select);


                preparedStatement.setInt(1, step.getStepnr());
                preparedStatement.setString(2, step.getDescription());
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

    public boolean update(Step step) {
        PreparedStatement preparedStatement = null;

        try {

            String select = "UPDATE \"stap\" SET stap = ?, omschrijving = ? WHERE id = ?";


            preparedStatement = super.getConnection().prepareStatement(select);

            preparedStatement.setInt(1, step.getStepnr());
            preparedStatement.setString(2, step.getDescription());
            preparedStatement.setInt(3,step.getId());

            preparedStatement.executeUpdate();
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
