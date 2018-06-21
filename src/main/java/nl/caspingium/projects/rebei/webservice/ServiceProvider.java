package nl.caspingium.projects.rebei.webservice;

public class ServiceProvider {

	public static IngredientService getIngredientService() {
		return new IngredientService();
	}
	public static RecipeService getRecipeService() {
		return new RecipeService();
	}
	public static RecipeIngredientService getRecipeIngredientService() {
		return new RecipeIngredientService();
	}
	public static UserIngredientService getUserIngredientService() {
		return new UserIngredientService();
	}
}