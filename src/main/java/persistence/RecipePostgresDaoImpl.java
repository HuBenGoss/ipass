package persistence;

import model.Ingredient;
import model.Recipe;
import model.UserIngredient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RecipePostgresDaoImpl extends PostgresBaseDao implements RecipeDao{


    private ArrayList<Recipe> selectRecipes(String query){

        ArrayList<Recipe> results = new ArrayList<Recipe>();
        try (Connection connection = super.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet dbResultSet = stmt.executeQuery(query);

            while (dbResultSet.next()) {
                String title = dbResultSet.getString("titel");
                int id = dbResultSet.getInt("id");
                double cookingTime = dbResultSet.getDouble("bereidingstijd");
                int userId = dbResultSet.getInt("gebruikerid");
                String description = dbResultSet.getString("beschrijving");
                if(description == null)
                    description = "No description";



                Recipe recipe = new Recipe(id,title,cookingTime,null,description);
                results.add(recipe);
            }
            connection.close();


        }
        catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return results;
    }

    public ArrayList<Recipe> findAll(boolean showIngredients) {
        ArrayList<Recipe> recipes = selectRecipes("SELECT * FROM recept");

        if(showIngredients)
        {
            for(Recipe recipe: recipes)
            {
                RecipeIngredientPostgresDaoImpl recipeIngredientPostgresDao= new RecipeIngredientPostgresDaoImpl();
                recipe.setIngredients(recipeIngredientPostgresDao.findByRecipeId(recipe.getId()));
            }
        }

        return recipes;
    }

    public ArrayList<Recipe> findbyIngredients(ArrayList<UserIngredient> ingredients, boolean showIngredients) {
        String query = "";
        int count = 0;
        for(UserIngredient ingredient: ingredients) {
            String newQuery;
            if(count > 0)
                newQuery = " AND ";
            else
                newQuery = " WHERE ";

            newQuery += "id in(SELECT \"recept-ingrediënt\".receptid FROM \"recept-ingrediënt\" WHERE hoeveelheid <= "+ingredient.getQuantity()+" AND \"recept-ingrediënt\".ingrediëntid = "+ingredient.getIngredient().getId()+")";

            query += newQuery;

            count++;
        }

        query = "SELECT * FROM recept " + query;

        System.out.println(query);


        ArrayList<Recipe> recipes = selectRecipes(query);
        if(showIngredients)
        {
            for(Recipe recipe: recipes)
            {
                RecipeIngredientPostgresDaoImpl recipeIngredientPostgresDao= new RecipeIngredientPostgresDaoImpl();
                recipe.setIngredients(recipeIngredientPostgresDao.findByRecipeId(recipe.getId()));
            }
        }
        return recipes;
    }

    public Recipe findById(int id,boolean showIngredients) {
        Recipe recipe = selectRecipes(String.format("SELECT * FROM recept WHERE id = %d",id)).get(0);
        if(showIngredients) {
            RecipeIngredientPostgresDaoImpl recipeIngredientPostgresDao = new RecipeIngredientPostgresDaoImpl();
            recipe.setIngredients(recipeIngredientPostgresDao.findByRecipeId(recipe.getId()));
        }

        return recipe;
    }

}
