package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.System.getProperty;

public class SQLHelper {
    private static QueryRunner runner = new QueryRunner();

    private SQLHelper() {

    }

    private static final String url = getProperty("datasource");
    private static final String user = getProperty("user");
    private static final String password = getProperty("password");

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        var getPaymentStatusSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        try (var connection = getConn()) {
            var status = runner.query(connection, getPaymentStatusSQL, new ScalarHandler<String>());
            return status;
        }
    }

    @SneakyThrows
    public static String getCreditStatus() {
        var getCreditStatusSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        try (var connection = getConn()) {
            var creditStatus = runner.query(connection, getCreditStatusSQL, new ScalarHandler<String>());
            return creditStatus;
        }
    }

    @SneakyThrows
    public static String getTransactionId() {
        var getTransactionIdSQL = "SELECT transaction_id  FROM payment_entity ORDER BY created DESC LIMIT 1";
        try (var connection = getConn()) {
            var transactionId = runner.query(connection, getTransactionIdSQL, new ScalarHandler<String>());
            return transactionId;
        }
    }

    @SneakyThrows
    public static String getBankId() {
        var getBankIdSQL = "SELECT bank_id  FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        try (var connection = getConn()) {
            var bankId = runner.query(connection, getBankIdSQL, new ScalarHandler<String>());
            return bankId;
        }
    }

    @SneakyThrows
    public static String getPaymentId() {
        var getPaymentIdSQL = "SELECT payment_id  FROM order_entity ORDER BY created DESC LIMIT 1";
        try (var connection = getConn()) {
            var paymentId = runner.query(connection, getPaymentIdSQL, new ScalarHandler<String>());
            return paymentId;
        }
    }

    @SneakyThrows
    public static String getCreditId() {
        var getPaymentIdSQL = "SELECT credit_id  FROM order_entity ORDER BY created DESC LIMIT 1";
        try (var connection = getConn()) {
            var paymentId = runner.query(connection, getPaymentIdSQL, new ScalarHandler<String>());
            return paymentId;
        }
    }

    @SneakyThrows
    public static String getAmount() {
        var getAmountSQL = "SELECT amount FROM payment_entity ORDER BY created DESC LIMIT 1";
        try (var connection = getConn()) {
            var amountDB = runner.query(connection, getAmountSQL, new ScalarHandler<Integer>());
            Integer.toString(amountDB);
            return String.valueOf(amountDB);
        }
    }

    @SneakyThrows
    public static void dbCleanData() {
        var runner = new QueryRunner();
        var deleteFromOrder_entity = "DELETE FROM order_entity;";
        var deleteFromCredit_request_entity = "DELETE FROM credit_request_entity;";
        var deleteFromPayment_entity = "DELETE FROM payment_entity;";

        try (var connection = getConn()) {
            runner.update(connection, deleteFromOrder_entity);
            runner.update(connection, deleteFromCredit_request_entity);
            runner.update(connection, deleteFromPayment_entity);
        }
    }


}
