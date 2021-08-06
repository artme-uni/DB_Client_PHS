package presenter;

import connectivity.ConnectionProperties;
import connectivity.DataAccessObject;
import phs.DatabasePHS;
import model.database.DatabaseModel;
import model.properties.PropertiesBundle;
import model.properties.PropertyType;
import phs.QueriesPHS;
import model.queries.QueriesModel;
import phs.ServicePHS;
import model.service.ServiceInfo;
import model.service.ServiceModel;
import model.service.ServiceType;
import phs.SupportElementsPHS;
import model.support.SupportElementsModel;
import view.IPresenter;
import view.components.panels.forms.EntryFormPanel;
import view.components.panels.table.TablePanel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Presenter implements IPresenter {
    private final ArrayList<ErrorHandler> errorHandlers = new ArrayList<>();
    private DataAccessObject dao;
    private String clientRole;

    private final DatabaseModel databaseModel;
    private final QueriesModel queriesModel;
    private final SupportElementsModel supportElementsModel;
    private final ServiceModel serviceModel;

    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String DATE_TIME_FORMAT_SQL = "dd-MM-yyyy hh:mi:ss";
    private static final String DATE_TIME_FORMAT_JAVA = "dd-MM-yyyy hh:mm:ss";

    private final TitleConverter titleConverter = new TitleConverter();

    private static final String TABLE_NAMESPACE = "Table namespace";

    public Presenter() {
        this.databaseModel = new DatabasePHS().getModel();
        this.queriesModel = new QueriesPHS().getModel();
        this.supportElementsModel = new SupportElementsPHS().getModel();
        this.serviceModel = new ServicePHS().getModel();

        titleConverter.addTitles(TABLE_NAMESPACE, databaseModel.getTablesTitles());
        initializeTableTitles();
        initializeQueriesTitles();
        initializeServiceTitles();
    }

    private void initializeTableTitles() {
        List<String> names = databaseModel.getAvailableTables();

        for (String tableName : names) {
            PropertiesBundle tableProperties = databaseModel.getTable(tableName);
            titleConverter.addTitles(tableName, tableProperties.getTitles());
        }
    }

    private void initializeQueriesTitles() {
        List<String> names = queriesModel.getAvailableQueriesNames();

        for (String queryName : names) {
            PropertiesBundle queryProperties = queriesModel.getQuery(queryName);
            titleConverter.addTitles(queryName, queryProperties.getTitles());
        }
    }

    private void initializeServiceTitles() {
        List<String> names = serviceModel.getAvailableServicesNames();

        for (String serviceName : names) {
            PropertiesBundle service = serviceModel.getServiceBundle(serviceName);
            titleConverter.addTitles(serviceName, service.getTitles());
        }
    }

    @Override
    public boolean login(String hostname, int port, String login, String password, String role) {
        ConnectionProperties properties = new ConnectionProperties(hostname, port, login, password);
        setClientRole(role);

        try {
            this.dao = new DataAccessObject(properties);
        } catch (SQLException exception) {
            showMessage("Cannot connect to " + hostname + ":" + port, exception.getMessage());
            return false;
        } catch (IOException ioException) {
            showMessage("Cannot create dao :{}", ioException.getMessage());
            ioException.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public List<String> getAvailableTablesTitles() {
        List<String> names = databaseModel.getAvailableTables(clientRole);
        return titleConverter.getTitles(TABLE_NAMESPACE, names);
    }

    @Override
    public Set<String> getReadOnlyTablesTitles() {
        Set<String> names = databaseModel.getReadAccessedTables(clientRole);
        return titleConverter.getTitles(TABLE_NAMESPACE, names);
    }

    @Override
    public List<String> getAvailableQueriesNames() {
        return queriesModel.getAccessedQueriesNames(clientRole);
    }

    @Override
    public List<String> getAvailableServiceNames() {
        return serviceModel.getAccessedServiceNames(clientRole);
    }

    @Override
    public void logout() {
        dao.closeConnection();
    }

    @Override
    public boolean prepareTablePanel(String tableTitle, TablePanel panel) {
        String name = titleConverter.getName(TABLE_NAMESPACE, tableTitle);
        PropertiesBundle tableInfo = databaseModel.getTable(name);
        try {
            prepareTable(tableInfo, panel);
            return true;
        } catch (SQLException exception) {
            showMessage("Cannot prepare table" + tableTitle, exception.getMessage());
            return false;
        }
    }

    @Override
    public void prepareQueryPanel(String queryName, TablePanel panel) {
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);
        try {
            prepareTable(tableInfo, panel);
        } catch (SQLException exception) {
            showMessage("Cannot prepare query table" + queryName, exception.getMessage());
        }
    }

    private void prepareTable(PropertiesBundle tableInfo, TablePanel panel) throws SQLException {
        for (String name : tableInfo.getPropertyNames()) {
            String title = tableInfo.getPropertyTitle(name);
            panel.addColumn(title);
        }
    }

    @Override
    public boolean fillTablePanel(String tableTitle, TablePanel panel) {
        String tableName = titleConverter.getName(TABLE_NAMESPACE, tableTitle);
        PropertiesBundle tableInfo = databaseModel.getTable(tableName);
        List<String> columnNames = tableInfo.getPropertyNames();

        try {
            Map<String, Map<Integer, String>> mappingScheme = getMappingScheme(tableInfo, columnNames);
            ResultSet resultSet = dao.getTableData(tableName, columnNames);
            fillTable(resultSet, panel, tableInfo, mappingScheme);
            panel.setEditable(false);
            return true;
        } catch (SQLException exception) {
            showMessage("Cannot get data from table " + tableName, exception.getMessage());
            return false;
        }
    }

    private Map<String, Map<Integer, String>> getMappingScheme(PropertiesBundle tableInfo, List<String> columnNames) throws SQLException {
        Map<String, Map<Integer, String>> mappingScheme = new HashMap<>();
        for (String columnName : columnNames) {
            PropertyType type = tableInfo.getPropertyType(columnName);
            if (type.equals(PropertyType.REFERENCE)) {
                String referenceTableName = tableInfo.getPropertyReference(columnName);
                mappingScheme.put(columnName, getRecursiveRepresentations(referenceTableName));
            }
        }
        return mappingScheme;
    }

    private Map<String, Integer> reverseMapping(Map<Integer, String> mapping) {
        Map<String, Integer> reversedMapping = new HashMap<>();
        for (Map.Entry<Integer, String> entry : mapping.entrySet()) {
            reversedMapping.put(entry.getValue(), entry.getKey());
        }
        return reversedMapping;
    }


    private void fillTable(ResultSet resultSet,
                           TablePanel tableData,
                           PropertiesBundle tableInfo,
                           Map<String, Map<Integer, String>> mapping) throws SQLException {
        List<String> columnNames = tableInfo.getPropertyNames();

        while (resultSet.next()) {
            List<Object> values = new ArrayList<>();

            for (String name : columnNames) {
                PropertyType columnType = tableInfo.getPropertyType(name);

                values.add(prepareDataToShow(resultSet, name, columnType, mapping.get(name)));
            }

            tableData.addRow(values);
        }
    }

    @Override
    public boolean fillQueryPanel(String queryName, Map<String, Object> queryVariables, TablePanel panel) {
        PropertiesBundle tableInfo = queriesModel.getTable(queryName);
        PropertiesBundle queryInfo = queriesModel.getQuery(queryName);

        Map<String, String> stringQueryVariables = new HashMap<>();

        List<String> usedVariables = new ArrayList<>();

        try {
            for (Map.Entry<String, Object> entry : queryVariables.entrySet()) {
                String propertyName = titleConverter.getName(queryName, entry.getKey());
                usedVariables.add(propertyName);
                PropertyType propertyType = queryInfo.getPropertyType(propertyName);
                stringQueryVariables.put(propertyName, prepareDataToSend(entry.getValue().toString(), propertyType));
            }
        } catch (ParseException e) {
            showMessage("Use specified date format " + DATE_FORMAT + " and time format " + DATE_TIME_FORMAT_JAVA, e.getMessage());
            return false;
        }

        try {
            List<String> unusedVariables = queriesModel.getQuery(queryName).getPropertyNames();
            unusedVariables.removeAll(usedVariables);

            ResultSet resultSet = dao.getQueryResult(queryName, stringQueryVariables, unusedVariables);

            List<String> columnNames = tableInfo.getPropertyNames();
            Map<String, Map<Integer, String>> mappingScheme = getMappingScheme(tableInfo, columnNames);
            fillTable(resultSet, panel, tableInfo, mappingScheme);
            return true;
        } catch (SQLException exception) {
            showMessage("Cannot execute " + queryName, exception.getMessage());
            return false;
        }
    }

    @Override
    public void refreshTablePanel(String tableTitle, TablePanel panel) {
        String tableName = titleConverter.getName(TABLE_NAMESPACE, tableTitle);
        panel.clearTable();
        fillTablePanel(tableName, panel);
    }

    @Override
    public void prepareQueryFilterPanel(String queryName, EntryFormPanel panel) {
        PropertiesBundle queryInfo = queriesModel.getQuery(queryName);
        try {
            prepareForm(queryName, queryInfo, panel);
        } catch (SQLException exception) {
            showMessage("Cannot prepare query form for " + queryInfo, exception.getMessage());
        }
    }

    @Override
    public void prepareServiceForm(String serviceName, EntryFormPanel panel) {
        PropertiesBundle serviceInfo = serviceModel.getServiceBundle(serviceName);
        try {
            prepareForm(serviceName, serviceInfo, panel);
        } catch (SQLException exception) {
            showMessage("Cannot prepare service form for " + serviceName, exception.getMessage());
        }
    }

    @Override
    public void prepareEntryForm(String tableTitle, EntryFormPanel panel) {
        try {
            String name = titleConverter.getName(TABLE_NAMESPACE, tableTitle);
            PropertiesBundle tableInfo = databaseModel.getTable(name);
            prepareForm(name, tableInfo, panel);
        } catch (SQLException exception) {
            showMessage("Cannot prepare entry form for " + tableTitle, exception.getMessage());
        }
    }

    private void prepareForm(String metaName, PropertiesBundle info, EntryFormPanel panel) throws SQLException {
        List<String> columnNames = info.getPropertyNames();

        for (String columnName : columnNames) {
            String columnTitle = titleConverter.getTitle(metaName, columnName);
            PropertyType columnType = info.getPropertyType(columnName);

            switch (columnType) {
                case BOOLEAN:
                    panel.addBooleanField(columnTitle);
                    break;
                case ID:
                    break;
                case INTEGER:
                    panel.addNumericField(columnTitle, false);
                    break;
                case REFERENCE:
                    String referencedTableName = info.getPropertyReference(columnName);
                    Map<Integer, String> representations = getRecursiveRepresentations(referencedTableName);
                    panel.addMappedDropdownField(columnTitle, representations);
                    break;
                case DROPDOWN:
                    String[] dropdownValues = info.getDropdownValues(columnName);
                    panel.addDropDownField(columnTitle, dropdownValues);
                    break;
                case DOUBLE:
                    panel.addNumericField(columnTitle, true);
                    break;
                case STRING:
                case DATE:
                case TIME:
                case STR_ARRAY:
                    panel.addTextField(columnTitle);
                    break;
            }
        }
    }

    @Override
    public boolean fillEntryPanel(String tableTitle, EntryFormPanel panel, Map<String, String> identifyingValues) {
        String tableName = titleConverter.getName(TABLE_NAMESPACE, tableTitle);
        List<String> propertiesTitles = panel.getAttributesNames();
        List<String> propertiesNames = new ArrayList<>();

        for (String propertyTitle : propertiesTitles) {
            String propertyName = titleConverter.getName(tableName, propertyTitle);
            propertiesNames.add(propertyName);
        }

        try {
            Map<String, String> preparedValues = new HashMap<>();
            PropertiesBundle info = databaseModel.getTable(tableName);
            Map<String, Map<Integer, String>> mappingScheme = getMappingScheme(info, propertiesNames);

            for (Map.Entry<String, String> entry : identifyingValues.entrySet()) {
                String columnName = titleConverter.getName(tableName, entry.getKey());

                if (identifyingValues.size() == 1 || mappingScheme.get(columnName) == null) {
                    preparedValues.put(columnName, entry.getValue());
                } else {
                    Map<String, Integer> currentMapping = reverseMapping(mappingScheme.get(columnName));
                    Integer columnValue = currentMapping.get(entry.getValue());
                    preparedValues.put(columnName, columnValue.toString());
                }
            }

            ResultSet resultSet = dao.getFilteredTableData(tableName, propertiesNames, preparedValues);
            fillEntryPanel(tableName, resultSet, panel, propertiesNames);
            return true;

        } catch (SQLException e) {
            showMessage("Cannot fill entry form for " + tableTitle, e.getMessage());
            return false;
        }
    }

    private void fillEntryPanel(String tableName, ResultSet resultSet, EntryFormPanel panel, List<String> columnNames) throws SQLException {
        PropertiesBundle info = databaseModel.getTable(tableName);
        Map<String, Map<Integer, String>> mappingScheme = getMappingScheme(info, columnNames);

        resultSet.next();

        for (String columnName : columnNames) {
            PropertyType columnType = info.getPropertyType(columnName);
            Map<Integer, String> currentMappingScheme = mappingScheme.get(columnName);
            Object preparedValue = prepareDataToShow(resultSet, columnName, columnType, currentMappingScheme);

            String columnTitle = titleConverter.getTitle(tableName, columnName);

            switch (columnType) {
                case BOOLEAN:
                    panel.setBoolean(columnTitle, (boolean) preparedValue);
                    break;
                case ID:
                case INTEGER:
                    panel.setInteger(columnTitle, (int) preparedValue);
                    break;
                case REFERENCE:
                case STRING:
                case DATE:
                case TIME:
                case STR_ARRAY:
                case DROPDOWN:
                    panel.setString(columnTitle, preparedValue.toString());
                    break;
                case DOUBLE:
                    panel.setDouble(columnTitle, (double) preparedValue);
                    break;
            }
        }
    }


    @Override
    public String getIDColumnTitle(String tableTitle) {
        String tableName = titleConverter.getName(TABLE_NAMESPACE, tableTitle);
        return titleConverter.getTitle(tableName, getIDColumnName(tableName));
    }

    public String getIDColumnName(String tableName) {
        PropertiesBundle tableInfo = databaseModel.getTable(tableName);
        return tableInfo.getIDPropertyName();
    }

    @Override
    public void updateCellValue(String tableTitle, String columnTitle, int idValue, Object newValue) {
        String tableName = titleConverter.getName(TABLE_NAMESPACE, tableTitle);
        String columnName = titleConverter.getName(tableName, columnTitle);
        try {
            String idColumnName = titleConverter.getName(tableName, getIDColumnTitle(tableName));
            PropertiesBundle tableInfo = databaseModel.getTable(tableName);
            PropertyType columnType = tableInfo.getPropertyType(columnName);
            String preparedNewValue = prepareDataToSend(newValue.toString(), columnType);

            dao.updateCellValue(tableName, columnName, idColumnName, idValue, preparedNewValue);

        } catch (SQLException exception) {
            showMessage("Cannot update value in table " + tableName, exception.getMessage());
        } catch (ParseException exception) {
            showMessage("Date doesnt match format: " + DATE_FORMAT, exception.getMessage());
        }
    }

    @Override
    public boolean insertRow(String tableTitle, Map<String, Object> values) {
        String tableName = titleConverter.getName(TABLE_NAMESPACE, tableTitle);
        try {
            insertRowWithoutCommit(tableName, values);
            dao.commit();
            return true;
        } catch (SQLException exception) {
            showMessage("Cannot insert new row in table " + tableName, exception.getMessage());
            dao.rollback();
        } catch (ParseException exception) {
            showMessage("Date doesnt match format: " + DATE_FORMAT, exception.getMessage());
            dao.rollback();
        }

        return false;
    }

    @Override
    public boolean updateRow(String tableTitle, Map<String, Object> values, int rowID) {
        try {
            String tableName = titleConverter.getName(TABLE_NAMESPACE, tableTitle);
            Map<String, String> preparedValues = prepareFormValues(tableName, values);
            String idColumnName = databaseModel.getTable(tableName).getIDPropertyName();
            dao.updateRow(tableName, preparedValues, idColumnName, rowID);
            return true;
        } catch (ParseException exception) {
            showMessage("Date doesnt match format: " + DATE_FORMAT, exception.getMessage());
            dao.rollback();
            return false;
        } catch (SQLException exception) {
            showMessage("Cannot update row in table " + tableTitle, exception.getMessage());
            dao.rollback();
            return false;
        }
    }

    @Override
    public boolean updateRow(String tableTitle, Map<String, Object> values, Map<String, String> identifyingValues) {
        try {
            String tableName = titleConverter.getName(TABLE_NAMESPACE, tableTitle);
            Map<String, String> preparedValues = prepareFormValues(tableName, values);
            Map<String, String> preparedIdentifyingValues = prepareFormValues(tableName, identifyingValues);
            if (identifyingValues.size() != 1) {
                preparedIdentifyingValues = unmapValues(preparedIdentifyingValues, tableName);
            }

            dao.updateRow(tableName, preparedValues, preparedIdentifyingValues);
            return true;
        } catch (ParseException exception) {
            showMessage("Date doesnt match format: " + DATE_FORMAT, exception.getMessage());
            dao.rollback();
            return false;
        } catch (SQLException exception) {
            showMessage("Cannot update row in table " + tableTitle, exception.getMessage());
            dao.rollback();
            return false;
        }
    }

    private void insertRowWithoutCommit(String tableName, Map<String, Object> values) throws SQLException, ParseException {
        Map<String, String> preparedValues = prepareFormValues(tableName, values);
        dao.insertRowWithoutCommit(tableName, preparedValues);
    }

    private Map<String, String> prepareFormValues(String tableName, Map<String, ?> values) throws ParseException {
        PropertiesBundle tableInfo = databaseModel.getTable(tableName);
        Map<String, String> preparedValues = new HashMap<>();

        for (Map.Entry<String, ?> entry : values.entrySet()) {
            Object value = entry.getValue();
            String columnName = titleConverter.getName(tableName, entry.getKey());

            PropertyType columnType = tableInfo.getPropertyType(columnName);
            String preparedValue = prepareDataToSend(value.toString(), columnType);

            preparedValues.put(columnName, preparedValue);
        }

        return preparedValues;
    }

    private Map<String, String> unmapValues(Map<String, String> values, String tableName) throws SQLException {
        PropertiesBundle tableInfo = databaseModel.getTable(tableName);
        List<String> columnNames = tableInfo.getPropertyNames();
        Map<String, Map<Integer, String>> mappingScheme = getMappingScheme(tableInfo, columnNames);

        Map<String, String> unmappedValues = new HashMap<>();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if(mappingScheme.get(entry.getKey()) == null){
                unmappedValues.put(entry.getKey(), entry.getValue());
            } else {
                Map<String, Integer> unmappingScheme = reverseMapping(mappingScheme.get(entry.getKey()));
                unmappedValues.put(entry.getKey(), unmappingScheme.get(entry.getValue()).toString());
            }
        }
        return unmappedValues;
    }

    @Override
    public void removeRow(String tableTitle, Map<String, String> identifyingValues) {
        String tableName = titleConverter.getName(TABLE_NAMESPACE, tableTitle);
        try {
            Map<String, String> prepared = prepareFormValues(tableName, identifyingValues);
            if (identifyingValues.size() != 1) {
                prepared = unmapValues(prepared, tableName);
            }
            dao.deleteRow(tableName, prepared);
        } catch (SQLException | ParseException exception) {
            showMessage("Cannot delete row in table " + tableName, exception.getMessage());
        }
    }


    @Override
    public void createTables() {
        List<String> tables = databaseModel.getAvailableTables();
        List<String> supportElements = supportElementsModel.getSupportElementsNames();
        try {
            dao.createTables(tables);
            dao.createSupportElements(supportElements);
        } catch (SQLException exception) {
            showMessage("Cannot create tables on server", exception.getMessage());
        }
    }

    @Override
    public void fillTables() {
        List<String> tables = databaseModel.getAvailableTables();
        try {
            dao.fillTables(tables);
        } catch (SQLException exception) {
            showMessage("Cannot fill tables on server", exception.getMessage());
        }
    }

    @Override
    public void deleteTables() {
        List<String> tables = databaseModel.getAvailableTables();
        try {
            dao.deleteTables(tables);
        } catch (SQLException exception) {
            showMessage("Cannot delete tables on server", exception.getMessage());
        }

    }

    @Override
    public void executeServiceInsert(String serviceName, Map<String, Object> values) {
        ServiceInfo serviceInfo = serviceModel.getServiceInfo(serviceName);

        List<String> independentTables = serviceInfo.getIndependentTables();
        try {
            for (String tableName : independentTables) {
                startInsertChain(serviceName, tableName, values, serviceInfo);
            }
            dao.commit();
        } catch (SQLException exception) {
            showMessage("Cannot execute service inserts", exception.getMessage());
            dao.rollback();
        } catch (ParseException exception) {
            showMessage("Date doesnt match format: " + DATE_FORMAT, exception.getMessage());
            dao.rollback();
        }
    }

    @Override
    public ServiceType getServiceType(String serviceName) {
        return serviceModel.getServiceInfo(serviceName).getServiceType();
    }

    @Override
    public boolean executeServiceDelete(String serviceName) {
        ServiceInfo serviceInfo = serviceModel.getServiceInfo(serviceName);

        List<List<String>> deleteTablesChains = new ArrayList<>();

        List<String> independentTables = serviceInfo.getIndependentTables();
        for (String tableName : independentTables) {
            List<String> chain = new ArrayList<>();
            deleteTablesChains.add(chain);

            fillDeleteTablesChain(serviceInfo, tableName, chain);
            System.out.println(chain);
        }

        return true;
    }

    private void fillDeleteTablesChain(ServiceInfo serviceInfo, String tableName, List<String> chain) {
        List<String> dependentTables = serviceInfo.getDependentTables(tableName);
        for (String dependentTableName : dependentTables) {
            chain.add(dependentTableName);
            fillDeleteTablesChain(serviceInfo, tableName, chain);
        }
    }

    private void startInsertChain(String serviceName, String tableName, Map<String, Object> values, ServiceInfo serviceInfo) throws SQLException, ParseException {
        List<String> fields = serviceInfo.getTableFields(tableName);
        Map<String, Object> fieldsValues = values.entrySet().stream()
                .filter(entry -> fields.contains(titleConverter.getName(serviceName, entry.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        insertRowWithoutCommit(tableName, fieldsValues);

        List<String> dependentTables = serviceInfo.getDependentTables(tableName);
        if (dependentTables != null && !dependentTables.isEmpty()) {
            String idColumnName = getIDColumnName(tableName);
            int lastInsertedID = dao.getLastInsertedID(tableName, idColumnName);

            values.put(titleConverter.getTitle(tableName, idColumnName), (double) lastInsertedID);

            for (String dependentTable : dependentTables) {
                startInsertChain(serviceName, dependentTable, values, serviceInfo);
            }
        }
    }

    private String prepareDataToSend(String data, PropertyType type) throws ParseException {
        switch (type) {
            case STRING:
            case DROPDOWN:
                return "'" + data + "'";
            case BOOLEAN:
                boolean isTrue = data.toLowerCase(Locale.UK).equals("true");
                return isTrue ? "1" : "0";
            case DATE:
                DateFormat format = new SimpleDateFormat(DATE_FORMAT);
                Date date = format.parse(data);
                return "TO_DATE('" + format.format(date) + "', '" + DATE_FORMAT + "')";
            case TIME:
                DateFormat timeFormat = new SimpleDateFormat(DATE_TIME_FORMAT_JAVA);
                Date dateTime = timeFormat.parse(data);
                return "TO_DATE('" + timeFormat.format(dateTime) + "', '" + DATE_TIME_FORMAT_SQL + "')";
            case ID:
            case INTEGER:
            case REFERENCE:
            case DOUBLE:
                return data;
            case STR_ARRAY:
                return processStringArr(data);
            default:
                throw new IllegalArgumentException("Not expected case");
        }
    }

    private String processStringArr(String data) {
        StringBuilder result = new StringBuilder();
        String[] valuesArr = data.split(",");
        for (String val : valuesArr) {
            val = val.replace(" ", "");
            if (result.toString().equals("")) {
                result.append("'").append(val).append("'");
                continue;
            }
            result.append(", '").append(val).append("'");
        }
        return result.toString();
    }

    private Object prepareDataToShow(ResultSet resultSet, String columnName, PropertyType type, Map<Integer, String> mapping) throws SQLException {

        Object result = null;
        switch (type) {
            case BOOLEAN:
                int value = resultSet.getInt(columnName);
                result = value > 0;
                break;
            case REFERENCE:
                result = mapping.get(resultSet.getInt(columnName));
                break;
            case ID:
            case INTEGER:
                result = resultSet.getInt(columnName);
                break;
            case DOUBLE:
                result = resultSet.getDouble(columnName);
                break;
            case DATE:
                DateFormat format = new SimpleDateFormat(DATE_FORMAT);
                Date date = resultSet.getDate(columnName);
                if (date != null) {
                    result = format.format(date);
                }
                break;
            case TIME:
                DateFormat timeFormat = new SimpleDateFormat(DATE_TIME_FORMAT_JAVA);
                Date dateTime = resultSet.getDate(columnName);
                if (dateTime != null) {
                    result = timeFormat.format(dateTime);
                }
                break;
            case DROPDOWN:
            case STRING:
            case STR_ARRAY:
                result = resultSet.getString(columnName);
                break;
        }

        if (result == null) {
            return "-";
        }
        return result;
    }

    public Map<Integer, List<String>> getRepresentations(String tableName) throws SQLException {
        PropertiesBundle table = databaseModel.getTable(tableName);
        List<String> columns = table.getRepresentationProperties();
        String idColumnName = table.getIDPropertyName();
        return dao.getRepresentations(tableName, columns, idColumnName);
    }

    public Map<Integer, String> getRecursiveRepresentations(String tableName) throws SQLException {
        Map<Integer, List<String>> representations = getRepresentations(tableName);

        PropertiesBundle table = databaseModel.getTable(tableName);
        List<String> columns = table.getRepresentationProperties();

        for (int i = 0; i < columns.size(); i++) {
            String columnName = columns.get(i);
            PropertyType type = table.getPropertyType(columnName);
            if (type.equals(PropertyType.REFERENCE)) {
                String referencedTable = table.getPropertyReference(columnName);
                Map<Integer, String> innerRepresentation = getRecursiveRepresentations(referencedTable);

                for (Map.Entry<Integer, List<String>> entry : representations.entrySet()) {
                    String oldValue = entry.getValue().get(i);
                    String newValue = innerRepresentation.get(Integer.valueOf(oldValue));
                    entry.getValue().set(i, newValue);
                }
            }
        }

        Map<Integer, String> readyRepresentations = new HashMap<>();
        for (Map.Entry<Integer, List<String>> entry : representations.entrySet()) {
            readyRepresentations.put(entry.getKey(), convertToStringRepresentation(entry.getValue()));
        }

        return readyRepresentations;
    }

    private String convertToStringRepresentation(List<String> stringRepresentation) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringRepresentation.size(); i++) {
            stringBuilder.append(stringRepresentation.get(i));
            if (i != stringRepresentation.size() - 1) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    public void addErrorHandler(ErrorHandler handler) {
        errorHandlers.add(handler);
    }

    public void showMessage(String title, String message) {

        for (ErrorHandler handler : errorHandlers) {
            handler.showMsg(title + "\n" + message);
        }
    }

    public void setClientRole(String role) {
        clientRole = role;
    }

}
