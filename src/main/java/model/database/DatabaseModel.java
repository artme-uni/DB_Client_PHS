package model.database;

import model.properties.PropertiesBundle;

import java.util.*;
import java.util.stream.Collectors;

public class DatabaseModel {
    private final HashMap<String, PropertiesBundle> tables = new HashMap<>();
    private final ArrayList<PropertiesBundle> orderedTables = new ArrayList<>();

    private final Map<String, Set<String>> writeAccessGrantedTables = new HashMap<>();
    private final Map<String, Set<String>> readOnlyTables = new HashMap<>();

    private final Map<String, String> tableTitles = new HashMap<>();

    public PropertiesBundle addTable(String tableName, String tableTitle){
        PropertiesBundle tableInfo = new PropertiesBundle(tableName);
        tables.put(tableName, tableInfo);
        orderedTables.add(tableInfo);

        tableTitles.put(tableName, tableTitle);
        return tableInfo;
    }

    public PropertiesBundle getTable(String tableName){
        PropertiesBundle tableInfo = tables.get(tableName);
        if(tableInfo == null){
            throw new NoSuchElementException("Table with name " + tableName + " does not exist");
        }
        return tableInfo;
    }

    public Map<String, String> getTablesTitles(){
        return tableTitles;
    }

    public Set<String> getWriteAccessedTables(String roleName){
        Set<String> availableTables = writeAccessGrantedTables.get(roleName);
        if(availableTables == null){
            return new HashSet<>();
        }
        return availableTables;
    }

    public Set<String> getReadAccessedTables(String roleName){
        Set<String> availableTables = readOnlyTables.get(roleName);
        if(availableTables == null){
            return new HashSet<>();
        }
        return availableTables;
    }

    public List<String> getAvailableTables(String roleName){
        Set<String> availableTables = getWriteAccessedTables(roleName);
        availableTables.addAll(getReadAccessedTables(roleName));
        return getFilteredOrderedTables(availableTables);
    }

    public List<String> getAvailableTables(){
        return orderedTables.stream()
                .map(PropertiesBundle::getBundleName)
                .collect(Collectors.toList());
    }


    public List<String> getFilteredOrderedTables(Set<String> availableTables){
        return orderedTables.stream()
                .map(PropertiesBundle::getBundleName)
                .filter(availableTables::contains)
                .collect(Collectors.toList());
    }

    public void grantWriteAccess(String roleName, String tableName){
        Set<String> grantedTables = writeAccessGrantedTables.computeIfAbsent(roleName, k -> new HashSet<>());
        grantedTables.add(tableName);
    }

    public void grantReadOnlyAccess(String roleName, String tableName){
        Set<String> grantedTables = readOnlyTables.computeIfAbsent(roleName, k -> new HashSet<>());
        grantedTables.add(tableName);
    }

}
