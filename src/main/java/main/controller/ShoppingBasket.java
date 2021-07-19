package main.controller;

import main.repository.DataBase;
import main.entity.Sale;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ShoppingBasket {
    private DecimalFormat df = new DecimalFormat("0.00");
    private final Scanner scanner = new Scanner(System.in);
    private Sale sale;
    private ArrayList<Sale> totalBasket = new ArrayList<>();
    private final Warehouse warehouse;
    private final DataBase dataBase;

    public ShoppingBasket(Warehouse warehouse, DataBase dataBase, Sale sale) {
        this.warehouse = warehouse;
        this.dataBase = dataBase;
        this.sale = sale;
    }

    public void addProductsInTheShoppingBasket(String username) {
        try {
            dataBase.displayCustomerItems();
            System.out.print("Please enter PRODUCT_ID to insert into Shopping Basket: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = dataBase.checkIfExists(id);
            if (test != 0) {
                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine());
                double price = dataBase.getProductPrice(id);
                String productName = dataBase.getProductName(id);
                if (dataBase.getQuantity(id) >= quantity) {
                    totalBasket.add(new Sale(id, productName, quantity, price, username));
                    System.out.println(productName + " successfully added to the Shopping Basket");
                } else {
                    if (dataBase.getQuantity(id) > 0) {
                        System.out.println("Unfortunately insufficient quantity in the stock!");
                        System.out.println("Available quantity " + dataBase.getQuantity(id));
                    } else {
                        System.out.println("Unfortunately " + productName + " is sold out!");
                    }
                }
            } else {
                System.out.println("Something went wrong! Please try again!");
                addProductsInTheShoppingBasket(username);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong! Please try again!");
            addProductsInTheShoppingBasket(username);
        }
    }

    public void removeFromBasket() {
        try {
            addProductInTheBasket();
            System.out.print("Please enter PRODUCT_ID to remove the product from the Shopping Basket: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (totalBasket.contains(id)) {
                totalBasket.remove(id);
            } else {
                warehouse.notExistingIDMessage(id);
                removeFromBasket();
            }
        } catch (Exception e) {
            System.out.println("Invalid input! Please choose a number from the menu to proceed!");
            System.out.println("Please try to remove the product again");
            removeFromBasket();
        }
    }

    private void addProductInTheBasket() {
        double subTotal = 0;
        System.out.println(" ");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        System.out.println(String.format("%-12s%-30s%-12s%-12s%-12s", "PRODUCT_ID ", "PRODUCT_NAME",
                "QUANTITY", "PRICE", "AMOUNT"));
        for (Sale sale : totalBasket) {
            subTotal = subTotal + (sale.getPrice() * sale.getQuantity());
            double sumPrice = (sale.getPrice() * sale.getQuantity());
            System.out.println(String.format("%-12s%-30s%-12s%-12s%-12s", sale.getId(), sale.getName(), +
                    (int) sale.getQuantity(), sale.getPrice(), df.format(sumPrice)));
        }
        printTotalCosts(subTotal, calculateTotalCosts(subTotal));
    }

    private double calculateTotalCosts(double subTotal) {
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        double tax = 1.21;
        double total = subTotal * tax;
        return Double.parseDouble(df.format(total));
    }

    private void printTotalCosts(double subTotal, double total) {
        double taxDifference = total - subTotal;
        System.out.println("\nSUBTOTAL " + df.format(subTotal));
        System.out.println("TAX RATE 21%");
        System.out.println("TAX \t " + df.format(taxDifference));
        System.out.print("TOTAL \t $");
        System.out.println(df.format(total));
    }

    private double basket() {
        double subTotal = 0;
        for (Sale sale : totalBasket) {
            subTotal = subTotal + (sale.getPrice() * sale.getQuantity());
        }
        return subTotal;
    }

    public void checkout(String username) {
        //int orderID = 110557;
        double payment = calculateTotalCosts(basket());
        if (getBudget(username) >= payment) {
            int orderID = dataBase.getLastOrder() + 1;
            for (Sale sale : totalBasket) {
                int oldQuantity = dataBase.getQuantity(sale.getId());
                int newQuantity = oldQuantity - sale.getQuantity();
                dataBase.editQuantity(newQuantity, sale.getId());

                dataBase.createSale(orderID, sale.getName(), sale.getQuantity(),
                        sale.getPrice(), sale.getUsername(), "2021-07-19");
            }
            System.out.println("ORDER ID: " + orderID);
            addProductInTheBasket();
            double currentBudget = getBudget(username) - payment;
            dataBase.updateBudget(username, currentBudget);
            System.out.println("Thank you for your purchase!");
            totalBasket.clear();
        } else {
            System.out.println("Payment rejected - you don't have enough money!");
        }
    }

    public void insertInTheBasket() {
        if (totalBasket.size() == 0) {
            System.out.println("Your Shopping Basket is empty! ");
        } else if (totalBasket.size() == 1) {
            System.out.println("You have " + totalBasket.size() + " item in your Shopping Basket!");
            addProductInTheBasket();
        } else {
            System.out.println("You have " + totalBasket.size() + " items in your Shopping Basket!");
            addProductInTheBasket();
        }
    }

    public double getBudget(String username) {
        return dataBase.checkBudget(username);
    }

    public void clearShoppingBasket() {
        totalBasket.clear();
        System.out.println("Shopping Basket successfully cleared!");
    }
}
