package model.service;

import java.util.*;
import java.util.stream.Collectors;

public class ServiceInfo {
    private final String serviceName;
    private ServiceType serviceType;

    private final Map<String, List<String>> tablesFields = new HashMap<>();
    private final Map<String, List<String>> dependentTables = new HashMap<>();

    public ServiceInfo(String serviceName) {
        this.serviceName = serviceName;
    }

    public void addTableFields(String tableName, List<String> fieldsNames){
        List<String> fields = fieldsNames.stream().map(s -> s.toLowerCase(Locale.US)).collect(Collectors.toList());
        tablesFields.put(tableName, fields);
    }

    public void addTableDependency(String mainTableName, String dependentTableName) {
        dependentTables.computeIfAbsent(mainTableName, k -> new ArrayList<>());
        List<String> tables = dependentTables.get(mainTableName);
        tables.add(dependentTableName);
    }

    public List<String> getIndependentTables(){
        List<String> tablesNames = new ArrayList<>();

        for(String table : tablesFields.keySet()){
            if(!isDependentTable(table)){
                tablesNames.add(table);
            }
        }
        return tablesNames;
    }

    private boolean isDependentTable(String tableName){
        for(List<String> table : dependentTables.values()){
            if(table.contains(tableName)){
                return true;
            }
        }
        return false;
    }

    public List<String> getTableFields(String tableName){
        return tablesFields.get(tableName);
    }

    public List<String> getDependentTables(String tableName){
        return dependentTables.get(tableName);
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
}
