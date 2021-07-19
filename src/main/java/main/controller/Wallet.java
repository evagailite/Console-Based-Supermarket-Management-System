package main.controller;

import main.repository.DataBase;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Scanner;

public class Wallet {
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final Scanner scanner = new Scanner(System.in);
    private final DataBase dataBase;
    private final ShoppingBasket shoppingBasket;

    public Wallet(DataBase dataBase, ShoppingBasket shoppingBasket) {
        this.dataBase = dataBase;
        this.shoppingBasket = shoppingBasket;
    }

    public void transferMoney(String username) {
        try {
            System.out.print("Enter amount you want to transfer: ");
            double budget = Double.parseDouble(scanner.nextLine());
            if (shoppingBasket.getBudget(username) >= budget) {
                System.out.print("Enter your bank account: ");
                String bankAccount = scanner.nextLine();
                double newBudget = shoppingBasket.getBudget(username) - budget;
                dataBase.updateBudget(username, newBudget);
                System.out.println("$" + budget + " transferred to the " + bankAccount + " bank account");
                if (newBudget > 0) {
                    System.out.println("\nYou have $" + df.format(shoppingBasket.getBudget(username)) +
                            " in the Digital Wallet!");
                } else {
                    System.out.println("Your Digital wallet is empty!");
                }
            } else {
                System.out.println("Not enough money in your wallet!");
            }
        } catch (Exception exception) {
            System.out.println("Something went wrong! Please try again!");
        }
    }

    public void addMoneyInTheWallet(String username) {
        try {
            System.out.print("Please enter your budget: ");
            double budget = Double.parseDouble(scanner.nextLine());
            double newBudget = shoppingBasket.getBudget(username) + budget;
            dataBase.updateBudget(username, newBudget);
        } catch (Exception exception) {
            System.out.println("Something went wrong! Please try again!");
            addMoneyInTheWallet(username);
        }
    }

    public void wallet(String username) {
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        double currentWallet = shoppingBasket.getBudget(username);
        if (currentWallet > 0) {
            System.out.println("You have $" + df.format(shoppingBasket.getBudget(username)) +
                    " in the Digital Wallet!");
        } else {
            System.out.println("Your digital Wallet is empty");
        }
    }
}
