package view.components.panels.forms;

import view.components.panels.ColumnsPanel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.*;

public class SelectiveFormPanel {
    private final EntryFormPanel entryForm;
    private ColumnsPanel checkBoxesColumn;

    private final Map<Object, Integer> checkBoxesIndexes = new HashMap<>();
    private final List<JCheckBox> checkboxes = new ArrayList<>();

    private final boolean isSelectEnabled;

    public SelectiveFormPanel(List<Integer> columnProportion, boolean isSelectEnabled) {
        this.entryForm = new EntryFormPanel(columnProportion);
        this.isSelectEnabled = isSelectEnabled;
    }

    public void addCheckBoxes(boolean isSelected){
        int propertiesCount = getAttributesCount();
        checkBoxesColumn = new ColumnsPanel(Collections.singletonList(100));
        checkBoxesColumn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        for(int i = 0; i < propertiesCount; i++){
            JCheckBox checkBox = new JCheckBox();
            checkBox.setVisible(isSelectEnabled);

            checkBox.setSelected(isSelected);
            checkBox.addActionListener(getCheckBoxListener());

            checkBoxesIndexes.put(checkBox, i);
            checkboxes.add(checkBox);

            entryForm.setEnable(isSelected, i);

            checkBoxesColumn.addRow(checkBox);
        }
    }

    private ActionListener getCheckBoxListener(){
        return event -> {
            Integer index = checkBoxesIndexes.get(event.getSource());
            JCheckBox sourceCheckBox = checkboxes.get(index);
            entryForm.setEnable(sourceCheckBox.isSelected(), index);
        };
    }

    public Map<String, Object> getValuesTuples(){
        List<Object> values = getValues();
        List<String> valuesNames = getAttributesNames();
        if(values.size() != valuesNames.size()){
            throw new NoSuchElementException();
        }

        Map<String, Object> valuesTuples = new HashMap<>();
        for (int i = 0; i < values.size(); i++) {
            valuesTuples.put(valuesNames.get(i), values.get(i));
        }

        return valuesTuples;
    }

    public Map<String, Object> getFilteredValuesTuples(){
        Map<String, Object> valuesTuples = getValuesTuples();
        List<String> names = getAttributesNames();

        for (int i = 0; i < names.size(); i++) {
            if(!isSelected(i)){
                String attributeName = names.get(i);
                valuesTuples.remove(attributeName);
            }
        }

        return valuesTuples;
    }

    public boolean isSelected(int attributeIndex){
        JCheckBox checkBox = checkboxes.get(attributeIndex);
        return checkBox.isSelected();
    }

    public JPanel getFormContent(){
        return entryForm.getPanelContent();
    }

    public JPanel getCheckBoxesContent(){
        return checkBoxesColumn;
    }

    public int getAttributesCount(){
        return getAttributesNames().size();
    }

    public List<String> getAttributesNames(){
        return entryForm.getAttributesNames();
    }

    public EntryFormPanel getEntryFormPanel(){
        return entryForm;
    }

    public List<Object> getValues(){
        return entryForm.getValues();
    }

    public void clearInputData(){
        entryForm.clearInputData();
    }
}
