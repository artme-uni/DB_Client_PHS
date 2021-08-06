package view.snapshots;

import view.components.panels.ButtonsPanel;
import view.components.panels.forms.EntryFormPanel;
import view.components.panels.forms.SelectiveFormPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class FilteringEntryForm extends JPanel {
    private String selectedName;

    private SelectiveFormPanel entryForm;
    private final ButtonsPanel buttonsPanel;
    private JPanel checkBoxes;

    private final List<Integer> columnProportion;

    private static final String BACK_BUTTON_NAME = "Назад";
    private final String ACTION_BUTTON_NAME;

    private final boolean isCheckBoxesVisible;

    public FilteringEntryForm(List<Integer> columnProportion, String actionButtonName, boolean isCheckBoxesVisible) {
        setLayout(new BorderLayout());

        ACTION_BUTTON_NAME = actionButtonName;

        this.columnProportion = columnProportion;
        this.entryForm = new SelectiveFormPanel(columnProportion, isCheckBoxesVisible);
        this.buttonsPanel = new ButtonsPanel();

        buttonsPanel.addButton(BACK_BUTTON_NAME);
        buttonsPanel.addButton(ACTION_BUTTON_NAME);

        add(entryForm.getFormContent(), BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        this.isCheckBoxesVisible = isCheckBoxesVisible;
    }

    public FilteringEntryForm(List<Integer> columnProportion, String actionButtonName) {
        this(columnProportion, actionButtonName, true);
    }

    public void addCheckBoxes(boolean isSelected){
        entryForm.addCheckBoxes(isSelected);
        if(checkBoxes != null) {
            remove(checkBoxes);
        }
        checkBoxes = entryForm.getCheckBoxesContent();
        add(checkBoxes, BorderLayout.WEST);
    }

    public EntryFormPanel getEntryFormPanel(){
        clearForm();
        return entryForm.getEntryFormPanel();
    }

    public EntryFormPanel getExistingEntryFormPanel(){
        return entryForm.getEntryFormPanel();
    }

    private void clearForm(){
        remove(entryForm.getFormContent());
        entryForm = new SelectiveFormPanel(columnProportion, isCheckBoxesVisible);
        add(entryForm.getFormContent(), BorderLayout.CENTER);
    }

    public void addBackButtonListener(ActionListener actionListener){
        buttonsPanel.addButtonListener(BACK_BUTTON_NAME, actionListener);
    }

    public void addApplyButtonListener(ActionListener actionListener){
        buttonsPanel.addButtonListener(ACTION_BUTTON_NAME, actionListener);
    }

    public List<Object> getValues(){
        return entryForm.getValues();
    }

    public void clearInputData(){
        entryForm.clearInputData();
    }

    public Map<String, Object> getFilteredValuesTuple(){
        return entryForm.getFilteredValuesTuples();
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }
}
