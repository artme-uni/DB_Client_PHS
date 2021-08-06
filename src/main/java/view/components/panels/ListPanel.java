package view.components.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.NoSuchElementException;

public class ListPanel extends JPanel {
    private static final int CONTENT_ALIGNMENT = SwingConstants.CENTER;
    private static final int CELL_HEIGHT = 40;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> list = new JList<>(listModel);

    public ListPanel() {
        setLayout(new BorderLayout());

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configureCellProperties();

        add(new JScrollPane(list), SwingConstants.CENTER);
    }

    private void configureCellProperties(){
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
        renderer.setHorizontalAlignment(CONTENT_ALIGNMENT);
        list.setFixedCellHeight(CELL_HEIGHT);
    }

    public void addRecord(String carName) {
        listModel.addElement(carName);
    }

    public String getSelectedContent(){
        int index = list.getSelectedIndex();
        if(index == -1){
            throw new NoSuchElementException();
        }
        return listModel.getElementAt(index);
    }

    public void addDoubleClickListener(MouseAdapter mouseAdapter){
        list.addMouseListener(mouseAdapter);
    }

    public void clearList(){
        listModel.clear();
    }
}
