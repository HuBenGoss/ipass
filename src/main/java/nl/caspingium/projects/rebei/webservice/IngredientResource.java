package nl.caspingium.projects.rebei.webservice;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nl.caspingium.projects.rebei.model.Ingredient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/ingredients")
public class IngredientResource {
    private IngredientService ingredientService = ServiceProvider.getIngredientService();

    private JsonObject job(Ingredient ingredient)
    {
        JsonObject ingredientObject = new JsonObject();

        try {
            ingredientObject.addProperty("name",ingredient.getName());
            ingredientObject.addProperty("id",ingredient.getId());
//            ingredientObject.addProperty("measuringUnit",ingredient.getMeasuringUnit().getName());

            return ingredientObject;

        }catch (NullPointerException ex){
            ex.printStackTrace();

            return ingredientObject;
        }
    }

    @GET
    @Produces("application/json")
    public String getRecipes(){
        JsonArray recipeArray = new JsonArray();

        for (Ingredient ingredient : ingredientService.getAllIngredients())
        {
            recipeArray.add(job(ingredient));
        }
        return recipeArray.toString();
    }
}
