package nl.caspingium.projects.rebei.model;

public class Step {
    private int id;
    private int stepnr;
    private String description;

    public Step(int id, int stepnr, String description) {
        this.id = id;
        this.stepnr = stepnr;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getStepnr() {
        return stepnr;
    }

    public String getDescription() {
        return description;
    }
}
