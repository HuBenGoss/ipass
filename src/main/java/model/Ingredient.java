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
}
