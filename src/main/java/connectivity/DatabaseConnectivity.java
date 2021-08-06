package connectivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseConnectivity {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectivity.class);
    private final Connection connection;
    private final ConnectionProperties properties;

    public DatabaseConnectivity(ConnectionProperties properties) throws SQLException {

        this.properties = properties;
        this.connection = DriverManager.getConnection(
                properties.getOracleURL(),
                properties.getUserLogin(),
                properties.getPassword());

        connection.setAutoCommit(false);

        logger.info("Connection to {}:{} was created\n", properties.getHostname(), properties.getPort());
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            logExecutedStatement("Query", sql);
            return resultSet;
        } catch (SQLException exception) {
            logExecutionFailure("query", sql, exception.getMessage());
            throw exception;
        }
    }

    public void executeUpdateStatement(String sql) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            logExecutedStatement("Update statement", sql);
        } catch (SQLException exception){
            logExecutionFailure("update statement", sql, exception.getMessage());
            throw exception;
        }
    }

    public void commit() throws SQLException {
        connection.commit();
        logger.info("Commit!");
    }

    public void rollback() throws SQLException {
        connection.rollback();
        logger.warn("Rollback!");
    }

    public ResultSet executePreparedStatement(String sql, Map<String, String> variablesValues) throws SQLException {
        try {
            List<String> variables = new ArrayList<>(variablesValues.keySet());
            sql = SQLQueryCreator.replaceVariables(sql, variables);
            Map<String, Integer> variablesIndexes = SQLQueryCreator.getVariablesIndexes(sql, variables);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (Map.Entry<String, String> entry : variablesValues.entrySet()) {
                int index = variablesIndexes.get(entry.getKey());
                String value = entry.getValue();

                if (value.startsWith("'")) {
                    value = value.substring(1, value.length() - 1);
                }
                preparedStatement.setString(index, value);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            logExecutedStatement("Prepared statement", sql);
            return resultSet;
        } catch (SQLException exception){
            logExecutionFailure("prepared statement", sql, exception.getMessage());
            throw exception;
        }
    }

    public ResultSet executeHandmadeStatement(String sql, Map<String, String> variablesValues) throws SQLException {
            StringBuilder preparedSql = new StringBuilder(sql);

            for (Map.Entry<String, String> entry : variablesValues.entrySet()) {
                int index = preparedSql.indexOf(":" + entry.getKey());
                String value = entry.getValue();
                preparedSql.replace(index, index + entry.getKey().length() + 1, value);
            }

            return executeQuery(preparedSql.toString());
    }


    public void closeConnection() {
        try {
            connection.close();
            logger.info("Connection to {}:{} was closed\n", properties.getHostname(), properties.getPort());
        } catch (SQLException exception) {
            logger.info("Cannot close connection to {}:{}\n", properties.getHostname(), properties.getPort());
            exception.printStackTrace();
        }
    }

    private void logExecutedStatement(String statementType, String sql){
        logger.info("{} was executed:\n{}\n", statementType, sql);
    }

    private void logExecutionFailure(String statementType, String sql, String err){
        logger.error("Cannot execute {}:\n{}\n{}\n", statementType, err, sql);
    }
}
