package main.repository;

import main.types.Category;
import main.types.ProductUnit;
import main.types.UserType;

import java.sql.*;
import java.util.Scanner;

public class DataBase {
    private static Scanner scanner = new Scanner(System.in);
    private Connection connection;
    // JDBC driver name and database URL
    private static final String DB_URL = "jdbc:h2:C:\\Users\\User\\Dropbox\\Programming\\AccentureBootcamp2021\\projects\\supermarket";
    //  Database credentials
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    //1st table
    private static final String TABLE_USERS = "users";

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "first_name";
    private static final String EMAIL = "email";
    private static final String BUDGET = "budget";
    private static final String USER_TYPE = "user_type";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
            USERNAME + " VARCHAR(50) NOT NULL, " +
            PASSWORD + " VARCHAR(50) NOT NULL, " +
            FIRST_NAME + " VARCHAR(50) NOT NULL, " +
            EMAIL + " VARCHAR(50) NOT NULL, " +
            BUDGET + " DECIMAL(20,2), " +
            USER_TYPE + " VARCHAR(50) NOT NULL, " +
            "CONSTRAINT pk_username PRIMARY KEY (" + USERNAME + ")" +
            ");";
    //2nd table
    private static final String TABLE_PRODUCTS = "products";

    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_NAME = "product_name";
    private static final String QUANTITY = "quantity";
    private static final String PRICE = "price";
    private static final String UNIT = "unit";
    private static final String PRODUCT_CATEGORY = "product_category";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + " (" +
            PRODUCT_ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            PRODUCT_NAME + " VARCHAR(100) NOT NULL, " +
            QUANTITY + " INTEGER NOT NULL, " +
            PRICE + " DECIMAL NOT NULL, " +
            UNIT + " VARCHAR(200) NOT NULL, " +
            PRODUCT_CATEGORY + " VARCHAR(200) NOT NULL " +
            ");";
    //3rd table
    private static final String TABLE_SALES = "sales";

    private static final String SALES_ID = "sales_id";
    private static final String ORDER_ID = "order_id";
    private static final String PRODUCT = "product";
    private static final String PURCHASE_DATE = "purchase_date";

    private static final String CREATE_TABLE_SALES = "CREATE TABLE IF NOT EXISTS " + TABLE_SALES + " (" +
            SALES_ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            ORDER_ID + " INTEGER NOT NULL, " +
            PRODUCT + " VARCHAR(100) NOT NULL," +
            QUANTITY + " INTEGER NOT NULL, " +
            PRICE + " DECIMAL NOT NULL," +
            USERNAME + " VARCHAR(100) NOT NULL," +
            PURCHASE_DATE + " DATE NOT NULL " +
            ");";


//    private static final String CREATE_TABLE_SALES = "CREATE TABLE IF NOT EXISTS " + TABLE_SALES + " (" +
//            SALES_ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " +
//            ORDER_ID + " INTEGER NOT NULL, " +
//            PRODUCT + " VARCHAR(100) NOT NULL REFERENCES " + TABLE_PRODUCTS + "(" + PRODUCT_ID + ")," +
//            QUANTITY + " DECIMAL NOT NULL, " +
//            PRICE + " DECIMAL NOT NULL REFERENCES " + TABLE_PRODUCTS + "(" + PRODUCT_ID + ")," +
//            USERNAME + " VARCHAR(100) NOT NULL REFERENCES " + TABLE_USERS + "(" + USERNAME + ")," +
//            PURCHASE_DATE + " DATE NOT NULL " +
//            ");";

    public boolean open() {
        try {
            connection = getConnection();
            prepareDatabase(connection);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection " + e.getMessage());
        }
    }

    private static void prepareDatabase(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            //  statement.executeUpdate("DROP TABLE " + TABLE_SALES);
            statement.executeUpdate(CREATE_TABLE_USERS);
            statement.executeUpdate(CREATE_TABLE_PRODUCTS);
            statement.executeUpdate(CREATE_TABLE_SALES);
        }
    }

    private static final String ADD_PRODUCT = "INSERT INTO " + TABLE_PRODUCTS + "(" +
            PRODUCT_NAME + ", " + QUANTITY + ", " + PRICE + ", " +
            UNIT + ", " + PRODUCT_CATEGORY + ")" + " VALUES (?, ?, ?, ?, ? )";

    public void addItems(String name, double quantity,
                         double price, ProductUnit productUnit, Category category) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_PRODUCT)) {
                preparedStatement.setString(1, name);
                preparedStatement.setDouble(2, quantity);
                preparedStatement.setDouble(3, price);
                preparedStatement.setString(4, String.valueOf(productUnit));
                preparedStatement.setString(5, String.valueOf(category));
                preparedStatement.executeUpdate();
                System.out.println("\n" + name + " successfully added to the Supermarket!");
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String ADD_USER_CUSTOMER = "INSERT INTO " + TABLE_USERS + "(" +
            USERNAME + ", " + PASSWORD + ", " + FIRST_NAME + ", " +
            EMAIL + ", " + BUDGET + ", " + USER_TYPE + ")" + " VALUES (?, ?, ?, ?, ?, ? )";

    public void createUserCustomer(String username, String password, String firstName,
                                   String email, double budget, UserType userType) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_CUSTOMER)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, firstName);
                preparedStatement.setString(4, email);
                preparedStatement.setDouble(5, budget);
                preparedStatement.setString(6, String.valueOf(userType));
                preparedStatement.executeUpdate();
                System.out.println("\n" + username + " account successfully created!");
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String ADD_SALES = "INSERT INTO " + TABLE_SALES + "(" +
            ORDER_ID + ", " + PRODUCT + ", " + QUANTITY + ", " +
            PRICE + ", " + USERNAME + ", " + PURCHASE_DATE + ")" + " VALUES (?, ?, ?, ?, ?, ? )";

    public void createSale(int order_id, String product, int quantity,
                           double price, String username, String date) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_SALES)) {
                preparedStatement.setInt(1, order_id);
                preparedStatement.setString(2, product);
                preparedStatement.setInt(3, quantity);
                preparedStatement.setDouble(4, price);
                preparedStatement.setString(5, username);
                preparedStatement.setString(6, date);
                preparedStatement.executeUpdate();
                //  System.out.println("\nPurchase successfully made!");
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    public static final String CHECK_FOR_PASSWORD = "SELECT " + PASSWORD + " FROM " + TABLE_USERS + " WHERE " +
            PASSWORD + " =?";

    public String checkPassword(String password) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_FOR_PASSWORD)) {
                preparedStatement.setString(1, password);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        password = rs.getString(PASSWORD);
                    } else {
                        return null;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return password;
    }

    public static final String CHECK_FOR_USERNAME = "SELECT " + USERNAME + " FROM " + TABLE_USERS + " WHERE " +
            USERNAME + " =?";

    public String checkUsername(String username) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_FOR_USERNAME)) {
                preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        username = rs.getString(USERNAME);
                        return username;
                    } else {
                        return null;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return null;
    }

    public static final String GET_BUDGET = "SELECT " + BUDGET + " FROM " + TABLE_USERS + " WHERE " +
            USERNAME + " =?";

    public double checkBudget(String username) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BUDGET)) {
                preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        double budget = rs.getDouble(BUDGET);
                        return budget;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return 0;
    }

    public static final String CHECK_FOR_USERTYPE = "SELECT " + USER_TYPE + " FROM " + TABLE_USERS + " WHERE " +
            USERNAME + " =? AND " + PASSWORD + "=?";

    public String checkUserType(String username, String password) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_FOR_USERTYPE)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        String userType = rs.getString(USER_TYPE);
                        return userType;
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return null;
    }

    //
//    private static final String CLEAN_TABLE = "DROP TABLE " + TABLE_ITEMS + ";";
//
//    public void cleanTable() {
//        try (Connection connection = getConnection()) {
//            try (Statement statement = connection.createStatement()) {
//                statement.executeUpdate(CLEAN_TABLE);
//                statement.executeUpdate(CREATE_TABLE_ITEMS);
//            }
//        } catch (SQLException throwables) {
//            System.out.println("Something went wrong " + throwables.getMessage());
//            throwables.printStackTrace();
//        }
//    }
//

    private static final String DELETE_ITEM = "DELETE FROM " + TABLE_PRODUCTS + " WHERE " + PRODUCT_ID + "=?";

    public void removeItem(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_ITEM)) {
                statement.setInt(1, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    System.out.println("Successfully deleted one row");
                } else if (update == 0) {
                    System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String UPDATE_PRICE = "UPDATE " + TABLE_PRODUCTS + " SET " + PRICE + " = ? WHERE "
            + PRODUCT_ID + "=?";

    public void editPrice(double price, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_PRICE)) {
                statement.setDouble(1, price);
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    System.out.println("Successfully updated one row");
                } else if (update == 0) {
                    System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String UPDATE_QUANTITY = "UPDATE " + TABLE_PRODUCTS + " SET " + QUANTITY + " = ? WHERE "
            + PRODUCT_ID + "=?";

    public void editQuantity(double quantity, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUANTITY)) {
                statement.setDouble(1, quantity);
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    //  System.out.println("Successfully updated one row");
                } else if (update == 0) {
                    //  System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String UPDATE_UNIT = "UPDATE " + TABLE_PRODUCTS + " SET " + UNIT + " = ? WHERE "
            + PRODUCT_ID + "=?";

    public void editUnit(ProductUnit productUnit, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_UNIT)) {
                statement.setString(1, String.valueOf(productUnit));
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    System.out.println("Successfully updated one row");
                } else if (update == 0) {
                    System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String UPDATE_CATEGORY = "UPDATE " + TABLE_PRODUCTS + " SET " + PRODUCT_CATEGORY + " = ? WHERE "
            + PRODUCT_ID + "=?";

    public void editCategory(Category category, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_CATEGORY)) {
                statement.setString(1, String.valueOf(category));
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    System.out.println("Successfully updated one row");
                } else if (update == 0) {
                    System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }


    private static final String UPDATE_NAME = "UPDATE " + TABLE_PRODUCTS + " SET " + PRODUCT_NAME + " = ? WHERE "
            + PRODUCT_ID + "=?";

    public void editName(String name, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_NAME)) {
                statement.setString(1, name);
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    System.out.println("Successfully updated one row");
                } else if (update == 0) {
                    System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String DISPLAY_ITEMS = "SELECT * FROM " + TABLE_PRODUCTS + ";";

    public void displayItems() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(DISPLAY_ITEMS);
                try (ResultSet rs = statement.executeQuery(DISPLAY_ITEMS)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 2) {
                            System.out.printf("%-30s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-12s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 2) {
                                System.out.printf("%-30s", rs.getString(i));
                            } else {
                                System.out.printf("%-12s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static final String DISPLAY_USERS = "SELECT * FROM " + TABLE_USERS + ";";

    public void displayUsers() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(DISPLAY_USERS);
                try (ResultSet rs = statement.executeQuery(DISPLAY_USERS)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 4) {
                            System.out.printf("%-30s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-17s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 4) {
                                System.out.printf("%-30s", rs.getString(i));
                            } else {
                                System.out.printf("%-17s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static final String DISPLAY_SALES = "SELECT * FROM " + TABLE_SALES + ";";

    public void displaySales() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(DISPLAY_SALES);
                try (ResultSet rs = statement.executeQuery(DISPLAY_SALES)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 3) {
                            System.out.printf("%-30s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-12s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 3) {
                                System.out.printf("%-30s", rs.getString(i));
                            } else {
                                System.out.printf("%-12s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static final String DISPLAY_CUSTOMER_ITEMS = "SELECT " + PRODUCT_ID + ", " +
            PRODUCT_NAME + ", " + PRICE + ", " + " FROM " + TABLE_PRODUCTS + ";";

    public void displayCustomerItems() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(DISPLAY_CUSTOMER_ITEMS);
                try (ResultSet rs = statement.executeQuery(DISPLAY_CUSTOMER_ITEMS)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i <= 1) {
                            System.out.printf("%-15s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-30s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i <= 1) {
                                System.out.printf("%-15s", rs.getString(i));
                            } else {
                                System.out.printf("%-30s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String DISPLAY_CUSTOMER_SALES = "SELECT " + ORDER_ID + ", " +
            PRODUCT + ", " + QUANTITY + ", " + PRICE + "," + PURCHASE_DATE +
            " FROM " + TABLE_SALES + " WHERE " + USERNAME + "=?;";

    public void displayCustomerSales(String username) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DISPLAY_CUSTOMER_SALES)) {
                preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 2) {
                            System.out.printf("%-30s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-12s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 2) {
                                System.out.printf("%-30s", rs.getString(i));
                            } else {
                                System.out.printf("%-12s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }


    private static final String COUNT_ITEMS = "SELECT COUNT (DISTINCT " + PRODUCT_ID + ") FROM " + TABLE_PRODUCTS + ";";

    public void printCount() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(COUNT_ITEMS);
                try (ResultSet rs = statement.executeQuery(COUNT_ITEMS)) {
                    while (rs.next()) {
                        String title = rs.getString(1);
                        System.out.println("You have " + title + " products in the Supermarket");
                        System.out.println(" ");
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String RESET_ID = "ALTER TABLE " + TABLE_PRODUCTS + " DROP " + PRODUCT_ID + ";";

    public void resetId() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(RESET_ID);
            }

        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String RESET = "ALTER TABLE " + TABLE_PRODUCTS + " ADD " + PRODUCT_ID +
            " int UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST;";

    public void reset() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(RESET);
            }

        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }


    public static final String CHECK_ID = "SELECT " + PRODUCT_ID + " FROM " +
            TABLE_PRODUCTS + " WHERE " + PRODUCT_ID + "=?";

    public int checkIfExists(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_ID)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        id = rs.getInt(PRODUCT_ID);
                    } else {
                        return 0;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return id;
    }

    public static final String GET_PRODUCT_NAME = "SELECT " + PRODUCT_NAME + " FROM " +
            TABLE_PRODUCTS + " WHERE " + PRODUCT_ID + "=?";

    public String getProductName(int id) {
        String name = null;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_NAME)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        name = rs.getString(PRODUCT_NAME);
                    } else {
                        return null;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return name;
    }

    public static final String GET_PRODUCT_PRICE = "SELECT " + PRICE + " FROM " +
            TABLE_PRODUCTS + " WHERE " + PRODUCT_ID + "=?";

    public double getProductPrice(int id) {
        double price = 0;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_PRICE)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        price = rs.getDouble(PRICE);
                    } else {
                        return 0;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return price;
    }

    public static final String GET_LAST_ORDER_NUMBER = "SELECT " + ORDER_ID + " FROM " +
            TABLE_SALES + " ORDER BY " + SALES_ID + " DESC LIMIT 1;";

    public int getLastOrder() {
        int orderId = 0;
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(GET_LAST_ORDER_NUMBER);
                try (ResultSet rs = statement.executeQuery(GET_LAST_ORDER_NUMBER)) {
                    while (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return orderId;
    }

    private static final String UPDATE_BUDGET = "UPDATE " + TABLE_USERS + " SET " + BUDGET + " = ? WHERE "
            + USERNAME + "=?";

    public void updateBudget(String username, double budget) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_BUDGET)) {
                statement.setDouble(1, budget);
                statement.setString(2, username);
                int update = statement.executeUpdate();
                if (update == 1) {
                    System.out.println("\nPayment processed!");
                } else if (update == 0) {
                    System.out.println("Something went wrong! Payment was rejected!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    public static final String GET_QUANTITY = "SELECT " + QUANTITY + " FROM " +
            TABLE_PRODUCTS + " WHERE " + PRODUCT_ID + "=?";

    public int getQuantity(int id) {
        int quantity = 0;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_QUANTITY)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        quantity = rs.getInt(QUANTITY);
                    } else {
                        return 0;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return quantity;
    }

    //ALTER TABLE TEST ALTER COLUMN PRICE SET DATA TYPE DECIMAL(20,2);
    //ALTER TABLE PRODUCTS ALTER COLUMN QUANTITY SET DATA TYPE INTEGER;

        /*
SELECT
ORDER_ID, USERNAME, PURCHASE_DATE,
SUM (QUANTITY * PRICE ) AS TOTAL
FROM SALES
WHERE USERNAME = 'test777'
GROUP BY ORDER_ID;
     */

    public static final String DISPLAY_USER_PURCHASE = "SELECT " + PURCHASE_DATE + ", " + ORDER_ID +
            ", SUM ( " + QUANTITY + " * " + PRICE + ") AS TOTAL FROM  " + TABLE_SALES + " WHERE " + USERNAME + "=? GROUP BY " +
            ORDER_ID + ";";

    public void displayUserPurchase(String username) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DISPLAY_USER_PURCHASE)) {
                preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 2) {
                            System.out.printf("%-12s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-15s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 2) {
                                System.out.printf("%-12s", rs.getString(i));
                            } else {
                                System.out.printf("%-15s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }

    }

    public static final String DISPLAY_ALL_SALES = "SELECT " + ORDER_ID + ", " + PURCHASE_DATE +
            ", SUM ( " + QUANTITY + " * " + PRICE + ") AS TOTAL FROM  " + TABLE_SALES + " GROUP BY " +
    ORDER_ID + ";";

    public void displayAllSalesReport() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(DISPLAY_ALL_SALES);
                try (ResultSet rs = statement.executeQuery(DISPLAY_ALL_SALES)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 2) {
                            System.out.printf("%-15s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-12s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 2) {
                                System.out.printf("%-15s", rs.getString(i));
                            } else {
                                System.out.printf("%-12s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private static final String COUNT_ORDERS = "SELECT COUNT (DISTINCT " + ORDER_ID + ") FROM " + TABLE_SALES + ";";

    public int countOrders() {
        int order = 0;
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(COUNT_ORDERS);
                try (ResultSet rs = statement.executeQuery(COUNT_ORDERS)) {
                    while (rs.next()) {
                        order = rs.getInt(1);
                        return order;
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return 0;
    }

    private static final String SUM_TOTAL_PURCHASE = "SELECT SUM( " + QUANTITY + " * " + PRICE +
            ") AS TOTAL FROM " + TABLE_SALES + ";";

    public double sumTotalPurchase() {
        int sales = 0;
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(SUM_TOTAL_PURCHASE);
                try (ResultSet rs = statement.executeQuery(SUM_TOTAL_PURCHASE)) {
                    while (rs.next()) {
                        sales = rs.getInt(1);
                        return sales;
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return 0;
    }

    private static final String BEST_SELLING_PRODUCT = "SELECT " + PRODUCT + ", " +
            " SUM( " + QUANTITY + ") AS TOTAL_QTY FROM " + TABLE_SALES + " GROUP BY " + PRODUCT +
            " ORDER BY TOTAL_QTY DESC LIMIT 3;";

    public void bestSellingProduct() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(BEST_SELLING_PRODUCT);
                try (ResultSet rs = statement.executeQuery(BEST_SELLING_PRODUCT)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 1) {
                            System.out.printf("%-30s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-12s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 1) {
                                System.out.printf("%-30s", rs.getString(i));
                            } else {
                                System.out.printf("%-12s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}

