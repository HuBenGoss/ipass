package nl.caspingium.projects.rebei.persistence;

import nl.caspingium.projects.rebei.model.*;
import nl.caspingium.projects.rebei.webservice.ServiceProvider;
import nl.caspingium.projects.rebei.webservice.StepService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipePostgresDaoImpl extends PostgresBaseDao implements RecipeDao{

    private StepService service = ServiceProvider.getStepService();

    private ArrayList<Recipe> selectRecipes(String query){

        ArrayList<Recipe> results = new ArrayList<Recipe>();
        try (Connection connection = super.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet dbResultSet = stmt.executeQuery(query);

            while (dbResultSet.next()) {
                String title = dbResultSet.getString("titel");
                int id = dbResultSet.getInt("id");
                int cookingTime = dbResultSet.getInt("bereidingstijd");
                int userId = dbResultSet.getInt("gebruikerid");
                byte[] imageData = dbResultSet.getBytes("afbeelding");


                Recipe recipe = new Recipe(id,title,cookingTime,null,imageData);

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
                recipe.setSteps(service.getStepByRecipe(recipe.getId()));

                RecipeIngredientPostgresDaoImpl recipeIngredientPostgresDao= new RecipeIngredientPostgresDaoImpl();
                recipe.setIngredients(recipeIngredientPostgresDao.findByRecipeId(recipe.getId()));
            }
        }

        return recipes;
    }

    private String generateAndQuery(List<UserIngredient> ingredients,boolean idFieldOnly) {
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

        if(idFieldOnly)
            query = "SELECT id FROM recept " + query;
        else
            query = "SELECT * FROM recept " + query;
        System.out.println(query);

        return query;
    }

    private String generateOrQuery(List<UserIngredient> ingredients) {
        String query = "";
        int count = 0;
        for(UserIngredient ingredient: ingredients) {
            String newQuery;
            if(count > 0)
                newQuery = " OR ";
            else
                newQuery = " WHERE ";

            newQuery += "id in(SELECT \"recept-ingrediënt\".receptid FROM \"recept-ingrediënt\" WHERE hoeveelheid <= "+ingredient.getQuantity()+" AND \"recept-ingrediënt\".ingrediëntid = "+ingredient.getIngredient().getId()+")";

            query += newQuery;

            count++;
        }
        String notvalues = "";


        query = "SELECT * FROM recept " + query + " AND recept.id NOT IN("+generateAndQuery(ingredients,true)+")";
        System.out.println(query);

        return query;
    }

    public ArrayList<Recipe> findbyIngredients(ArrayList<UserIngredient> ingredients, boolean showIngredients) {



        String andQuery = generateAndQuery(ingredients,false);
        ArrayList<Recipe> recipes = selectRecipes(andQuery);

        if(ingredients.size() > 0) {

            String orQuery = generateOrQuery(ingredients);
            ArrayList<Recipe> orList =  selectRecipes(orQuery);
            for (Recipe recipe : orList) {
                recipes.add(recipe);
            }
        }

        if(showIngredients)
        {
            for(Recipe recipe: recipes)
            {
                RecipeIngredientPostgresDaoImpl recipeIngredientPostgresDao= new RecipeIngredientPostgresDaoImpl();
                recipe.setIngredients(recipeIngredientPostgresDao.findByRecipeId(recipe.getId()));
                recipe.setSteps(service.getStepByRecipe(recipe.getId()));
            }
        }
        return recipes;
    }

    public Recipe findById(int id,boolean showIngredients) {
        if(selectRecipes(String.format("SELECT * FROM recept WHERE id = %d",id)).size() > 0) {
            Recipe recipe = selectRecipes(String.format("SELECT * FROM recept WHERE id = %d", id)).get(0);
            if (showIngredients) {
                recipe.setSteps(service.getStepByRecipe(recipe.getId()));
                RecipeIngredientPostgresDaoImpl recipeIngredientPostgresDao = new RecipeIngredientPostgresDaoImpl();
                recipe.setIngredients(recipeIngredientPostgresDao.findByRecipeId(recipe.getId()));
            }

            return recipe;
        }else{
            return null;
        }
    }

    public boolean delete(Recipe recipe) {
        boolean result = true;
        boolean ingredientExists = findById(recipe.getId(),false) != null;

        if(ingredientExists) {
            String query = "DELETE FROM recept WHERE id=%d";

            RecipeIngredientPostgresDaoImpl recipeIngredientPostgresDao = new RecipeIngredientPostgresDaoImpl();
            StepPostgresDaoImpl stepPostgresDao = new StepPostgresDaoImpl();
            try(Connection connection = getConnection()) {
                Statement stmt = connection.createStatement();
                result = stmt.executeUpdate(String.format(query,recipe.getId())) == 1;
                result = recipeIngredientPostgresDao.delete(recipe.getId());
                result = stepPostgresDao.delete(recipe.getId());
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    public boolean save(Recipe recipe) {
        PreparedStatement preparedStatement = null;
        if(findById(recipe.getId(),false) == null) {
            try {

                String select = "INSERT INTO recept (titel,bereidingstijd,afbeelding)  VALUES (?,?,?,?) RETURNING id;";
                preparedStatement = super.getConnection().prepareStatement(select);


                preparedStatement.setString(1, recipe.getTitle());
                preparedStatement.setDouble(2, recipe.getCookingTime());
                preparedStatement.setBytes(3, recipe.getImageData());

                ResultSet set = preparedStatement.executeQuery();
                set.next();
                recipe.setId(set.getInt(1));

                RecipeIngredientPostgresDaoImpl recipeIngredientPostgresDao = new RecipeIngredientPostgresDaoImpl();
                StepPostgresDaoImpl stepPostgresDao = new StepPostgresDaoImpl();

                boolean result = true;
                for(RecipeIngredient ingredient:recipe.getIngredients()){
                    result = recipeIngredientPostgresDao.save(ingredient,recipe.getId());
                }
                for(Step step:recipe.getSteps()){
                    result = stepPostgresDao.save(step,recipe.getId());
                }
                return result;


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

    public boolean update(Recipe recipe) {
        PreparedStatement preparedStatement = null;

        try {

            String select = "UPDATE recept SET titel = ?,bereidingstijd = ?,afbeelding = ? WHERE id = ?";


            preparedStatement = super.getConnection().prepareStatement(select);

            preparedStatement.setString(1, recipe.getTitle());
            preparedStatement.setInt(2,recipe.getCookingTime());
            preparedStatement.setBytes(3, recipe.getImageData());

            preparedStatement.setInt(4,recipe.getId());

            preparedStatement.executeUpdate();

            // we don't have to remove from our list as it's already set to new name in main.java
            RecipeIngredientPostgresDaoImpl recipeIngredientPostgresDao = new RecipeIngredientPostgresDaoImpl();
            StepPostgresDaoImpl stepPostgresDao = new StepPostgresDaoImpl();

            boolean result = true;
            for(RecipeIngredient ingredient:recipe.getIngredients()){
                result = recipeIngredientPostgresDao.update(ingredient);
            }
            for(Step step:recipe.getSteps()){
                result = stepPostgresDao.update(step);
            }
            return result;

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
