package view.components.panels.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ComboBoxRender extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());

        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getSelectionForeground());
        }

        return this;
    }

}
