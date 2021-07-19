package main;

import java.util.Date;
import java.util.Scanner;

public class Warehouse {
    private final Scanner scanner = new Scanner(System.in);
    private Users users;
    private Product product;
    private final DataBase dataBase;

    public Warehouse(Product product, DataBase dataBase) {
        this.product = product;
        this.dataBase = dataBase;
    }

    public void setActiveUser(Users activeUsers) {
        this.users = users;
    }

    public Users getActiveUsers() {
        return users;
    }

    public void addProduct() {
        try {
            System.out.print("Enter product name: ");
            product.setName(scanner.nextLine());
            System.out.print("Enter product quantity: ");
            product.setQuantityInStock(scanner.nextDouble());
            scanner.nextLine();
            System.out.print("Enter product price: ");
            product.setPrice(Double.parseDouble(scanner.nextLine()));
            System.out.print("Enter product unit - KILOGRAM, LITRE, PIECE: ");
            product.setProductUnit(ProductUnit.valueOf(scanner.nextLine().toUpperCase()));
            System.out.print("Enter product category - FOOD, DRINK, NON_FOOD, FROZEN_FOOD: ");
            product.setCategory(Category.valueOf(scanner.nextLine().toUpperCase()));
            dataBase.addItems(product.getName(), product.getQuantityInStock(),
                    product.getPrice(), product.getProductUnit(), product.getCategory());
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to create the product again");
            addProduct();
        }
    }

    public void editProductPrice() {
        try {
            display();
            System.out.print("Please enter PRODUCT_ID to modify the product price: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = dataBase.checkIfExists(id);
            if (test != 0) {
                System.out.print("Please enter a new price: ");
                double price = Double.parseDouble(scanner.nextLine());
                dataBase.editPrice(price, id);
            } else {
                notExistingIDMessage(id);
                editProductPrice();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to modify the product price again");
            editProductPrice();
        }
    }

    public void display() {
        dataBase.printCount();
        dataBase.displayItems();
    }

    public void removeProduct() {
        try {
            display();
            System.out.print("Please enter PRODUCT_ID to remove the product: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = dataBase.checkIfExists(id);
            if (test != 0) {
                dataBase.removeItem(id);
                dataBase.resetId();
                dataBase.reset();
            } else {
                notExistingIDMessage(id);
                removeProduct();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to remove the product again");
            removeProduct();
        }
    }

    public void editProductName() {
        try {
            display();
            System.out.print("Please enter PRODUCT_ID to modify the product name: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = dataBase.checkIfExists(id);
            if (test != 0) {
                System.out.print("Please enter a new product name: ");
                String name = scanner.nextLine();
                dataBase.editName(name, id);
            } else {
                notExistingIDMessage(id);
                editProductName();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to change the product name again");
            editProductName();
        }
    }

    public void editProductQuantity() {
        try {
            display();
            System.out.print("Please enter PRODUCT_ID to modify the product quantity: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = dataBase.checkIfExists(id);
            if (test != 0) {
                System.out.print("Please enter a new product quantity: ");
                double quantity = scanner.nextDouble();
                scanner.nextLine();
                dataBase.editQuantity(quantity, id);
                System.out.println("Successfully updated one row");
            } else {
                notExistingIDMessage(id);
                editProductQuantity();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to change the product quantity again");
            editProductQuantity();
        }
    }

    public void editProductUnit() {
        try {
            display();
            System.out.print("Please enter PRODUCT_ID to modify the product unit: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = dataBase.checkIfExists(id);
            if (test != 0) {
                System.out.print("Please enter a new product unit - KILOGRAM, LITRE, PIECE: ");
                product.setProductUnit(ProductUnit.valueOf(scanner.nextLine().toUpperCase()));
                dataBase.editUnit(product.getProductUnit(), id);
            } else {
                notExistingIDMessage(id);
                editProductUnit();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to change the product unit again!");
            editProductUnit();
        }
    }

    public void editProductCategory() {
        try {
            display();
            System.out.print("Please enter PRODUCT_ID to modify the product category: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = dataBase.checkIfExists(id);
            if (test != 0) {
                System.out.print("Please enter a new product category - FOOD, DRINK, NON_FOOD, FROZEN_FOOD: ");
                product.setCategory(Category.valueOf(scanner.nextLine().toUpperCase()));
                dataBase.editCategory(product.getCategory(), id);
            } else {
                notExistingIDMessage(id);
                editProductCategory();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to change the product category again!");
            editProductCategory();
        }
    }

    public void invalidInputMessage() {
        System.out.println("Invalid input!");
    }

    public void notExistingIDMessage(int id) {
        System.out.println("PRODUCT_ID " + id + " not found! Please try again!");
    }

    public void restock() {
        try {
            display();
            System.out.print("Please enter PRODUCT_ID to restock the product: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = dataBase.checkIfExists(id);
            if (test != 0) {
                System.out.print("Please enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();
                int oldQuantity = dataBase.getQuantity(id);
                int newQuantity = oldQuantity + quantity;
                dataBase.editQuantity(newQuantity, id);
                System.out.println("Successfully updated one row");
            } else {
                notExistingIDMessage(id);
                editProductQuantity();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to change the product quantity again");
            editProductQuantity();
        }
    }

    public void salesReport() {
        Date date = new Date();
        System.out.println(date);

        System.out.println("\nTop 3 best-selling products: ");
        dataBase.bestSellingProduct();

        System.out.println("\nGross sales of all orders with VAT included: $" + dataBase.sumTotalPurchase());
        System.out.println("\nIn total " + dataBase.countOrders() + " orders were placed in the Supermarket:");

        dataBase.displayAllSalesReport();


    }
}
