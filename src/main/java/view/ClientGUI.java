package view;

import model.service.ServiceType;
import presenter.ErrorHandler;
import view.components.frames.MainFrame;
import view.snapshots.*;
import view.snapshots.common.Selection;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class ClientGUI implements ErrorHandler {
    private final IPresenter presenter;

    private static final String MAIN_FRAME_NAME = "Database Client";
    private static final double FRAME_WIDTH_PROPORTION = 3;
    private static final double FRAME_HEIGHT_PROPORTION = 2;
    private static final double FRAME_SCALE = 0.7;
    private static final List<Integer> FORM_COLUMNS_PROPORTION = Arrays.asList(20, 70, 10);
    private static final List<Integer> MENU_COLUMNS_PROPORTION = Arrays.asList(30, 40, 30);

    private static final String LOGIN_FORM_PANEL_NAME = "Login Form";
    private static final String TABLE_SELECTION_PANEL_NAME = "Table Selection";
    private static final String QUERY_SELECTION_PANEL_NAME = "Query Selection";
    private static final String SERVICE_SELECTION_PANEL_NAME = "Service Selection";
    private static final String SERVICE_FORM_PANEL_NAME = "Service Form";
    private static final String TABLE_DATA_PANEL_NAME = "Table Data";
    private static final String TABLE_PRESENTATION_PANEL_NAME = "Table Presentation";
    private static final String CREATION_ENTRY_FORM_PANEL_NAME = "Creation Entry Form";
    private static final String EDITING_ENTRY_FORM_PANEL_NAME = "Editing Entry Form";
    private static final String MAIN_MENU_PANEL_NAME = "Main menu";
    private static final String CONFIG_MENU_PANEL_NAME = "Config menu";
    private static final String QUERY_FILTER_PANEL_NAME = "Query filter";

    private final MainFrame mainFrame = new MainFrame(MAIN_FRAME_NAME, FRAME_WIDTH_PROPORTION, FRAME_HEIGHT_PROPORTION, FRAME_SCALE);

    private final LoginForm loginForm = new LoginForm(FORM_COLUMNS_PROPORTION);
    private final Selection tableSelection = new Selection("ТАБЛИЦЫ", "Показать таблицу");
    private final TableData tableData = new TableData();
    private final TablePresentation tablePresentation = new TablePresentation();
    private final FilteringEntryForm creationEntryForm = new FilteringEntryForm(FORM_COLUMNS_PROPORTION, "Создать");
    private final FilteringEntryForm editingEntryFrom = new FilteringEntryForm(FORM_COLUMNS_PROPORTION, "Изменить", false);
    private final MainMenu mainMenu = new MainMenu(MENU_COLUMNS_PROPORTION);
    private final ServerConfigMenu configMenu = new ServerConfigMenu(MENU_COLUMNS_PROPORTION);
    private final Selection querySelection = new Selection("ЗАПРОСЫ", "Показать запрос");
    private final FilteringEntryForm queryFilterForm = new FilteringEntryForm(FORM_COLUMNS_PROPORTION, "Применить");
    private final Selection serviceSelection = new Selection("ИНСТРУМЕНТЫ", "Open service");
    private final FilteringEntryForm serviceForm = new FilteringEntryForm(FORM_COLUMNS_PROPORTION, "Выполнить");

    private Set<String> readOnlyTables;
    private static Set<String> rolesCanConfigServer = new HashSet<>();

    public ClientGUI(IPresenter presenter) {
        this.presenter = presenter;

        mainFrame.addPanel(loginForm, LOGIN_FORM_PANEL_NAME);
        mainFrame.addPanel(tableSelection, TABLE_SELECTION_PANEL_NAME);
        mainFrame.addPanel(tableData, TABLE_DATA_PANEL_NAME);
        mainFrame.addPanel(creationEntryForm, CREATION_ENTRY_FORM_PANEL_NAME);
        mainFrame.addPanel(editingEntryFrom, EDITING_ENTRY_FORM_PANEL_NAME);
        mainFrame.addPanel(mainMenu, MAIN_MENU_PANEL_NAME);
        mainFrame.addPanel(configMenu, CONFIG_MENU_PANEL_NAME);
        mainFrame.addPanel(querySelection, QUERY_SELECTION_PANEL_NAME);
        mainFrame.addPanel(tablePresentation, TABLE_PRESENTATION_PANEL_NAME);
        mainFrame.addPanel(queryFilterForm, QUERY_FILTER_PANEL_NAME);
        mainFrame.addPanel(querySelection, QUERY_SELECTION_PANEL_NAME);
        mainFrame.addPanel(serviceSelection, SERVICE_SELECTION_PANEL_NAME);
        mainFrame.addPanel(serviceForm, SERVICE_FORM_PANEL_NAME);

        mainFrame.showPanel(LOGIN_FORM_PANEL_NAME);
        addWindowClosingListener();

        addLoginFormListeners();
        addTableSelectionListeners();
        addTableDataListeners();
        addCreationEntryFormListeners();
        addMainMenuListeners();
        addQuerySelectionListeners();
        addServerConfigListeners();
        addQueryPresentationListeners();
        addQueryFilterListeners();
        addServiceSelectionListener();
        addServiceFormListener();
        addEditingEntryFromListeners();

        rolesCanConfigServer.add("Администратор");
    }

    private boolean login() {
        String hostname = loginForm.getHostname();
        int port = loginForm.getPort();
        String login = loginForm.getLogin();
        String password = loginForm.getPassword();
        String role = loginForm.getRole();

        mainMenu.setConfigEnabled(rolesCanConfigServer.contains(role));

        return presenter.login(hostname, port, login, password, role);
    }

    private void getTemplateLoginForm() {
        loginForm.setHostName("84.237.50.81");
        loginForm.setPort(1521);
        loginForm.setLogin("18201_KONONOV");
        loginForm.setPassword("nitvo8-siqgaq-coKfah");
    }

    private void logout() {
        presenter.logout();
    }

    private void prepareTableSelection() {
        List<String> tablesNames = presenter.getAvailableTablesTitles();
        tableSelection.addListElements(tablesNames);
        readOnlyTables = presenter.getReadOnlyTablesTitles();
    }

    private void prepareQuerySelection() {
        List<String> queriesNames = presenter.getAvailableQueriesNames();
        querySelection.addListElements(queriesNames);
    }

    private void prepareServiceSelection() {
        List<String> serviceNames = presenter.getAvailableServiceNames();
        serviceSelection.addListElements(serviceNames);
    }

    private void createTablesOnServer() {
        presenter.createTables();
    }

    private void fillTablesOnServer() {
        presenter.fillTables();
    }

    private void deleteTablesOnServer() {
        presenter.deleteTables();
    }

    private void showTableSelection() {
        mainFrame.showPanel(TABLE_SELECTION_PANEL_NAME);
    }

    private void showQuerySelection() {
        mainFrame.showPanel(QUERY_SELECTION_PANEL_NAME);
    }

    private void showLoginForm() {
        mainFrame.showPanel(LOGIN_FORM_PANEL_NAME);
    }

    private void showTableData() {
        mainFrame.showPanel(TABLE_DATA_PANEL_NAME);
    }

    private void showTablePresentation() {
        mainFrame.showPanel(TABLE_PRESENTATION_PANEL_NAME);
    }

    private void showCreationEntryForm() {
        mainFrame.showPanel(CREATION_ENTRY_FORM_PANEL_NAME);
    }

    private void showEditingEntryForm() {
        mainFrame.showPanel(EDITING_ENTRY_FORM_PANEL_NAME);
    }

    private void showServiceForm() {
        mainFrame.showPanel(SERVICE_FORM_PANEL_NAME);
    }

    private void showMainMenu() {
        mainFrame.showPanel(MAIN_MENU_PANEL_NAME);
    }

    private void showServerConfigMenu() {
        mainFrame.showPanel(CONFIG_MENU_PANEL_NAME);
    }

    private void showQueryFilterForm() {
        mainFrame.showPanel(QUERY_FILTER_PANEL_NAME);
    }

    private void showServiceSelection() {
        mainFrame.showPanel(SERVICE_SELECTION_PANEL_NAME);
    }

    private void prepareTablePanel(String selectedTableName, boolean isReadOnly) {
        tableData.clearTable();
        tableData.setTableName(selectedTableName);
        tableData.setEditable(!isReadOnly);
        presenter.prepareTablePanel(selectedTableName, tableData.getTablePanel());
    }

    private void prepareQueryPanel(String selectedQueryName) {
        tablePresentation.clearTable();
        tablePresentation.setTableName(selectedQueryName);
        presenter.prepareQueryPanel(selectedQueryName, tablePresentation.getTablePanel());
    }

    private void prepareInsertEntryForm(String selectedTableName) {
        presenter.prepareEntryForm(selectedTableName, creationEntryForm.getEntryFormPanel());
        creationEntryForm.addCheckBoxes(true);
    }

    private void prepareEditingEntryForm(String selectedTableName) {
        presenter.prepareEntryForm(selectedTableName, editingEntryFrom.getEntryFormPanel());
        editingEntryFrom.addCheckBoxes(true);
    }

    private void prepareQueryFilterForm(String selectedQueryName) {
        presenter.prepareQueryFilterPanel(selectedQueryName, queryFilterForm.getEntryFormPanel());
        queryFilterForm.addCheckBoxes(false);
    }

    private void prepareServiceForm(String selectedServiceName) {
        presenter.prepareServiceForm(selectedServiceName, serviceForm.getEntryFormPanel());
        serviceForm.addCheckBoxes(true);
    }


    private boolean fillTableData(String selectedTableName) {
        boolean isFilled = presenter.fillTablePanel(selectedTableName, tableData.getTablePanel());
        if (isFilled) {
            tableData.updateRowsCountInLabel();
        }
        return isFilled;
    }

    private boolean fillQueryTableData(String selectedQueryName) {
        Map<String, Object> queryVariables = queryFilterForm.getFilteredValuesTuple();
        boolean isFilled = presenter.fillQueryPanel(selectedQueryName, queryVariables, tablePresentation.getTablePanel());
        if (isFilled) {
            tablePresentation.updateRowsCountInLabel();
        }
        return isFilled;
    }

    private void removeSelectedRow() {
        int rowIndex;
        try {
            rowIndex = tableData.getSelectedRowIndex();
            String selectedTableName = tableData.getTableName();
            Map<String, String> identifyingValues = getIdentifyingValues(selectedTableName, rowIndex);

            disableTableListener();
            presenter.removeRow(selectedTableName, identifyingValues);
            refreshTablePanel();
            enableTableListener();
        } catch (IllegalStateException e) {
            showMsg("Cannot remove row! Please select at least on element");
        }
    }

    private void updateRowValue(){
        int rowIndex;
        try {
            String tableName = tableData.getTableName();

            rowIndex = tableData.getSelectedRowIndex();
            String selectedTableName = tableData.getTableName();
            Map<String, String> identifyingValues = getIdentifyingValues(selectedTableName, rowIndex);
            Map<String, Object> values = editingEntryFrom.getFilteredValuesTuple();

            boolean isUpdated = presenter.updateRow(tableName, values, identifyingValues);
            if(isUpdated){
                refreshTablePanel();
                showTableData();
            }

        } catch (IllegalStateException e) {
            showMsg("Cannot remove row! Please select at least on element");
        }
    }

    private boolean prepareEditingForm(){
        int rowIndex;
        try {
            rowIndex = tableData.getSelectedRowIndex();
            String selectedTableName = tableData.getTableName();
            Map<String, String> identifyingValues = getIdentifyingValues(selectedTableName, rowIndex);

            presenter.fillEntryPanel(selectedTableName, editingEntryFrom.getExistingEntryFormPanel(), identifyingValues);
            return true;
        } catch (IllegalStateException e) {
            showMsg("Cannot edit row! Please select at least on element");
            return false;
        }
    }

    Map<String, String>  getIdentifyingValues(String selectedTableName, int rowIndex){
        Map<String, String> identifyingValues = new HashMap<>();
        String idColumnName = presenter.getIDColumnTitle(selectedTableName);
        if (idColumnName != null){
            Integer idValue = (Integer) tableData.getValue(rowIndex, idColumnName);
            identifyingValues.put(idColumnName, idValue.toString());
        } else {
            List<String> columnTitles = tableData.getColumnNames();
            for(String columnTitle : columnTitles){
                String val = tableData.getValue(rowIndex, columnTitle).toString();
                identifyingValues.put(columnTitle, val);
            }
        }
        return identifyingValues;
    }


    private void updateCellValue(int columnIndex, int rowIndex) {
        String columnName = tableData.getColumnName(columnIndex);
        Object cellValue = tableData.getValue(rowIndex, columnName);
        String tableName = tableData.getTableName();
        String idColumnName = presenter.getIDColumnTitle(tableName);
        Integer idValue = (Integer) tableData.getValue(rowIndex, idColumnName);

        disableTableListener();
        presenter.updateCellValue(tableName, columnName, idValue, cellValue);
        refreshTablePanel();
        enableTableListener();
    }

    private void refreshTablePanel() {
        String tableName = tableData.getTableName();
        presenter.refreshTablePanel(tableName, tableData.getTablePanel());
        tableData.updateRowsCountInLabel();
    }

    private void clearEntryForm() {
        creationEntryForm.clearInputData();
    }

    private void clearServiceForm() {
        serviceForm.clearInputData();
    }

    private boolean addNewRow() {
        String selectedTableName = tableData.getTableName();
        Map<String, Object> newRowValues = creationEntryForm.getFilteredValuesTuple();
        return presenter.insertRow(selectedTableName, newRowValues);
    }

    private void addLoginFormListeners() {
        loginForm.addLoginButtonListener(event -> {
            boolean isLogged = login();
            if (isLogged) {
                showMainMenu();
            }
        });
        loginForm.addFillingButtonListener(event -> getTemplateLoginForm());
    }

    private void addMainMenuListeners() {
        mainMenu.addLogoutButtonListener(event -> {
            logout();
            showLoginForm();
        });
        mainMenu.addShowTablesListener(event -> {
            prepareTableSelection();
            showTableSelection();
        });
        mainMenu.addShowQueryListener(event -> {
            prepareQuerySelection();
            showQuerySelection();
        });
        mainMenu.addShowServiceListener(event -> {
            prepareServiceSelection();
            showServiceSelection();
        });
        mainMenu.addConfigButtonListener(event -> showServerConfigMenu());
    }

    private void addServerConfigListeners() {
        configMenu.addBackButtonListener(event -> showMainMenu());

        configMenu.addCreateButtonListener(event -> createTablesOnServer());
        configMenu.addFillButtonListener(event -> fillTablesOnServer());
        configMenu.addDeleteButtonListener(event -> deleteTablesOnServer());
    }

    private void selectTable() {
        String selectedTableName;
        try {
            selectedTableName = tableSelection.getSelectedElement();
        } catch (RuntimeException exception) {
            showMsg("Select a table name");
            return;
        }

        boolean isReadOnly = readOnlyTables.contains(selectedTableName);
        prepareTablePanel(selectedTableName, isReadOnly);

        if (!isReadOnly) {
            prepareInsertEntryForm(selectedTableName);
            prepareEditingEntryForm(selectedTableName);
        }

        boolean isFilled = fillTableData(selectedTableName);
        if (isFilled) {
            enableTableListener();
            showTableData();
        }
    }

    private void selectQuery() {
        String selectedQueryName;
        try {
            selectedQueryName = querySelection.getSelectedElement();
        } catch (RuntimeException exception) {
            showMsg("Select a query name");
            return;
        }

        prepareQueryPanel(selectedQueryName);
        prepareQueryFilterForm(selectedQueryName);
        boolean isFilled = fillQueryTableData(selectedQueryName);
        if (isFilled) {
            showTablePresentation();
        }
    }

    private void selectService() {
        String selectedServiceName;
        try {
            selectedServiceName = serviceSelection.getSelectedElement();
        } catch (RuntimeException exception) {
            showMsg("Select a service name");
            return;
        }

        ServiceType type = presenter.getServiceType(selectedServiceName);
        switch (type) {
            case INSERT:
                serviceForm.setSelectedName(selectedServiceName);
                prepareServiceForm(selectedServiceName);
                showServiceForm();
                return;
            case DELETE:
                boolean isExecuted = presenter.executeServiceDelete(selectedServiceName);
                if (isExecuted) {
                    showMsg(selectedServiceName + "\n successfully executed");
                }
        }
    }

    private void refreshQueryTablePanel() {
        String selectedQueryName = tablePresentation.getTableName();
        tablePresentation.clearTableContent();
        boolean isFilled = fillQueryTableData(selectedQueryName);
        if (isFilled) {
            showTablePresentation();
        }
    }

    private void addTableSelectionListeners() {
        tableSelection.addBackButtonListener(event -> showMainMenu());
        tableSelection.addShowButtonListener(event -> selectTable());

        tableSelection.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    selectTable();
                }
            }
        });
    }

    private void startEntryEditing(){
        boolean isFormLoaded = prepareEditingForm();
        if(isFormLoaded){
            showEditingEntryForm();
        }
    }

    private void addTableDataListeners() {
        tableData.addBackButtonListener(event -> showTableSelection());
        tableData.addCreateButtonListener(event -> showCreationEntryForm());
        tableData.addDeleteButtonListener(event -> removeSelectedRow());
        tableData.addEditButtonListener(event -> startEntryEditing());
    }

    private void enableTableListener() {
//        tableData.addTableListener(event -> updateCellValue(event.getColumn(), event.getFirstRow()));
    }

    private void disableTableListener() {
//        tableData.removeActionListeners();
    }

    private void addCreationEntryFormListeners() {
        creationEntryForm.addBackButtonListener(event -> {
            clearEntryForm();
            showTableData();
        });

        creationEntryForm.addApplyButtonListener(event -> {
            boolean isAdded = addNewRow();

            if (isAdded) {
                disableTableListener();
                refreshTablePanel();
                enableTableListener();
                showTableData();
            }
        });
    }

    private void addEditingEntryFromListeners(){
        editingEntryFrom.addApplyButtonListener(event -> updateRowValue());

        editingEntryFrom.addBackButtonListener(event -> showTableData());
    }


    private void addQuerySelectionListeners() {
        querySelection.addBackButtonListener(event -> showMainMenu());

        querySelection.addShowButtonListener(event -> selectQuery());

        querySelection.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    selectQuery();
                }
            }
        });
    }

    private void addQueryPresentationListeners() {
        tablePresentation.addBackButtonListener(event -> showQuerySelection());

        tablePresentation.addFilterButtonListener(event -> showQueryFilterForm());
    }

    private void addQueryFilterListeners() {
        queryFilterForm.addApplyButtonListener(event -> refreshQueryTablePanel());

        queryFilterForm.addBackButtonListener(event -> showTablePresentation());
    }

    private void addWindowClosingListener() {
        mainFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    presenter.logout();
                } catch (Exception ignored) {
                }

                System.exit(0);
            }
        });
    }

    private void addServiceSelectionListener() {
        serviceSelection.addBackButtonListener(event -> showMainMenu());

        serviceSelection.addShowButtonListener(event -> selectService());

        serviceSelection.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    selectService();
                }
            }
        });
    }

    private void addServiceFormListener() {

        serviceForm.addBackButtonListener(event -> showServiceSelection());

        serviceForm.addApplyButtonListener(event -> {
            String serviceName = serviceForm.getSelectedName();
            Map<String, Object> values = serviceForm.getFilteredValuesTuple();
            presenter.executeServiceInsert(serviceName, values);
        });
    }

    @Override
    public void showMsg(String message) {
        JOptionPane.showMessageDialog(mainFrame, message);
    }

}
