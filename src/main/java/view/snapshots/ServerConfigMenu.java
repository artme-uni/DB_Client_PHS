package view.snapshots;

import view.components.panels.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ServerConfigMenu extends JPanel {
    private final MenuPanel menuPanel;

    private static final String CREATE_TABLES_BUTTON_NAME = "Создать таблицы";
    private static final String FILL_TABLES_BUTTON_NAME = "Заполнить данными";
    private static final String DELETE_TABLES_BUTTON_NAME = "Удалить таблицы";
    private static final String BACK_BUTTON_NAME = "Назад";

    public ServerConfigMenu(List<Integer> columnProportion){
        this.menuPanel = new MenuPanel(columnProportion);

        setLayout(new BorderLayout());

        menuPanel.addSpace();
        menuPanel.addButton(CREATE_TABLES_BUTTON_NAME);
        menuPanel.addButton(FILL_TABLES_BUTTON_NAME);
        menuPanel.addButton(DELETE_TABLES_BUTTON_NAME);
        menuPanel.addButton(BACK_BUTTON_NAME);
        menuPanel.addSpace();

        add(menuPanel, BorderLayout.CENTER);
    }

    public void addCreateButtonListener(ActionListener actionListener) {
        menuPanel.addButtonListener(CREATE_TABLES_BUTTON_NAME, actionListener);
    }

    public void addFillButtonListener(ActionListener actionListener) {
        menuPanel.addButtonListener(FILL_TABLES_BUTTON_NAME, actionListener);
    }

    public void addDeleteButtonListener(ActionListener actionListener) {
        menuPanel.addButtonListener(DELETE_TABLES_BUTTON_NAME, actionListener);
    }

    public void addBackButtonListener(ActionListener actionListener) {
        menuPanel.addButtonListener(BACK_BUTTON_NAME, actionListener);
    }
}
