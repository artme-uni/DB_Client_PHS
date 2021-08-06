package view.components.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;

public class ButtonsPanel extends JPanel {
    private static final int BORDER_HEIGHT = 10;
    private static final int BUTTON_HEIGHT = 40;

    private final HashMap<String, JButton> buttons = new HashMap<>();
    private final GridLayout layout = new GridLayout(1,0);

    public ButtonsPanel() {
        setLayout(layout);
    }

    public void addButton(String buttonName){
        int currentColumnsCount = layout.getColumns();
        layout.setColumns(currentColumnsCount + 1);

        JButton button = new JButton(buttonName);
        button.setPreferredSize(new Dimension(10, BUTTON_HEIGHT));
        setBorder(BorderFactory.createEmptyBorder(BORDER_HEIGHT, 0, BORDER_HEIGHT, 0));

        String processedName = getProcessName(buttonName);
        buttons.put(processedName, button);
        add(button);
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
