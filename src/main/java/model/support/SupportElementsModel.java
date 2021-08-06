package model.support;

import java.util.ArrayList;
import java.util.List;

public class SupportElementsModel {
    private final ArrayList<String> orderedElementsNames = new ArrayList<>();

    public void addSupportElement(String tableName){
        orderedElementsNames.add(tableName);
    }

    public List<String> getSupportElementsNames(){
        return orderedElementsNames;
    }
}
