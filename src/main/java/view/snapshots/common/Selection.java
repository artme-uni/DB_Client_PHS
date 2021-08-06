package view.snapshots.common;

import view.components.panels.ButtonsPanel;
import view.components.panels.ListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;

public class Selection extends JPanel {
    private final ListPanel listPanel;
    private final ButtonsPanel buttonsPanel;

    private final String backButtonName;
    private final String showButtonName;

    public Selection(String snapshotName, String showButtonName) {
        this.backButtonName = "Назад";
        this.showButtonName = showButtonName;

        setLayout(new BorderLayout());
        listPanel = new ListPanel();
        buttonsPanel = new ButtonsPanel();

        buttonsPanel.addButton(backButtonName);
        buttonsPanel.addButton(showButtonName);

        add(listPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setTitle(snapshotName);
    }

    private void setTitle(String title){
        JLabel name = new JLabel(title);
        name.setBorder(BorderFactory.createEmptyBorder(0,0,20, 0));
        name.setHorizontalAlignment(SwingConstants.CENTER);
        add(name, BorderLayout.NORTH);
    }

    public void addListElements(List<String> elements){
        clearList();
        for(String e : elements){
            listPanel.addRecord(e);
        }
    }

    public String getSelectedElement(){
        return listPanel.getSelectedContent();
    }

    public void addBackButtonListener(ActionListener actionListener){
        buttonsPanel.addButtonListener(backButtonName, actionListener);
    }

    public void addShowButtonListener(ActionListener actionListener){
        buttonsPanel.addButtonListener(showButtonName, actionListener);
    }

    public void addMouseListener(MouseAdapter adapter){
        listPanel.addDoubleClickListener(adapter);
    }

    public void clearList(){
        listPanel.clearList();
    }
}
