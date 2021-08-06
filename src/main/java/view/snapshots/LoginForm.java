package view.snapshots;

import view.components.panels.ButtonsPanel;
import view.components.panels.forms.EntryFormPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginForm extends JPanel{
    private final EntryFormPanel entryForm;
    private final ButtonsPanel buttonsPanel;

    private static final String HOSTNAME_FIELD_NAME = "Имя хоста";
    private static final String PORT_FIELD_NAME = "Порт";
    private static final String LOGIN_FIELD_NAME = "Логин";
    private static final String PASSWORD_FIELD_NAME = "Пароль";
    private static final String ROLES_FIELD_NAME = "Роль";

    private static final String LOGIN_BUTTON_NAME = "Войти";
    private static final String FILLING_BUTTON_NAME = "Заполнить";

    private static final String[] ROLES =
            {"Администратор", "Закупщик", "Менеджер по персоналу", "Бухгалтер"};

    public LoginForm(List<Integer> columnProportion) {
        setLayout(new BorderLayout());

        entryForm = new EntryFormPanel(columnProportion);
        buttonsPanel = new ButtonsPanel();

        entryForm.addClearRow();
        entryForm.addTextField(HOSTNAME_FIELD_NAME);
        entryForm.addNumericField(PORT_FIELD_NAME, false);
        entryForm.addTextField(LOGIN_FIELD_NAME);
        entryForm.addPasswordField(PASSWORD_FIELD_NAME);
        entryForm.addDropDownField(ROLES_FIELD_NAME, ROLES);
        entryForm.addClearRow();

        buttonsPanel.addButton(LOGIN_BUTTON_NAME);
        buttonsPanel.addButton(FILLING_BUTTON_NAME);

        add(entryForm.getPanelContent(), BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public void addLoginButtonListener(ActionListener actionListener){
        buttonsPanel.addButtonListener(LOGIN_BUTTON_NAME, actionListener);
    }

    public void addFillingButtonListener(ActionListener actionListener){
        buttonsPanel.addButtonListener(FILLING_BUTTON_NAME, actionListener);
    }

    public String getHostname(){
        return entryForm.getString(HOSTNAME_FIELD_NAME);
    }

    public void setHostName(String hostname){
        entryForm.setString(HOSTNAME_FIELD_NAME, hostname);
    }

    public int getPort() {
        double portValue = entryForm.getDouble(PORT_FIELD_NAME);
        double valueRemainder = portValue - (int) portValue;
        if(valueRemainder != 0){
            throw new IllegalArgumentException("Port value cannot be not integer");
        }
        return (int) portValue;
    }

    public void setPort(int port){
        entryForm.setDouble(PORT_FIELD_NAME, port);
    }

    public String getLogin(){
        return entryForm.getString(LOGIN_FIELD_NAME);
    }

    public void setLogin(String login){
        entryForm.setString(LOGIN_FIELD_NAME, login);
    }

    public String getPassword(){
        return entryForm.getString(PASSWORD_FIELD_NAME);
    }

    public void setPassword(String password){
        entryForm.setString(PASSWORD_FIELD_NAME, password);
    }

    public String getRole(){
        return entryForm.getString(ROLES_FIELD_NAME);
    }
}
