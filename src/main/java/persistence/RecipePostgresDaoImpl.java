package persistence;

import model.Ingredient;
import model.Recipe;

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
                int userId = dbResultSet.getInt("gebruikersId");
                String description = dbResultSet.getString("beschrijving");



                Recipe recipe = new Recipe(id,title,cookingTime,null,description);
                results.add(recipe);
            }

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

    public ArrayList<Recipe> findbyIngredients(ArrayList<Ingredient> ingredients,boolean showIngredients) {
        ArrayList<Recipe> recipes = selectRecipes("SELECT * FROM recept WHERE ");
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
        Recipe recipe = selectRecipes(String.format("SELECT * FROM recept WHERE id = (%d)",id)).get(0);
        if(showIngredients) {
            RecipeIngredientPostgresDaoImpl recipeIngredientPostgresDao = new RecipeIngredientPostgresDaoImpl();
            recipe.setIngredients(recipeIngredientPostgresDao.findByRecipeId(recipe.getId()));
        }

        return recipe;
    }

}
