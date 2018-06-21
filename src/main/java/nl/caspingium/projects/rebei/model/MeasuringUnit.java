package nl.caspingium.projects.rebei.model;

public class MeasuringUnit {
    private int id;
    private String name;

    public MeasuringUnit(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
