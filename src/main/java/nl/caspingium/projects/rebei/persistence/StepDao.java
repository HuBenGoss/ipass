package nl.caspingium.projects.rebei.persistence;


import nl.caspingium.projects.rebei.model.Step;

import java.util.ArrayList;

public interface StepDao {
    ArrayList<Step> findByRecipe(int recipeId);
}
