package model;

public class Ingredient {

    private int id;
    private String name;
    private MeasuringUnit measuringUnit;

    public Ingredient(int id,String name, MeasuringUnit measuringUnit) {
        this.id = id;
        this.name = name;
        this.measuringUnit = measuringUnit;
    }

    public String getName() {
        return name;
    }

    public MeasuringUnit getMeasuringUnit() {
        return measuringUnit;
    }

    public int getId() {
        return id;
    }
}
