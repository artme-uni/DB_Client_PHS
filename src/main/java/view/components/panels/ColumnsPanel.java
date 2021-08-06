package view.components.panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ColumnsPanel extends JPanel {
    private final List<Integer> columnsProportions;
    private static final int CONSTRAINT_HEIGHT = 10;

    private final GridBagConstraints constraint = new GridBagConstraints();

    public ColumnsPanel(List<Integer> columnsProportions) {
        this.columnsProportions = columnsProportions;
        setLayout(new GridBagLayout());
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridy = 0;
        constraint.weighty = CONSTRAINT_HEIGHT;
    }

    public void addRow(JComponent... rowComponents){
        if(rowComponents.length != columnsProportions.size()){
            throw new IllegalArgumentException("Arg count must be equal " + columnsProportions.size());
        }

        for (int i = 0; i < rowComponents.length; i++) {
            constraint.gridx = i;
            constraint.weightx = columnsProportions.get(i);
            add(rowComponents[i], constraint);
        }
        constraint.gridy += 1;
    }
}
