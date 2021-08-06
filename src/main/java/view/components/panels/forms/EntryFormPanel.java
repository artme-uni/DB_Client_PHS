package view.components.panels.forms;

import view.components.panels.ColumnsPanel;

import javax.swing.*;
import java.util.*;

public class EntryFormPanel {
    private final List<String> attributes = new ArrayList<>();
    private final ColumnsPanel mainTable;
    private static final int COLUMN_COUNT = 3;
    private static final int NAME_ALIGNMENT = SwingConstants.CENTER;
    private final Map<String, Map<String, Integer>> possibleDropdownValues = new HashMap<>();

    private final Map<String, JTextField> textFields = new HashMap<>();
    private final Map<String, JPasswordField> passwordFields = new HashMap<>();
    private final Map<String, JSpinner> spinners = new HashMap<>();
    private final Map<String, JComboBox<String>> comboBoxes = new HashMap<>();
    private final Map<String, JCheckBox> checkBoxes = new HashMap<>();

    private static final String NO_SUCH_ELEMENT_EXCEPTION_MSG = "Form doesn't have field %s with type %s";

    public EntryFormPanel(List<Integer> columnProportion) {
        if(columnProportion.size() != COLUMN_COUNT){
            throw new IllegalArgumentException("Array with columns proportions must contain " + COLUMN_COUNT + " elements");
        }
        mainTable = new ColumnsPanel(columnProportion);
    }

    public void addTextField(String fieldName){
        JTextField textField = new JTextField();
        addRow(fieldName, textField);

        String processedName = getProcessedName(fieldName);
        textFields.put(processedName, textField);
    }

    public void addBooleanField(String fieldName){
        JCheckBox checkBox = new JCheckBox();
        addRow(fieldName, checkBox);

        String processedName = getProcessedName(fieldName);
        checkBoxes.put(processedName, checkBox);
    }

    public void addNumericField(String fieldName, boolean isDecimal){
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(0.0, null, null, 1);
        JSpinner spinner = new JSpinner(spinnerNumberModel);
        if(!isDecimal) {
            spinner.setEditor(new JSpinner.NumberEditor(spinner, "#"));
        }
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) spinner.getEditor();
        spinnerEditor.getTextField().setHorizontalAlignment(SwingConstants.LEFT);
        addRow(fieldName, spinner);

        String processedName = getProcessedName(fieldName);
        spinners.put(processedName, spinner);
    }

    public void addDropDownField(String fieldName, String[] items){
        JComboBox<String> comboBox = new JComboBox<>(items);
        addRow(fieldName, comboBox);

        String processedName = getProcessedName(fieldName);
        comboBoxes.put(processedName, comboBox);
    }

    public void addMappedDropdownField(String fieldName, Map<Integer, String> possibleValues){
        Map<String, Integer> reverseValues = new HashMap<>();
        for(Map.Entry<Integer, String> entry: possibleValues.entrySet()){
            reverseValues.put(entry.getValue(), entry.getKey());
        }
        this.possibleDropdownValues.put(fieldName, reverseValues);
        addDropDownField(fieldName, reverseValues.keySet().toArray(new String[0]));
    }

    public void addPasswordField(String fieldName){
        JPasswordField passwordField = new JPasswordField();
        addRow(fieldName, passwordField);

        String processedName = getProcessedName(fieldName);
        passwordFields.put(processedName, passwordField);
    }

    private void addRow(String fieldName, JComponent component){
        putAttribute(fieldName);

        JLabel labelWithName = new JLabel(fieldName);
        labelWithName.setHorizontalAlignment(NAME_ALIGNMENT);

        mainTable.addRow(labelWithName, component, new JPanel());
    }

    public void addClearRow(){
        mainTable.addRow(new JPanel(), new JPanel(), new JPanel());
    }

    private void putAttribute(String fieldName){
        if (attributes.contains(fieldName)){
            throw new IllegalArgumentException("Attribute with name '" + fieldName+ "' already exists");
        }
        attributes.add(fieldName);
    }

    private boolean textFieldExists(String fieldName){
        String processedName = getProcessedName(fieldName);
        return textFields.containsKey(processedName);
    }

    private JTextField getTextField(String fieldName){
        String processedName = getProcessedName(fieldName);
        return textFields.get(processedName);
    }

    private boolean passwordFieldExists(String fieldName){
        String processedName = getProcessedName(fieldName);
        return passwordFields.containsKey(processedName);
    }

    private JPasswordField getPasswordField(String fieldName){
        String processedName = getProcessedName(fieldName);
        return passwordFields.get(processedName);
    }

    private boolean comboBoxExists(String fieldName){
        String processedName = getProcessedName(fieldName);
        return comboBoxes.containsKey(processedName);
    }

    private JComboBox<String> getComboBox(String fieldName){
        String processedName = getProcessedName(fieldName);
        return comboBoxes.get(processedName);
    }

    public String getString(String fieldName){
        if(textFieldExists(fieldName)){
            return getTextField(fieldName).getText();
        }
        if(passwordFieldExists(fieldName)){
            return String.valueOf(getPasswordField(fieldName).getPassword());
        }
        if(comboBoxExists(fieldName)){
            return (String) getComboBox(fieldName).getSelectedItem();
        }

        throw new NoSuchElementException(String.format(NO_SUCH_ELEMENT_EXCEPTION_MSG, fieldName, "String"));
    }

    public void setString(String fieldName, String value){
        if(textFieldExists(fieldName)){
            getTextField(fieldName).setText(value);
            return;
        }
        if(passwordFieldExists(fieldName)){
            getPasswordField(fieldName).setText(value);
            return;
        }
        if(comboBoxExists(fieldName)){
            getComboBox(fieldName).getModel().setSelectedItem(value);
            return;
        }

        throw new NoSuchElementException(String.format(NO_SUCH_ELEMENT_EXCEPTION_MSG, fieldName, "String"));
    }


    private boolean spinnerExists(String fieldName){
        String processedName = getProcessedName(fieldName);
        return spinners.containsKey(processedName);
    }

    private JSpinner getSpinner(String fieldName){
        String processedName = getProcessedName(fieldName);
        return spinners.get(processedName);
    }

    public double getDouble(String fieldName){
        if(spinnerExists(fieldName)){
            return (double) getSpinner(fieldName).getValue();
        }

        throw new NoSuchElementException(String.format(NO_SUCH_ELEMENT_EXCEPTION_MSG, fieldName, "Double"));
    }

    public void setDouble(String fieldName, double value){
        if(spinnerExists(fieldName)){
            getSpinner(fieldName).setValue(value);
            return;
        }

        throw new NoSuchElementException(String.format(NO_SUCH_ELEMENT_EXCEPTION_MSG, fieldName, "Double"));
    }

    public void setInteger(String fieldName, int value){
        if(spinnerExists(fieldName)){
            getSpinner(fieldName).setValue(value);
            return;
        }

        throw new NoSuchElementException(String.format(NO_SUCH_ELEMENT_EXCEPTION_MSG, fieldName, "Integer"));
    }

    private boolean checkBoxExists(String fieldName){
        String processedName = getProcessedName(fieldName);
        return checkBoxes.containsKey(processedName);
    }

    private JCheckBox getCheckBox(String fieldName){
        String processedName = getProcessedName(fieldName);
        return checkBoxes.get(processedName);
    }

    public boolean getBoolean(String fieldName){
        if(checkBoxExists(fieldName)){
            return getCheckBox(fieldName).isSelected();
        }

        throw new NoSuchElementException(String.format(NO_SUCH_ELEMENT_EXCEPTION_MSG, fieldName, "Boolean"));
    }

    public void setBoolean(String fieldName, boolean value){
        if(checkBoxExists(fieldName)){
            getCheckBox(fieldName).setSelected(value);
            return;
        }

        throw new NoSuchElementException(String.format(NO_SUCH_ELEMENT_EXCEPTION_MSG, fieldName, "Boolean"));
    }

    public JPanel getPanelContent(){
        mainTable.setVisible(true);
        return mainTable;
    }

    protected String getProcessedName(String name){
//        return name.toLowerCase(Locale.UK);
        return name;
    }

    public List<Object> getValues(){
        ArrayList<Object> values = new ArrayList<>();
        for(String attributeName : attributes){
            if(textFields.containsKey(attributeName)) {
                JTextField value = textFields.get(attributeName);
                values.add(value.getText());
            }
            if(passwordFields.containsKey(attributeName)) {
                JPasswordField value = passwordFields.get(attributeName);
                values.add(new String(value.getPassword()));
            }
            if(spinners.containsKey(attributeName)) {
                JSpinner value = spinners.get(attributeName);
                values.add(value.getValue());
            }
            if(comboBoxes.containsKey(attributeName)) {
                JComboBox<String> value = comboBoxes.get(attributeName);
                Map<String, Integer> mapping = possibleDropdownValues.get(attributeName);
                String selectedElement = (String) value.getSelectedItem();
                if(mapping != null) {
                    values.add(mapping.get(selectedElement));
                } else {
                    values.add(selectedElement);
                }
            }
            if(checkBoxes.containsKey(attributeName)) {
                JCheckBox value = checkBoxes.get(attributeName);
                values.add(value.isSelected());
            }
        }
        return values;
    }

    public void clearInputData(){
        for(String attributeName : attributes){
            if(textFields.containsKey(attributeName)) {
                JTextField value = textFields.get(attributeName);
                value.setText("");
            }
            if(passwordFields.containsKey(attributeName)) {
                JPasswordField value = passwordFields.get(attributeName);
                value.setText("");
            }
            if(spinners.containsKey(attributeName)) {
                JSpinner value = spinners.get(attributeName);
                value.setValue(0);
            }
            if(comboBoxes.containsKey(attributeName)) {
                JComboBox<String> value = comboBoxes.get(attributeName);
                value.setSelectedItem("");
            }
            if(checkBoxes.containsKey(attributeName)) {
                JCheckBox value = checkBoxes.get(attributeName);
                value.setSelected(false);
            }
        }
    }

    private JComponent getPanel(String attributeName){
        if(textFields.containsKey(attributeName)) {
            return textFields.get(attributeName);
        }
        if(passwordFields.containsKey(attributeName)) {
            return passwordFields.get(attributeName);
        }
        if(spinners.containsKey(attributeName)) {
            return spinners.get(attributeName);
        }
        if(comboBoxes.containsKey(attributeName)) {
            return comboBoxes.get(attributeName);
        }
        if(checkBoxes.containsKey(attributeName)) {
            return checkBoxes.get(attributeName);
        }

        throw new NoSuchElementException("Cannot find element with name '" + attributeName + "'");
    }

    public List<String> getAttributesNames(){
        return attributes;
    }

    public void setEnable(boolean isEnable, int elementIndex){
        String attributeName = attributes.get(elementIndex);
        JComponent component = getPanel(attributeName);
        component.setEnabled(isEnable);
    }
}
