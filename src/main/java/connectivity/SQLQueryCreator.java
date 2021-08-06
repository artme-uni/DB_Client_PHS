package connectivity;

import java.util.*;

public class SQLQueryCreator {
    private SQLQueryCreator() {
    }

    public static StringBuilder getQuery(String tableName, List<String> targetColumns) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT\n");
        addListing(stringBuilder, targetColumns, "\t", "\n", ",");
        stringBuilder.append("FROM\n");
        addElement(stringBuilder, tableName, "\t", "");
        return stringBuilder;
    }

    public static StringBuilder getUpdateValueStatement(String tableName, String columnName, String newValue) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE\n");
        addElement(stringBuilder, tableName, "\t", "\n");
        stringBuilder.append("SET\n");
        String assigment = columnName + " = " + newValue;
        addElement(stringBuilder, assigment, "\t", "");
        return stringBuilder;
    }

    public static StringBuilder getUpdateValueStatement(String tableName, Map<String, String> values) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE\n");
        addElement(stringBuilder, tableName, "\t", "\n");
        stringBuilder.append("SET\n");
        int counter = 1;
        for(Map.Entry<String, String> entry: values.entrySet()){
            String assigment = entry.getKey() + " = " + entry.getValue();
            if (counter != values.size()) {
                addElement(stringBuilder, assigment, "\t", ",\n");
            } else {
                addElement(stringBuilder, assigment, "\t", "");
            }

            counter++;
        }
        return stringBuilder;
    }

    public static String getInsertRowStatement(String tableName, Map<String, String> values) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO\n");
        addElement(stringBuilder, tableName, "\t", "\n");

        if (!values.isEmpty()) {
            stringBuilder.append("\t(");
            addListing(stringBuilder, new ArrayList<>(values.keySet()), "", "", ", ");
            stringBuilder.append(")\n");
        }

        stringBuilder.append("VALUES\n");
        stringBuilder.append("\t(");

        if (values.isEmpty()) {
            stringBuilder.append("DEFAULT");
        } else {
            addListing(stringBuilder, new ArrayList<>(values.values()), "", "", ", ");
        }

        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    public static StringBuilder getDeleteRowStatement(String tableName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM\n");
        addElement(stringBuilder, tableName, "\t", "");

        return stringBuilder;
    }

    public static String getDropTableStatement(String tableName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DROP TABLE ");
        addElement(stringBuilder, tableName, "", "");

        return stringBuilder.toString();
    }

    public static String getTruncateTableStatement(String tableName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TRUNCATE TABLE ");
        addElement(stringBuilder, tableName, "", "");

        return stringBuilder.toString();
    }

    public static void addCondition(StringBuilder stringBuilder, String condition) {
        stringBuilder.append("\n");
        stringBuilder.append("WHERE\n");
        addElement(stringBuilder, condition, "\t", "");
    }

    public static void addConditions(StringBuilder stringBuilder, Map<String, String> requiredValues) {
        stringBuilder.append("\n");
        stringBuilder.append("WHERE\n");

        int counter = 1;
        for(Map.Entry<String, String> entry: requiredValues.entrySet()){
            String condition = entry.getKey() + " = " + entry.getValue();
            if(counter != requiredValues.size()){
                addElement(stringBuilder, condition, "\t", " AND\n");
            } else {
                addElement(stringBuilder, condition, "\t", "");
            }
            counter++;
        }
    }

    private static void addListing(StringBuilder stringBuilder, List<String> elements,
                                   String beginningAppender, String endAppender, String delimiter) {
        for (int i = 0; i < elements.size(); i++) {
            stringBuilder.append(beginningAppender);
            String name = elements.get(i);
            stringBuilder.append(name);
            if (i != elements.size() - 1) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(endAppender);
        }
    }

    private static void addElement(StringBuilder stringBuilder, String element,
                                   String beginningAppender, String endAppender) {
        stringBuilder.append(beginningAppender);
        stringBuilder.append(element);
        stringBuilder.append(endAppender);
    }

    public static String getFilterConditions(String sql, List<String> unusedVariables) {
        String result = sql;

        for (String variable : unusedVariables) {
            result = filterRows(result, ":" + variable);
        }

        result = removeIllegal(result, "AND\n");
        result = removeIllegal(result, "WHERE\n");

        return result;
    }

    private static int mustBeRemoved(String sql, String target) {
        StringBuilder builder = new StringBuilder(sql);

        int foundTargetIndex = builder.indexOf(target, 0);
        int nextCharIndex = foundTargetIndex + target.length();

        while (foundTargetIndex != -1) {
            if (nextCharIndex >= sql.length()) {
                return foundTargetIndex;
            }

            while(Character.isWhitespace(sql.charAt(nextCharIndex))){
                nextCharIndex++;
            }

            String nextChar = String.valueOf(sql.charAt(nextCharIndex));
            if (nextChar.matches("[)]")) {
                return foundTargetIndex;
            }

            if (nextCharIndex + 8 < sql.length()) {
                String nextWord = sql.substring(nextCharIndex, nextCharIndex + 8);
                if (nextWord.equals("ORDER BY") || nextWord.equals("GROUP BY")) {
                    return foundTargetIndex;
                }
            }

            foundTargetIndex = builder.indexOf(target, nextCharIndex);
            nextCharIndex = foundTargetIndex + target.length();
        }

        return -1;
    }

    private static String removeIllegal(String inputSQL, String target) {
        StringBuilder builder = new StringBuilder(inputSQL);

        int index = mustBeRemoved(builder.toString(), target);
        while (index != -1) {
            builder.delete(index, index + target.length());
            index = mustBeRemoved(builder.toString(), target);
        }
        return builder.toString();
    }

    public static Map<String, Integer> getVariablesIndexes(String sql, List<String> variables) {
        Map<String, Integer> stringIndexes = new HashMap<>();

        for (String var : variables) {
            stringIndexes.put(var, sql.indexOf(var));
        }

        variables.sort(Comparator.comparing(stringIndexes::get));

        for (int i = 0; i < variables.size(); i++) {
            stringIndexes.put(variables.get(i), i + 1);
        }

        return stringIndexes;
    }

    public static String replaceVariables(String sql, List<String> variables) {
        String result = sql;
        for (String var : variables) {
            result = result.replaceAll(":" + var, "?");
        }

        return result;
    }

    private static String filterRows(String mainString, String deprecatedSymbols) {
        String[] rows = mainString.split("\n");
        StringBuilder builder = new StringBuilder();
        for (String row : rows) {
            int index = row.indexOf(deprecatedSymbols);
            if (index == -1) {
                builder.append(row);
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    public static String getMaxIDQuery(String tableName, String IDColumnName) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT\n")
                .append("\tMAX(").append(IDColumnName).append(") ").append("AS ID\n")
                .append("FROM ")
                .append(tableName);

        return builder.toString();
    }
}

