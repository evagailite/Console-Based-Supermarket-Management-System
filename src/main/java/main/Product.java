package main;

public class Product {

    private String name;
    private double price;
    private double quantityInStock;
    private double pricePerUnit;
    private ProductUnit productUnit;
    private Category category;

    public Product(String name, double price, double quantityInStock,
                   double pricePerUnit, ProductUnit productUnit, Category category) {
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.pricePerUnit = pricePerUnit;
        this.productUnit = productUnit;
        this.category = category;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public ProductUnit getProductUnit() {
        return productUnit;
    }

    public Category getCategory() {
        return category;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public void setProductUnit(ProductUnit productUnit) {
        this.productUnit = productUnit;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
