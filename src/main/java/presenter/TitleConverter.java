package presenter;

import java.util.*;

public class TitleConverter {
    private final Map<String, Map<String, String>> tablesTitles = new HashMap<>();
    private final Map<String, Map<String, String>> tablesNames = new HashMap<>();


    public void addTitles(String metaName, Map<String, String> titles) {
        tablesTitles.put(metaName, titles);
        Map<String, String> names = new HashMap<>();
        for (Map.Entry<String, String> entry : titles.entrySet()) {
            names.put(entry.getValue(), entry.getKey());
        }
        tablesNames.put(metaName, names);
    }

    public String getTitle(String metaName, String tableName) {
        Map<String, String> currentTitles = tablesTitles.get(metaName);
        Map<String, String> currentNames = tablesNames.get(metaName);

        String title = currentTitles.get(tableName);
        if (title == null && currentNames.containsKey(tableName)) {
            return tableName;
        }
        return title;
    }

    public String getName(String metaName, String tableTitle) {
        Map<String, String> currentTitles = tablesTitles.get(metaName);
        Map<String, String> currentNames = tablesNames.get(metaName);

        String name = currentNames.get(tableTitle);
        if (name == null && currentTitles.containsKey(tableTitle)) {
            return tableTitle;
        }
        return name;
    }

    public List<String> getTitles(String metaName, List<String> tableNames) {
        List<String> titles = new ArrayList<>();
        for (String name : tableNames) {
            titles.add(getTitle(metaName, name));
        }
        return titles;
    }

    public List<String> getNames(String metaName, List<String> tableTitles) {
        List<String> names = new ArrayList<>();
        for (String title : tableTitles) {
            names.add(getName(metaName, title));
        }
        return names;
    }

    public Set<String> getTitles(String metaName, Set<String> tableNames) {
        Set<String> titles = new HashSet<>();
        for (String name : tableNames) {
            titles.add(getTitle(metaName, name));
        }
        return titles;
    }

    public Set<String> getNames(String metaName, Set<String> tableTitles) {
        Set<String> names = new HashSet<>();
        for (String title : tableTitles) {
            names.add(getName(metaName, title));
        }
        return names;
    }
}
