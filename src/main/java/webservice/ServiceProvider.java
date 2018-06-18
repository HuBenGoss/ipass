package webservice;

public class ServiceProvider {

	public static RecipeService getRecipeService() {
		return new RecipeService();
	}
}