package main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private DecimalFormat df = new DecimalFormat("0.00");
    private Scanner scanner = new Scanner(System.in);
    private DataBase dataBase;
    private Warehouse warehouse;
    private Users users;
    private ShoppingBasket shoppingBasket;
    private final ArrayList<Sale> totalBasket = new ArrayList<>();
    private Wallet wallet;

    public Menu(DataBase dataBase, Warehouse warehouse,
                Users users, ShoppingBasket shoppingBasket, Wallet wallet) {
        this.dataBase = dataBase;
        this.warehouse = warehouse;
        this.users = users;
        this.shoppingBasket = shoppingBasket;
        this.wallet = wallet;
    }

    public void welcomeMessage() {
        System.out.println("*********************************");
        System.out.println("*                               *");
        System.out.println("*         W E L C O M E         *");
        System.out.println("*            TO THE             *");
        System.out.println("*          SUPERMARKET          *");
        System.out.println("*                               *");
        System.out.println("*********************************");
    }

    public void start() {
        getStartMenu();
        try {
            String userChoice = scanner.nextLine();
            Users activeUser = returnUserType(userChoice);

            if (activeUser == null) {
                invalidInputMessage();
                start();
            }

            warehouse.setActiveUser(activeUser);
            showUserMenu(activeUser.getUserType());

        } catch (NumberFormatException e) {
            invalidInputMessage();
            start();
        }
    }

    public String login(UserType userType) {
        try {
            System.out.println("Please LOGIN");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if ((dataBase.checkUsername(username).contains(username)) &&
                    (dataBase.checkPassword(password).contains(password) &&
                            (dataBase.checkUserType(username, password).contains(String.valueOf(userType))))) {
                return username;
            }
        } catch (Exception e) {
            System.out.println("Invalid username or password!!");
            handleException(userType);
        }
        return null;
    }

    private void handleException(UserType userType) {
        if (userType.equals(UserType.CUSTOMER)) {
            handleSignIn(userType);
        } else {
            start();
        }
    }

    private void handleSignIn(UserType userType) {
        try {
            System.out.println("Press: ");
            System.out.println("\t1 - Sign in");
            System.out.println("\t2 - Exit to Main menu");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                createUser(userType);
            } else if (choice.equals("2")) {
                start();
            } else {
                invalidInputMessage();
                start();
            }
        } catch (Exception e) {
            invalidInputMessage();
            handleSignIn(userType);
        }
    }

    private void handleAccess(UserType userType) {
        try {
            System.out.println("Press: ");
            System.out.println("\t1 - Log in");
            System.out.println("\t2 - Sign in");
            System.out.println("\t3 - Return to Main Menu");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                String username = login(userType);
                if (username != null) {
                    loginWelcomeMessage(username);
                    getCustomerMenu();
                    String userChoice = scanner.nextLine();
                    handleMenuChoice(userType, userChoice, username);
                } else {
                    loginErrorMessage();
                    handleSignIn(userType);
                }
            } else if (choice.equals("2")) {
                createUser(userType);
            } else if (choice.equals("3")) {
                start();
            } else {
                invalidInputMessage();
                start();
            }
        } catch (Exception e) {
            invalidInputMessage();
            handleSignIn(userType);
        }
    }

    public void createUser(UserType userType) {
        try {
            if (userType.equals(UserType.CUSTOMER)) {
                System.out.println("\nPlease Sign-In");
                getUserData(userType);
                System.out.print("Enter your budget: ");
                users.setBudget(Double.parseDouble(scanner.nextLine()));
                dataBase.createUserCustomer(users.getUsername(), users.getPassword(), users.getName(),
                        users.getEmail(), users.getBudget(), userType);

                System.out.println("Please Log in!");
                start();

            } else if (userType.equals(UserType.SALES_MANAGER)) {
                System.out.println("\nTo create Sales Manager please fill required information");
                getUserData(userType);
                dataBase.createUserCustomer(users.getUsername(), users.getPassword(), users.getName(),
                        users.getEmail(), 0.00, userType);

            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try again");
            createUser(userType);
        }
    }

    public void getUserData(UserType userType) {
        try {
            System.out.print("Enter username: ");
            users.setUsername(scanner.nextLine());
            if ((dataBase.checkUsername(users.getUsername()) != null)) {
                System.out.println("Unfortunately username already taken");
                System.out.println("Please try again!");
                createUser(userType);
            } else {
                System.out.print("Enter password: ");
                users.setPassword(scanner.nextLine());
                System.out.print("Enter name: ");
                users.setName(scanner.nextLine());
                System.out.print("Enter email: ");
                users.setEmail(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            System.out.println("Please try again!");
            createUser(userType);
        }
    }

    public void invalidInputMessage() {
        System.out.println("Invalid input! Please choose a number from the menu to proceed!");
    }

    private void proceedExit() {
        try {
            System.out.println("Press: ");
            System.out.println("\t1 - To Exit");
            System.out.println("\t2 - Open the Main Menu");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                quit();
            } else if (choice.equals("2")) {
                start();
            } else {
                invalidInputMessage();
                proceedExit();
            }
        } catch (Exception exception) {
            invalidInputMessage();
            proceedExit();
        }
    }

    private void quit() {
        System.out.println("Thank you for visiting SuperMarket!");
        System.out.println("Application has stopped working!");
        System.exit(0);
    }

    private Users returnUserType(String userChoice) {
        switch (Integer.parseInt(userChoice)) {
            case 1:
                return new Users(UserType.ADMINISTRATOR);
            case 2:
                return new Users(UserType.SALES_MANAGER);
            case 3:
                return new Users(UserType.CUSTOMER);
            case 4:
                quit();
            default:
                break;
        }
        return null;
    }

    private void showUserMenu(UserType userType) {
        switch (userType) {
            case ADMINISTRATOR:
                String username = login(userType);
                handleAdministratorAccess(username, userType);
                break;
            case SALES_MANAGER:
                username = login(userType);
                handleSalesManagerAccess(username, userType);
                break;
            case CUSTOMER:
                handleAccess(userType);
                break;
            default:
                start();
                break;
        }
    }

    private void handleSalesManagerAccess(String username, UserType userType) {
        if (username != null) {
            loginWelcomeMessage(username);
            getSalesManagerMenu();
            String userChoice = scanner.nextLine();
            handleMenuChoice(userType, userChoice, username);
        } else {
            loginErrorMessage();
            start();
        }
    }

    private void handleAdministratorAccess(String username, UserType userType) {
        if (username != null) {
            loginWelcomeMessage(username);
            getAdministratorMenu();
            String userChoice = scanner.nextLine();
            handleMenuChoice(userType, userChoice, username);
        } else {
            loginErrorMessage();
            start();
        }
    }

    private void loginWelcomeMessage(String username) {
        System.out.println("\nWelcome " + username + " to the Supermarket!");
    }

    private void loginErrorMessage() {
        System.out.println("Invalid username or password!");
        System.out.println("Please try again!");
    }

    private void handleMenuChoice(UserType userType, String userChoice, String username) {
        switch (userType) {
            case ADMINISTRATOR:
                handleAdministratorChoice(userChoice, username);
                break;
            case SALES_MANAGER:
                handleSalesManagerChoice(userChoice, username);
                break;
            case CUSTOMER:
                handleCustomerChoice(userChoice, username);
                break;
            default:
                start();
                break;
        }
    }

    private void getStartMenu() {
        System.out.println("\nPlease enter a number from the menu to proceed with the login: ");
        System.out.println("\t 1 - Administrator");
        System.out.println("\t 2 - Sales Manager");
        System.out.println("\t 3 - Customer");
        System.out.println("\t 4 - Exit the Supermarket");
    }

    private void getAdministratorMenu() {
        System.out.println("\nChoose the option to proceed: " +
                "\n\t 1. Create product" +
                "\n\t 2. Edit product " +
                "\n\t 3. Remove product" +
                "\n\t 4. View products" +
                "\n\t 5. View Sales reports" +
                "\n\t 6. View all users" +
                "\n\t 7. Create Sales Manager user" +
                "\n\t 8. Logout");
    }

    private void handleAdministratorChoice(String userChoice, String username) {
        switch (userChoice) {
            case "1":
                warehouse.addProduct();
                break;
            case "2":
                editProduct(username);
                break;
            case "3":
                warehouse.removeProduct();
                break;
            case "4":
                warehouse.display();
                break;
            case "5":
                warehouse.salesReport();
                break;
            case "6":
                dataBase.displayUsers();
                break;
            case "7":
                createUser(UserType.SALES_MANAGER);
                break;
            case "8":
                proceedExit();
                break;
            default:
                invalidInputMessage();
                getAdministratorMenu();
                userChoice = scanner.nextLine();
                handleAdministratorChoice(userChoice, username);
                break;
        }
        getAdministratorMenu();
        userChoice = scanner.nextLine();
        handleAdministratorChoice(userChoice, username);
    }

    private void getSalesManagerMenu() {
        System.out.println("\nChoose the option to proceed: " +
                "\n\t 1. Restock products" +
                "\n\t 2. Edit product" +
                "\n\t 3. Price change" +
                "\n\t 4. Display sales" +
                "\n\t 5. View Products" +
                "\n\t 6. View Sales reports" +
                "\n\t 7. Logout");
    }

    private void handleSalesManagerChoice(String userChoice, String username) {
        switch (userChoice) {
            case "1":
                warehouse.restock();
                break;
            case "2":
                editProduct(username);
                break;
            case "3":
                warehouse.editProductPrice();
                break;
            case "4":
                dataBase.displaySales();
                break;
            case "5":
                warehouse.display();
                break;
            case "6":
                warehouse.salesReport();
                break;
            case "7":
                proceedExit();
                break;
            default:
                invalidInputMessage();
                getSalesManagerMenu();
                userChoice = scanner.nextLine();
                handleSalesManagerChoice(userChoice, username);
                break;
        }
        getSalesManagerMenu();
        userChoice = scanner.nextLine();
        handleSalesManagerChoice(userChoice, username);
    }

    private void editProduct(String username) {
        getEditMenu();
        try {
            String choice = scanner.nextLine();
            handleEditMenu(choice, username);
        } catch (Exception e) {
        }
    }

    private void getCustomerMenu() {
        System.out.println("\nChoose the option to proceed: " +
                "\n\t 1. Your Digital Wallet" +
                "\n\t 2. View Shopping Basket" +
                "\n\t 3. Add an item to the Shopping Basket" +
                "\n\t 4. Remove an item from the Shopping Basket" +
                "\n\t 5. Clear the Shopping Basket" +
                "\n\t 6. Proceed to Checkout" +
                "\n\t 7. Order history" +
                "\n\t 8. Logout");
    }

    private void handleCustomerChoice(String userChoice, String username) {
        switch (userChoice) {
            case "1":
                wallet.wallet(username);
                getWalletMenu();
                String choice = scanner.nextLine();
                handleWalletChoice(choice, username);
                break;
            case "2":
                shoppingBasket.insertInTheBasket();
                break;
            case "3":
                shoppingBasket.addProductsInTheShoppingBasket(username);
                break;
            case "4":
                shoppingBasket.removeFromBasket();
                break;
            case "5":
                shoppingBasket.clearShoppingBasket();
                break;
            case "6":
                shoppingBasket.checkout(username);
                break;
            case "7":
                dataBase.displayCustomerSales(username);
                break;
            case "8":
                totalBasket.clear();
                proceedExit();
                break;
            default:
                invalidInputMessage();
                getCustomerMenu();
                userChoice = scanner.nextLine();
                handleCustomerChoice(userChoice, username);
                break;
        }
        getCustomerMenu();
        userChoice = scanner.nextLine();
        handleCustomerChoice(userChoice, username);
    }

    public void getWalletMenu() {
        System.out.println("\nChoose the option to proceed: " +
                "\n\t 1. Add money to the  Digital Wallet " +
                "\n\t 2. Transfer money" +
                "\n\t 3. View payments" +
                "\n\t 4. Return to Main menu");
    }

    public void handleWalletChoice(String userChoice, String username) {
        switch (userChoice) {
            case "1":
                wallet.addMoneyInTheWallet(username);
                break;
            case "2":
                wallet.transferMoney(username);
                break;
            case "3":
                dataBase.displayUserPurchase(username);
                break;
            case "4":
                getCustomerMenu();
                userChoice = scanner.nextLine();
                handleCustomerChoice(userChoice, username);
                break;
            default:
                invalidInputMessage();
                getWalletMenu();
                userChoice = scanner.nextLine();
                handleWalletChoice(userChoice, username);
                break;
        }
    }

    private void getEditMenu() {
        System.out.println("\nChoose field to edit: " +
                "\n\t 1. Name" +
                "\n\t 2. Quantity" +
                "\n\t 3. Price" +
                "\n\t 4. Unit" +
                "\n\t 5. Category" +
                "\n\t 6. Return to Main Menu");
    }

    private void handleEditMenu(String userChoice, String username) {
        switch (userChoice) {
            case "1":
                warehouse.editProductName();
                break;
            case "2":
                warehouse.editProductQuantity();
                break;
            case "3":
                warehouse.editProductPrice();
                break;
            case "4":
                warehouse.editProductUnit();
                break;
            case "5":
                warehouse.editProductCategory();
                break;
            case "6":
                getCustomerMenu();
                userChoice = scanner.nextLine();
                handleCustomerChoice(userChoice, username);
                break;
            default:
                invalidInputMessage();
                getWalletMenu();
                userChoice = scanner.nextLine();
                handleWalletChoice(userChoice, username);
                break;
        }
    }

}
