package connectivity;

import model.properties.PropertyType;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DataAccessObject {
    private final DatabaseConnectivity connectivity;
    private static final String CREATION_STATEMENTS_FILE_NAME = "/create_statements.xml";
    private static final String INSERT_STATEMENTS_FILE_NAME = "/insert_statements.xml";
    private static final String QUERIES_FILE_NAME = "/queries.xml";
    private static final String SUPPORT_ELEMENTS_FILE_NAME = "/support_elements.xml";

    private final Properties creationStatements;
    private final Properties insertStatements;
    private final Properties queries;
    private final Properties supportElements;

    public DataAccessObject(ConnectionProperties properties) throws SQLException, IOException {
        this.connectivity = new DatabaseConnectivity(properties);
        this.creationStatements = getProperties(CREATION_STATEMENTS_FILE_NAME);
        this.insertStatements = getProperties(INSERT_STATEMENTS_FILE_NAME);
        this.queries = getProperties(QUERIES_FILE_NAME);
        this.supportElements = getProperties(SUPPORT_ELEMENTS_FILE_NAME);
    }

    public ResultSet getTableData(String tableName, List<String> targetColumns) throws SQLException {
        String sqlCode = SQLQueryCreator.getQuery(tableName, targetColumns).toString();
        return connectivity.executeQuery(sqlCode);
    }

    public ResultSet getFilteredTableData(String tableName, List<String> targetColumns, Map<String, String> identifyingValues) throws SQLException {
        StringBuilder sqlCode = SQLQueryCreator.getQuery(tableName, targetColumns);
        SQLQueryCreator.addConditions(sqlCode, identifyingValues);
        return connectivity.executeQuery(sqlCode.toString());
    }

    public ResultSet getQueryResult(String queryName, Map<String, String> variablesValues, List<String> unusedVariables) throws SQLException {
        String sourceSqlCode = queries.getProperty(queryName);
        String sql = SQLQueryCreator.getFilterConditions(sourceSqlCode, unusedVariables);

        return connectivity.executeHandmadeStatement(sql, variablesValues);
    }

    public void updateCellValue(String tableName, String columnName,
                                String idColumnName, int idValue, String newCellValue) throws SQLException {
        StringBuilder sqlCode = SQLQueryCreator.getUpdateValueStatement(tableName, columnName, newCellValue);

        SQLQueryCreator.addCondition(sqlCode, idColumnName + " = " + idValue);

        try {
            connectivity.executeUpdateStatement(sqlCode.toString());
            connectivity.commit();
        } catch (SQLException exception) {
            connectivity.rollback();
            throw exception;
        }
    }

    public void closeConnection() {
        connectivity.closeConnection();
    }

    public void insertRow(String tableName, Map<String, String> values) throws SQLException {
        String sqlCode = SQLQueryCreator.getInsertRowStatement(tableName, values);
        try {
            connectivity.executeUpdateStatement(sqlCode);
            connectivity.commit();
        } catch (SQLException exception) {
            connectivity.rollback();
            throw exception;
        }
    }

    public void insertRowWithoutCommit(String tableName, Map<String, String> values) throws SQLException {
        String sqlCode = SQLQueryCreator.getInsertRowStatement(tableName, values);
        connectivity.executeUpdateStatement(sqlCode);
    }

    public void updateRow(String tableName, Map<String, String> values, String idColumnName, int rowID) throws SQLException {
        StringBuilder sqlCode = SQLQueryCreator.getUpdateValueStatement(tableName, values);
        SQLQueryCreator.addCondition(sqlCode, idColumnName + " = " + rowID);
        connectivity.executeUpdateStatement(sqlCode.toString());
    }
    public void updateRow(String tableName, Map<String, String> values, Map<String, String> identifyingValues) throws SQLException {
        StringBuilder sqlCode = SQLQueryCreator.getUpdateValueStatement(tableName, values);
        SQLQueryCreator.addConditions(sqlCode, identifyingValues);
        connectivity.executeUpdateStatement(sqlCode.toString());
    }

    public void commit() throws SQLException {
        connectivity.commit();
    }

    public void rollback() {
        try {
            connectivity.rollback();
        } catch (SQLException ignored){
        }
    }

    public int getLastInsertedID(String tableName, String IDColumnName) throws SQLException {
        String sqlCode = SQLQueryCreator.getMaxIDQuery(tableName, IDColumnName);
        ResultSet resultSet = connectivity.executeQuery(sqlCode);
        resultSet.next();
        return resultSet.getInt("ID");
    }

    public void deleteRow(String tableName, String idColumnName, int id) throws SQLException {
        StringBuilder sqlCode = SQLQueryCreator.getDeleteRowStatement(tableName);
        SQLQueryCreator.addCondition(sqlCode, idColumnName + " = " + id);

        try {
            connectivity.executeUpdateStatement(sqlCode.toString());
            connectivity.commit();
        } catch (SQLException exception) {
            connectivity.rollback();
            throw exception;
        }
    }

    public void deleteRow(String tableName, Map<String, String> identifyingValues) throws SQLException {
        StringBuilder sqlCode = SQLQueryCreator.getDeleteRowStatement(tableName);
        SQLQueryCreator.addConditions(sqlCode, identifyingValues);

        try {
            connectivity.executeUpdateStatement(sqlCode.toString());
            connectivity.commit();
        } catch (SQLException exception) {
            connectivity.rollback();
            throw exception;
        }
    }

    public void createSupportElements(List<String> elementsNames) throws SQLException {
        for (String name : elementsNames) {
            String sqlStatement = supportElements.getProperty(name);
            try {
                connectivity.executeUpdateStatement(sqlStatement);
            } catch (SQLException e) {
                if (e.getErrorCode() != 955) {
                    throw e;
                }

                String dropSqlStatement = supportElements.getProperty("Drop_" + name);
                connectivity.executeUpdateStatement(dropSqlStatement);
                connectivity.executeUpdateStatement(sqlStatement);
            }
        }
    }

    public void createTables(List<String> tablesNames) throws SQLException {
        for (String tableName : tablesNames) {
            String sqlStatement = creationStatements.getProperty(tableName);
            connectivity.executeUpdateStatement(sqlStatement);
        }
    }

    public void deleteTables(List<String> tablesNames) throws SQLException {
        for (int i = tablesNames.size() - 1; i >= 0; i--) {
            String name = tablesNames.get(i);
            String sqlStatement = SQLQueryCreator.getDropTableStatement(name);
            connectivity.executeUpdateStatement(sqlStatement);
        }
    }

    public void fillTables(List<String> tablesNames) throws SQLException {
        try {
            for (String tableName : tablesNames) {
                String sqlStatement = insertStatements.getProperty(tableName);
                if (!sqlStatement.equals("")) {
                    connectivity.executeUpdateStatement(sqlStatement);
                }
            }
            connectivity.commit();
        } catch (SQLException exception) {
            connectivity.rollback();
            throw exception;
        }
    }

    private Properties getProperties(String propertyName) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(propertyName)) {
            Properties prop = new Properties();
            prop.loadFromXML(inputStream);
            return prop;
        }
    }

    public Map<Integer, List<String>> getRepresentations(String tableName, List<String> columns, String idColumnName) throws SQLException {
        List<String> neededValues = new ArrayList<>();
        neededValues.add(idColumnName);
        neededValues.addAll(columns);

        StringBuilder sqlCode = SQLQueryCreator.getQuery(tableName, neededValues);
        ResultSet resultSet = connectivity.executeQuery(sqlCode.toString());

        Map<Integer, List<String>> representations = new HashMap<>();

        while (resultSet.next()) {
            List<String> representation = new ArrayList<>();
            for (String columnName : columns) {
                representation.add(resultSet.getString(columnName));
            }
            int id = resultSet.getInt(idColumnName);
            representations.put(id, representation);
        }
        return representations;
    }
}
