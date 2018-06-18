package webservice;

public class ServiceProvider {
	private static RecipeService recipeService = new RecipeService();

	public static RecipeService getWorldService() {
		return recipeService;
	}
}