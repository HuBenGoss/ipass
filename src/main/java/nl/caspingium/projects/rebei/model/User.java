package nl.caspingium.projects.rebei.model;

import java.util.ArrayList;

public class User {

    private String firstName;
    private String lastName;
    private String email;

    private int role;
    private ArrayList<Stock>stock;

    public User(String firstName, String lastName, String email, int role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.stock = new ArrayList<>();
    }

    public void addStock(Stock stock) {
        this.stock.add(stock);
    }
}
