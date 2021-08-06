package view.components.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

public class MenuPanel extends JPanel{
    private static final int BUTTON_HEIGHT = 60;

    private final HashMap<String, JButton> buttons = new HashMap<>();
    private final ColumnsPanel columnsPanel;

    public MenuPanel(List<Integer> columnsProportions) {
        this.columnsPanel = new ColumnsPanel(columnsProportions);
        setLayout(new BorderLayout());

        add(columnsPanel, SwingConstants.CENTER);
    }

    public void addButton(String buttonName){
        JButton button = new JButton(buttonName);
        button.setPreferredSize(new Dimension(10, BUTTON_HEIGHT));

        String processedName = getProcessName(buttonName);
        buttons.put(processedName, button);
        columnsPanel.addRow(new JPanel(), button, new JPanel());
    }

    public void addSpace(){
        columnsPanel.addRow(new JPanel(), new JPanel(), new JPanel());
    }

    public JButton getButton(String buttonName){
        String processedName = getProcessName(buttonName);
        JButton button = buttons.get(processedName);
        if(button == null){
            throw new NoSuchElementException("Button with name " + buttonName + " does not exists");
        }
        return button;
    }

    public void addButtonListener(String buttonName, ActionListener actionListener){
        JButton button = getButton(buttonName);
        button.addActionListener(actionListener);
    }

    public String getProcessName(String name){
        return name.toLowerCase(Locale.US);
    }
}
