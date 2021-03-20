package sample.controllers.SQL;


import org.sqlite.JDBC;

import java.sql.*;
import java.util.*;

public class DbHandler {

    private static final String CON_STR = "jdbc:sqlite:d:\\IdeaProjects\\University\\src\\sample\\data\\uDb.s3db";

    private static DbHandler instance = null;

    private static Connection connection ;

    private static Statement statement;

    private static ResultSet resultSet;

    public static ResultSet Select(String sql) throws SQLException, ClassNotFoundException {

        DriverManager.registerDriver(new JDBC());
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:d:\\University\\src\\sample\\data\\uDb.s3db");
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);
        return resultSet;
    }//список для справочника

    public static void CloseDB() throws SQLException {
        resultSet.close();
        statement.close();
        connection.close();
    }//закрыть соединение

    public static Statement  Statement() throws SQLException, ClassNotFoundException {
        DriverManager.registerDriver(new JDBC());
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:d:\\University\\src\\sample\\data\\uDb.s3db");
        statement = connection.createStatement();
        return statement;
    }//вернуть соединение

  /*  private DbHandler() throws SQLException {
        // Регистрируем драйвер, с которым будем работать
        // в нашем случае Sqlite
        DriverManager.registerDriver(new JDBC());
        // Выполняем подключение к базе данных
        this.connection = DriverManager.getConnection(CON_STR);
    }*/

    public List<Product> getAllProducts() {

        try (Statement statement = this.getConnection().createStatement()) {
            // В данный список будем загружать наши продукты, полученные из БД
            List<Product> products = new ArrayList<Product>();
            // В resultSet будет храниться результат нашего запроса,
            // который выполняется командой statement.executeQuery()
            ResultSet resultSet = statement.executeQuery("SELECT id, good, price, category_name FROM products");
            // Проходимся по нашему resultSet и заносим данные в products
        /*    while (resultSet.next()) {
                products.add(new Product(resultSet.getInt("id"),
                        resultSet.getString("good"),
                        resultSet.getDouble("price"),
                        resultSet.getString("category_name")));
            }*/
            // Возвращаем наш список
            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            // Если произошла ошибка - возвращаем пустую коллекцию
            return Collections.emptyList();
        }
    }



    // Добавление продукта в БД
    public void addProduct(Product product) {
        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
        try (PreparedStatement statement = this.getConnection().prepareStatement(
                "INSERT INTO Products(`good`, `price`, `category_name`) " +
                        "VALUES(?, ?, ?)")) {
            statement.setObject(1, product.good);
            statement.setObject(2, product.price);
            statement.setObject(3, product.category_name);
            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Удаление продукта по id
    public void deleteProduct(int id) {
        try (PreparedStatement statement = this.getConnection().prepareStatement(
                "DELETE FROM Products WHERE id = ?")) {
            statement.setObject(1, id);
            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static   Connection getConnection()  {
         return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}