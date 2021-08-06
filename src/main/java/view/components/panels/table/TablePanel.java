package view.components.panels.table;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.*;

public class TablePanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTable table;

    private boolean isEditable = true;

    private int columnCount = 0;
    private final Map<String, Integer> columnIndexes = new HashMap<>();
    private final Map<Integer, Map<String, Integer>> dropdownMapperValues = new HashMap<>();
    private final Map<Integer, Map<Integer, String>> dropdownMapperKeys = new HashMap<>();

    private final Set<Integer> notEditableColumns = new HashSet<>();

    public TablePanel() {
        setLayout(new BorderLayout());

        this.tableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return getValueAt(0, columnIndex).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if(!isEditable){
                    return false;
                }
                return !notEditableColumns.contains(column);
            }
        };

        this.table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, SwingConstants.CENTER);

        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if(!isEditable) {
            table.setRowSelectionAllowed(false);
        }

        setHeaderHorizontalAlignment();
        setDefaultCellAlignment(SwingConstants.RIGHT);

        UIManager.put("Table.gridColor", new ColorUIResource(Color.LIGHT_GRAY));
        table.getTableHeader().setPreferredSize(new Dimension(0, 20));
    }

    public void setSelectionAllowed(boolean isAllowed){
        this.isEditable = isAllowed;
        table.setRowSelectionAllowed(isAllowed);
    }

    public void setEditable(boolean isEditable){
        this.isEditable = isEditable;
    }

    public void addColumn(String columnName) {
        tableModel.addColumn(columnName);
        columnIndexes.put(columnName, columnCount++);
    }

    public void addRow(List<Object> rowValue) {
        for(Map.Entry<Integer, Map<Integer, String>> entry : dropdownMapperKeys.entrySet()){
            int oldValue = (int) rowValue.get(entry.getKey());
            String newValue = entry.getValue().get(oldValue);
            rowValue.set(entry.getKey(), newValue);
        }
        tableModel.addRow(rowValue.toArray());
        table.revalidate();
    }

    public void addDataMouseListener(MouseAdapter mouseAdapter){
        table.addMouseListener(mouseAdapter);
    }

    public void setDoubleType(String columnName){
//        int columnIndex = getColumnIndex(columnName);
//        TableColumn columnModel = table.getColumnModel().getColumn(columnIndex);
//        columnModel.setCellEditor(new SpinnerEditor(true));
    }

    public void setIntegerType(String columnName){
//        int columnIndex = getColumnIndex(columnName);
//        TableColumn columnModel = table.getColumnModel().getColumn(columnIndex);
//        columnModel.setCellEditor(new SpinnerEditor(false));
    }

    public void setDropDownType(String columnName, Map<String, Integer> elements){
        int columnIndex = getColumnIndex(columnName);
        dropdownMapperValues.put(columnIndex, elements);

        Map<Integer, String> invertedMap = new HashMap<>();
        for(Map.Entry<String, Integer> entry : elements.entrySet()){
            invertedMap.put(entry.getValue(), entry.getKey());
        }
        this.dropdownMapperKeys.put(columnIndex, invertedMap);

        List<String> orderedValues = new ArrayList<>(elements.keySet());
        Collections.sort(orderedValues);

        TableColumn columnModel = table.getColumnModel().getColumn(columnIndex);
//        columnModel.setCellRenderer(new ComboBoxRender());
        columnModel.setCellEditor(new ComboBoxEditor(orderedValues));
    }

    public void addTableListener(TableModelListener modelListener) {
        tableModel.addTableModelListener(modelListener);
    }

    public void removeListeners(){
        for (TableModelListener listener : tableModel.getTableModelListeners()){
            tableModel.removeTableModelListener(listener);
        }
    }

    public String getColumnName(int index){
        return tableModel.getColumnName(index);
    }

    private int getColumnIndex(String columnName){
        return columnIndexes.get(columnName);
    }

    public List<String> getColumnNames(){
        return new ArrayList<>(columnIndexes.keySet());
    }

    public Object getValue(int rowIndex, String columnName){
        int columnIndex = getColumnIndex(columnName);
        Object value = tableModel.getValueAt(rowIndex, columnIndex);

        if(dropdownMapperValues.containsKey(columnIndex)){
            Map<String, Integer> mapper = dropdownMapperValues.get(columnIndex);
            int unmappedValue = mapper.get(value.toString());
            return unmappedValue;
        }

        return value;
    }

    public void clearTable(){
        tableModel.setRowCount(0);
    }

    public int getSelectedRowIndex(){
        return table.getSelectedRow();
    }

    public void setColumnEditable(String columnName, boolean isEditable){
        int columnIndex = getColumnIndex(columnName);
        if(isEditable){
            notEditableColumns.remove(columnIndex);
        } else {
            notEditableColumns.add(columnIndex);
        }
    }

    private static class HeaderRenderer implements TableCellRenderer {
        DefaultTableCellRenderer renderer;
        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer)
                    table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col) {
            return renderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);
        }
    }

    public void setHeaderHorizontalAlignment(){
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer(table));
    }

    public void setDefaultCellAlignment(int alignment){
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(alignment);
        table.setDefaultRenderer(String.class, renderer);
    }

    public int getRowsCount(){
        return table.getRowCount();
    }

}
