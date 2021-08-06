package model.queries;

import model.properties.PropertiesBundle;

import java.util.*;
import java.util.stream.Collectors;

public class QueriesModel {
    private final HashMap<String, PropertiesBundle> queries = new HashMap<>();
    private final HashMap<String, PropertiesBundle> tables = new HashMap<>();
    private final ArrayList<PropertiesBundle> orderedQueries = new ArrayList<>();

    private final Map<String, Set<String>> accessedQueries = new HashMap<>();

    public PropertiesBundle addQuery(String queryName) {
        PropertiesBundle queryInfo = new PropertiesBundle(queryName);
        queries.put(queryName, queryInfo);

        PropertiesBundle tableInfo = new PropertiesBundle(queryName);
        tables.put(queryName, tableInfo);

        orderedQueries.add(queryInfo);
        return queryInfo;
    }

    public PropertiesBundle getQuery(String queryName) {
        PropertiesBundle queryInfo = queries.get(queryName);
        if (queryInfo == null) {
            throw new NoSuchElementException("Query with name '" + queryName + "' does not exist");
        }
        return queryInfo;
    }

    public PropertiesBundle getTable(String queryName) {
        PropertiesBundle tableInfo = tables.get(queryName);
        if (tableInfo == null) {
            throw new NoSuchElementException("Query with name '" + queryName + "' does not exist");
        }
        return tableInfo;
    }

    public List<String> getAccessedQueriesNames(String roleName){
        Set<String> accessedQueriesNames = accessedQueries.get(roleName);
        if(accessedQueriesNames == null){
            return new ArrayList<>();
        }

        return orderedQueries.stream()
                .map(PropertiesBundle::getBundleName)
                .filter(accessedQueriesNames::contains)
                .collect(Collectors.toList());
    }

    public List<String> getAvailableQueriesNames() {
        return orderedQueries.stream()
                .map(PropertiesBundle::getBundleName)
                .collect(Collectors.toList());
    }

    public void grantAccess(String roleName, String queryName){
        Set<String> queriesNames = accessedQueries.computeIfAbsent(roleName, k -> new HashSet<>());
        queriesNames.add(queryName);
    }
}
