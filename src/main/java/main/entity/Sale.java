package main.entity;

public class Sale {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private String username;

    public Sale(int id, String name, int quantity, double price, String username) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.username = username;
    }

    public int getQuantity() {
        return quantity;
    }

    public Sale() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getUsername() {
        return username;
    }


}
