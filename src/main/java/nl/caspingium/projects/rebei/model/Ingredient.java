package nl.caspingium.projects.rebei.model;

public class Ingredient {

    private int id;
    private String name;
    private String measuringUnit;


    public Ingredient(int id,String name, String measuringUnit) {
        this.id = id;
        this.name = name;
        this.measuringUnit = measuringUnit;

    }

    public String getName() {
        return name;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public int getId() {
        return id;
    }
}
