package view.snapshots;

import view.components.panels.ButtonsPanel;
import view.components.panels.table.TablePanel;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.Locale;

public class TableData extends JPanel {
    private String tableName;

    private TablePanel tablePanel;
    private final ButtonsPanel buttonsPanel;

    private boolean isEditable = true;

    private static final String BACK_BUTTON_NAME = "Назад";
    private static final String EDIT_BUTTON_NAME = "Изменить";
    private static final String CREATE_BUTTON_NAME = "Создать";
    private static final String DELETE_BUTTON_NAME = "Удалить";

    private final JLabel tableNameLabel = new JLabel("");

    public TableData() {
        setLayout(new BorderLayout());

        this.tablePanel = new TablePanel();
        this.buttonsPanel = new ButtonsPanel();

        buttonsPanel.addButton(BACK_BUTTON_NAME);
        buttonsPanel.addButton(EDIT_BUTTON_NAME);
        buttonsPanel.addButton(DELETE_BUTTON_NAME);
        buttonsPanel.addButton(CREATE_BUTTON_NAME);

        add(tablePanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setTitle();
    }

    public List<String> getColumnNames(){
        return tablePanel.getColumnNames();
    }

    public void setEditable(boolean isEditable){
        tablePanel.setSelectionAllowed(isEditable);
        this.isEditable = isEditable;

        JButton createButton = buttonsPanel.getButton(CREATE_BUTTON_NAME);
        createButton.setEnabled(isEditable);

        JButton deleteButton = buttonsPanel.getButton(DELETE_BUTTON_NAME);
        deleteButton.setEnabled(isEditable);
    }

    private void setTitle(){
        tableNameLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20, 0));
        tableNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(tableNameLabel, BorderLayout.NORTH);
    }

    public void addBackButtonListener(ActionListener actionListener) {
        buttonsPanel.addButtonListener(BACK_BUTTON_NAME, actionListener);
    }

    public void addCreateButtonListener(ActionListener actionListener) {
        buttonsPanel.addButtonListener(CREATE_BUTTON_NAME, actionListener);
    }

    public void addDeleteButtonListener(ActionListener actionListener){
        buttonsPanel.addButtonListener(DELETE_BUTTON_NAME, actionListener);
    }

    public void addEditButtonListener(ActionListener actionListener){
        buttonsPanel.addButtonListener(EDIT_BUTTON_NAME, actionListener);
    }

    public void addDataMouseListener(MouseAdapter mouseAdapter){
        tablePanel.addDataMouseListener(mouseAdapter);
    }

    public void clearTable(){
        remove(tablePanel);
        tablePanel = new TablePanel();
        tablePanel.setSelectionAllowed(isEditable);
        add(tablePanel);
    }

    public void addTableListener(TableModelListener tableModelListener){
        tablePanel.addTableListener(tableModelListener);
    }

    public String getColumnName(int index){
        return tablePanel.getColumnName(index);
    }

    public Object getValue(int rowIndex, String columnName){
        return tablePanel.getValue(rowIndex, columnName);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        tableNameLabel.setText(tableName.toUpperCase(Locale.ROOT));
    }

    public void updateRowsCountInLabel(){
        int rowCount = tablePanel.getRowsCount();
        StringBuilder label = new StringBuilder(String.valueOf(rowCount));
        label.append(" ");

        switch (rowCount % 10){
            case 1:
                label.append("запись");
                break;
            case 2:
            case 3:
            case 4:
                label.append("записи");
                break;
            default:
                label.append("записей");
        }
        tableNameLabel.setText(tableName.toUpperCase(Locale.ROOT) + "   |   " + label.toString());
    }

    public TablePanel getTablePanel(){
        return tablePanel;
    }

    public void removeActionListeners(){
        tablePanel.removeListeners();
    }

    public int getSelectedRowIndex(){
        int index = tablePanel.getSelectedRowIndex();
        if (index < 0 || index >= tablePanel.getRowsCount()){
            throw new IllegalStateException();
        }
        return index;
    }

}

