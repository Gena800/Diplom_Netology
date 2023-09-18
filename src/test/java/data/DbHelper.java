package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;


public class DbHelper {

    private DbHelper() {
    }

    @SneakyThrows
    public static Connection getConnection() {
        var dbUrl = System.getProperty("db.url");
        var dbUserName = System.getProperty("db.user");
        var dbPassword = System.getProperty("db.pass");
        return DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
    }

    @SneakyThrows
    public static void cleanTables() {
        var payment = "DELETE FROM payment_entity;";
        var credit = "DELETE FROM credit_request_entity;";
        var order = "DELETE FROM order_entity;";
        var runner = new QueryRunner();
        try (var connection = getConnection();
        ) {
            runner.update(connection, payment);
            runner.update(connection, credit);
            runner.update(connection, order);
        }
    }

    @SneakyThrows
    public static String getCreditStatusDB() {
        var status = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();
        String creditStatus;

        try (var connection = getConnection();
        ) {
            creditStatus = runner.query(connection, status, new ScalarHandler<>());
        }
        return creditStatus;
    }

    @SneakyThrows
    public static String getPaymentStatusDB() {
        var status = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();
        String payStatus;

        try (var connection = getConnection();
        ) {
            payStatus = runner.query(connection, status, new ScalarHandler<>());
        }

        return payStatus;
    }

    @SneakyThrows
    public static long getPaymentCount() {
        var count = "SELECT COUNT(id) as count FROM payment_entity;";
        var runner = new QueryRunner();
        long payCount;

        try (
                var connection = getConnection();
        ) {
            payCount = runner.query(connection, count, new ScalarHandler<>());
        }
        return payCount;
    }

    @SneakyThrows
    public static long getCreditCount() {
        var count = "SELECT COUNT(id) as count FROM credit_request_entity;";
        var runner = new QueryRunner();
        long creditCount;

        try (var connection = getConnection();
        ) {
            creditCount = runner.query(connection, count, new ScalarHandler<>());
        }
        return creditCount;
    }

    @SneakyThrows
    public static long getOrderCount() {
        var count = "SELECT COUNT(*) FROM order_entity;";
        var runner = new QueryRunner();
        long orderCount;

        try (var connection = getConnection();
        ) {
            orderCount = runner.query(connection, count, new ScalarHandler<>());
        }
        return orderCount;
    }

    @SneakyThrows
    public static long getOrderCountCreditId() {
        var count = "SELECT COUNT(credit_id)  FROM order_entity;";
        var runner = new QueryRunner();
        long orderCount;

        try (var connection = getConnection();
        ) {
            orderCount = runner.query(connection, count, new ScalarHandler<>());
        }
        return orderCount;
    }

    @SneakyThrows
    public static long getOrderCountPaymentId() {
        var count = "SELECT COUNT(payment_id)  FROM order_entity;";
        var runner = new QueryRunner();
        long orderCount;

        try (var connection = getConnection();
        ) {
            orderCount = runner.query(connection, count, new ScalarHandler<>());
        }
        return orderCount;
    }

}