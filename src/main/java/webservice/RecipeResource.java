package webservice;


import model.Ingredient;
import model.Recipe;
import model.RecipeIngredient;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/recipes")
public class RecipeResource {
    private RecipeService service = ServiceProvider.getRecipeService();

    private JsonObjectBuilder jobRecipe(Recipe recipe)
    {
        JsonObjectBuilder job = Json.createObjectBuilder();
        try {
            job.add("title",recipe.getTitle());
            job.add("cookingTime",recipe.getCookingTime());
            job.add("getDescription",recipe.getDescription());
            job.add("getDescription",recipe.getDescription());

            for(RecipeIngredient recipeIngredient: recipe.getIngredients())
            {
                job.add("ingredients",jobIngredient(recipeIngredient));
            }
            return job;
        }catch (NullPointerException ex){
            ex.printStackTrace();
            return job;
        }
    }

    private JsonObjectBuilder jobIngredient(RecipeIngredient ingredient)
    {
        JsonObjectBuilder job = Json.createObjectBuilder();
        try {
            job.add("name",ingredient.getIngredient().getName());
            job.add("quantity",ingredient.getQuantity());
            job.add("measuringUnit",ingredient.getIngredient().getMeasuringUnit().getName());
            return job;
        }catch (NullPointerException ex){
            ex.printStackTrace();
            return job;
        }
    }

    @GET
    @Produces("application/json")
    public String getRecipes(){
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for (Recipe recipe : service.getAllRecipes())
        {
            jab.add(jobRecipe(recipe));
        }
        JsonArray array = jab.build();
        return array.toString();
    }
}
