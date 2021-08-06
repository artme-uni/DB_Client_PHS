package model.properties;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class MappedPropertiesBundle<T> extends PropertiesBundle{
    private final HashMap<String, T> propertiesMappingValues = new HashMap<>();

    public MappedPropertiesBundle(String bundleName) {
        super(bundleName);
    }

    public void mapProperty(String propertyName, T mappedValue){
        if (propertiesMappingValues.containsKey(propertyName)){
            throw new IllegalArgumentException("Property with name " + propertyName + "has already mapped");
        }
        propertiesMappingValues.put(propertyName, mappedValue);
    }

    public void mapProperties(List<String> propertiesNames, T mappedValue){
        for(String propertyName : propertiesNames){
            mapProperty(propertyName, mappedValue);
        }
    }

    public T getMappedValues(String propertyName){
        if (!propertiesMappingValues.containsKey(propertyName)){
            throw new NoSuchElementException("Property with name " + propertyName + "does not exist");
        }
        return propertiesMappingValues.get(propertyName);
    }
}
