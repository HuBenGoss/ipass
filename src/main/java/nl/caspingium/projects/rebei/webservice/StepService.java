package nl.caspingium.projects.rebei.webservice;

import nl.caspingium.projects.rebei.model.Step;
import nl.caspingium.projects.rebei.persistence.StepDao;
import nl.caspingium.projects.rebei.persistence.StepPostgresDaoImpl;

import java.util.ArrayList;

public class StepService {
    protected ArrayList<Step> allSteps = null;
    StepDao dao = new StepPostgresDaoImpl();

    public StepService(){}

    public ArrayList<Step> getStepByRecipe(int id) {
        return dao.findByRecipe(id);
    }

}
