package view.snapshots;

import view.components.panels.ButtonsPanel;
import view.components.panels.table.TablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Locale;

public class TablePresentation extends  JPanel{
    private String tableName;

    private TablePanel tablePanel;
    private final ButtonsPanel buttonsPanel;

    private static final String BACK_BUTTON_NAME = "Назад";
    private static final String FILTER_BUTTON_NAME = "Фильтрация";

    private final JLabel tableNameLabel = new JLabel("");

    public TablePresentation() {
        setLayout(new BorderLayout());

        this.tablePanel = new TablePanel();
        tablePanel.setSelectionAllowed(false);
        this.buttonsPanel = new ButtonsPanel();

        buttonsPanel.addButton(BACK_BUTTON_NAME);
        buttonsPanel.addButton(FILTER_BUTTON_NAME);

        add(tablePanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setTitle();
    }

    private void setTitle(){
        tableNameLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20, 0));
        tableNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(tableNameLabel, BorderLayout.NORTH);
    }

    public void addBackButtonListener(ActionListener actionListener) {
        buttonsPanel.addButtonListener(BACK_BUTTON_NAME, actionListener);
    }

    public void addFilterButtonListener(ActionListener actionListener) {
        buttonsPanel.addButtonListener(FILTER_BUTTON_NAME, actionListener);
    }

    public void clearTable(){
        remove(tablePanel);
        tablePanel = new TablePanel();
        tablePanel.setSelectionAllowed(false);
        add(tablePanel);
    }

    public void clearTableContent(){
        tablePanel.clearTable();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        tableNameLabel.setText(tableName.toUpperCase(Locale.ROOT));
    }

    public TablePanel getTablePanel(){
        return tablePanel;
    }

    public void updateRowsCountInLabel(){
        int rowCount = tablePanel.getRowsCount();
        StringBuilder label = new StringBuilder(String.valueOf(rowCount));
        label.append(" ");

        switch (rowCount){
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

}
