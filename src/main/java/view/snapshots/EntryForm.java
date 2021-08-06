package view.snapshots;

import view.components.panels.forms.EntryFormPanel;
import view.components.panels.ButtonsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class EntryForm extends JPanel {
    private EntryFormPanel entryForm;
    private final ButtonsPanel buttonsPanel;

    private final List<Integer> columnProportion;

    private static final String BACK_BUTTON_NAME = "Назад";
    private static final String APPLY_BUTTON_NAME = "Применить";

    public EntryForm(List<Integer> columnProportion) {
        setLayout(new BorderLayout());

        this.columnProportion = columnProportion;
        this.entryForm = new EntryFormPanel(columnProportion);
        this.buttonsPanel = new ButtonsPanel();

        buttonsPanel.addButton(BACK_BUTTON_NAME);
        buttonsPanel.addButton(APPLY_BUTTON_NAME);

        add(entryForm.getPanelContent(), BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public EntryFormPanel getEntryFormPanel(){
        clearForm();
        return entryForm;
    }

    private void clearForm(){
        remove(entryForm.getPanelContent());
        entryForm = new EntryFormPanel(columnProportion);
        add(entryForm.getPanelContent(), BorderLayout.CENTER);
    }

    public void addBackButtonListener(ActionListener actionListener){
        buttonsPanel.addButtonListener(BACK_BUTTON_NAME, actionListener);
    }

    public void addApplyButtonListener(ActionListener actionListener){
        buttonsPanel.addButtonListener(APPLY_BUTTON_NAME, actionListener);
    }

    public List<Object> getValues(){
        return entryForm.getValues();
    }

    public void clearInputData(){
        entryForm.clearInputData();
    }
}
