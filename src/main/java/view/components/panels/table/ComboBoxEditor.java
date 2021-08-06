package view.components.panels.table;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * A custom editor for cells in the Country column.
 * @author www.codejava.net
 *
 */
public class ComboBoxEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    private String selectedValue;
    private final List<String> values;

    ComboBoxEditor(List<String> values) {
        this.values = values;
    }

    @Override
    public Object getCellEditorValue() {
        return this.selectedValue;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (value instanceof String) {
            this.selectedValue = (String) value;
        }

        JComboBox<String> comboBox = new JComboBox<>();

        for (String val : values) {
            comboBox.addItem(val);
        }

        comboBox.setSelectedItem(selectedValue);
        comboBox.addActionListener(this);
        comboBox.addActionListener(e -> table.grabFocus());

        return comboBox;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<String> comboBox = (JComboBox<String>) event.getSource();
        this.selectedValue = (String) comboBox.getSelectedItem();
    }

}