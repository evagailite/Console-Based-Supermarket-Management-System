package main;

import main.controller.Menu;
import main.controller.ShoppingBasket;
import main.controller.Wallet;
import main.controller.Warehouse;
import main.entity.Product;
import main.entity.Sale;
import main.entity.Users;
import main.repository.DataBase;

public class Main {
    public static void main(String[] args) {

        DataBase dataBase = new DataBase();
        if (!dataBase.open()) {
            System.out.println("Something went wrong. Can't open database.");
            return;
        }

        Users users = new Users();
        Product product = new Product();
        Warehouse warehouse = new Warehouse(product, dataBase);
        Sale sale = new Sale();
        ShoppingBasket shoppingBasket = new ShoppingBasket(warehouse, dataBase, sale);
        Wallet wallet = new Wallet(dataBase, shoppingBasket);
        Menu menu = new Menu(dataBase, warehouse, users, shoppingBasket, wallet);

        menu.welcomeMessage();
        menu.start();

        dataBase.close();
    }
}
