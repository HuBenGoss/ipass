package webservice;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import model.Recipe;
import model.RecipeIngredient;
import model.UserIngredient;

import javax.ws.rs.*;

import java.util.ArrayList;
import java.util.Date;

@Path("/recipes")
public class RecipeResource {
    private RecipeService recipeService = ServiceProvider.getRecipeService();
    private IngredientService ingredientService = ServiceProvider.getIngredientService();

    private JsonObject jobRecipe(Recipe recipe,boolean showIngredients)
    {
        JsonObject recipeObject = new JsonObject();
        try {
            recipeObject.addProperty("id",recipe.getId());
            recipeObject.addProperty("title",recipe.getTitle());
            recipeObject.addProperty("cookingTime",recipe.getCookingTime());
            recipeObject.addProperty("description",recipe.getDescription());

            if(showIngredients) {
                JsonObject ingredientObject = new JsonObject();
                for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(recipeIngredient.getId());
                    ingredientObject.add(sb.toString(), jobIngredient(recipeIngredient));
                }

                recipeObject.add("ingredients", ingredientObject);
            }
            return recipeObject;
        }catch (NullPointerException ex){
            ex.printStackTrace();
            return recipeObject;
        }
    }

    private JsonObject jobIngredient(RecipeIngredient ingredient)
    {
        JsonObject ingredientObject = new JsonObject();

        try {
            ingredientObject.addProperty("name",ingredient.getIngredient().getName());
            ingredientObject.addProperty("quantity",ingredient.getQuantity());
            ingredientObject.addProperty("measuringUnit",ingredient.getIngredient().getMeasuringUnit().getName());

            return ingredientObject;

        }catch (NullPointerException ex){
            ex.printStackTrace();

            return ingredientObject;
        }
    }

    @GET
    @Produces("application/json")
    public String getRecipes(){
        JsonArray  recipeArray = new JsonArray();

        for (Recipe recipe : recipeService.getAllRecipes())
        {
            recipeArray.add(jobRecipe(recipe,false));
        }
        return recipeArray.toString();
    }

    @GET
    @Path("recipe")
    @Produces("application/json")
    public String getRecipesByIngredients(@QueryParam("ingredients") String searchIngredients){


        ArrayList<UserIngredient> ingredients = new ArrayList<>();

        JsonArray searchIngredient = new Gson().fromJson(searchIngredients,JsonArray.class);
        for (Object item : searchIngredient)
        {
            if( item instanceof JsonObject) {
                JsonObject jsonObject = (JsonObject) item;
                ingredients.add(new UserIngredient(0,ingredientService.getIngredientById(jsonObject.get("ingredient").getAsInt()),jsonObject.get("quantity").getAsDouble(),new Date()));

            }
        }
//
//        for (String key : queryParams.keySet()) {
//            ingredients.add(new UserIngredient(0,ingredientService.getIngredientById(Integer.parseInt(key.replaceAll("\\D",""))),Double.parseDouble(queryParams.get(key).toString().replaceAll("\\D","")),new Date()));
//        }

        JsonArray  recipeArray = new JsonArray();

        for (Recipe recipe : recipeService.getRecipeByIngredients(ingredients))
        {
            recipeArray.add(jobRecipe(recipe,false));
        }

        return recipeArray.toString();
    }

    @GET
    @Path("recipe/{id}")
    @Produces("application/json")
    public String getRecipesById(@PathParam("id") int id){

        JsonArray  recipeArray = new JsonArray();

        Recipe recipe = recipeService.getRecipeById(id);

        recipeArray.add(jobRecipe(recipe,true));

        return recipeArray.toString();
    }


}
