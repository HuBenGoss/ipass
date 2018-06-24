package nl.caspingium.projects.rebei.webservice;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.glassfish.jersey.media.multipart.FormDataParam;
import nl.caspingium.projects.rebei.model.Recipe;
import nl.caspingium.projects.rebei.model.RecipeIngredient;
import nl.caspingium.projects.rebei.model.Step;
import nl.caspingium.projects.rebei.model.UserIngredient;


import javax.annotation.security.RolesAllowed;
import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

            if(showIngredients) {
                JsonObject ingredientObject = new JsonObject();
                int counter = 0;
                for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
                    try {

                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(counter);
                        ingredientObject.add(sb.toString(), jobIngredient(recipeIngredient));
                        counter++;
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
                recipeObject.add("ingredients", ingredientObject);

                JsonObject stepObject = new JsonObject();
                counter = 0;
                for (Step step : recipe.getSteps()) {
                    try {

                            StringBuilder sb = new StringBuilder();
                            sb.append("");
                            sb.append(counter++);
                            stepObject.add(sb.toString(), jobStep(step));
                    }catch (NullPointerException ex){
                        ex.printStackTrace();
                    }
                }
                recipeObject.add("steps", stepObject);
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
            ingredientObject.addProperty("measuringUnit",ingredient.getIngredient().getMeasuringUnit());

            return ingredientObject;

        }catch (NullPointerException ex){
            ex.printStackTrace();

            return ingredientObject;
        }
    }

    private JsonObject jobStep(Step step)
    {
        JsonObject stepObject = new JsonObject();

        try {
            stepObject.addProperty("stepnr",step.getStepnr());
            stepObject.addProperty("description",step.getDescription());

            return stepObject;

        }catch (NullPointerException ex){
            ex.printStackTrace();

            return stepObject;
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

        JsonArray recipeArray = new JsonArray();

        if(searchIngredients ==null) {
            ArrayList<UserIngredient> ingredients = new ArrayList<>();

            JsonArray searchIngredient = new Gson().fromJson(searchIngredients, JsonArray.class);
            for (Object item : searchIngredient) {
                if (item instanceof JsonObject) {
                    JsonObject jsonObject = (JsonObject) item;
                    ingredients.add(new UserIngredient(0, ingredientService.getIngredientById(jsonObject.get("ingredient").getAsInt()), jsonObject.get("quantity").getAsDouble(), new Date()));

                }
            }
//
//        for (String key : queryParams.keySet()) {
//            ingredients.add(new UserIngredient(0,ingredientService.getIngredientById(Integer.parseInt(key.replaceAll("\\D",""))),Double.parseDouble(queryParams.get(key).toString().replaceAll("\\D","")),new Date()));
//        }


            for (Recipe recipe : recipeService.getRecipeByIngredients(ingredients)) {
                recipeArray.add(jobRecipe(recipe, false));
            }
        }
        for(Recipe recipe : recipeService.getAllRecipes()) {
            recipeArray.add(jobRecipe(recipe, false));
        }

        return recipeArray.toString();
    }

    @GET
    @Path("recipe/{id}")
    @Produces("application/json")
    public String getRecipeById(@PathParam("id") int id){

        JsonArray  recipeArray = new JsonArray();

        Recipe recipe = recipeService.getRecipeById(id);

        recipeArray.add(jobRecipe(recipe,true));

        return recipeArray.toString();
    }

    @GET
    @Path("recipe/{id}/image")
    @Produces("image/png")
    public Response getRecipeImage(@PathParam("id") int id){


        try {
            InputStream is = new ByteArrayInputStream(recipeService.getRecipeById(id).getImageData());
            BufferedImage image = ImageIO.read(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageData = baos.toByteArray();
            return Response.ok(new ByteArrayInputStream(imageData)).build();
        }catch (java.io.IOException ex){
            ex.printStackTrace();
            return Response.status(500).build();
        }



    }

    @POST
    @RolesAllowed({"1"})
    @Path("add")
    @Produces("application/json")
    public String saveRecipe(@FormParam("title") String title,
                                @FormParam("cookingTime") int cookingTime,
                                @FormParam("description") String description,
//                                @FormDataParam("file") InputStream uploadedInputStream,
                                @FormParam("steps") String steps,
                                @FormParam("ingredients") String ingredients) {

        JsonArray  recipeArray = new JsonArray();
        ArrayList<Step> steps1 = new ArrayList<>();

        JsonArray stepJson = new Gson().fromJson(steps, JsonArray.class);
        for (Object item : stepJson) {
            if (item instanceof JsonObject) {
                JsonObject jsonObject = (JsonObject) item;
                System.out.print(jsonObject);
                System.out.println();


                steps1.add(new Step(0,jsonObject.get("step").getAsInt(),jsonObject.get("description").getAsString()));

            }
        }

        ArrayList<RecipeIngredient> ingredients1 = new ArrayList<>();

        JsonArray searchIngredient = new Gson().fromJson(ingredients, JsonArray.class);
        for (Object item : searchIngredient) {
            if (item instanceof JsonObject) {
                JsonObject jsonObject = (JsonObject) item;
                System.out.print(jsonObject);
                System.out.println();

                ingredients1.add(new UserIngredient(0, ingredientService.getIngredientById(jsonObject.get("ingredient").getAsInt()), jsonObject.get("quantity").getAsDouble(), new Date()));
            }
        }

        Recipe recipe = new Recipe(0,title,cookingTime,null,null);
        recipe.setSteps(steps1);
        recipe.setIngredients(ingredients1);
        recipeArray.add(jobRecipe(recipe,true));

        if(recipeService.save(recipe))
            return "success";
        else
            return recipeArray.toString();


    }


}
