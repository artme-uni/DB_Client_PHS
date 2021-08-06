package model.properties;

public class PropertyInfo {
    private final String propertyName;
    private final String propertyTitle;
    private final PropertyType propertyType;

    private String referencedTableName;

    public PropertyInfo(String propertyName, String propertyTitle, PropertyType propertyType) {
        this.propertyName = propertyName;
        this.propertyTitle = propertyTitle;
        this.propertyType = propertyType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void addReferencedTable(String tableName){
        if(!propertyType.equals(PropertyType.REFERENCE)){
            throw new UnsupportedOperationException();
        }
        referencedTableName = tableName;
    }

    public String getReferencedTable(){
        if(!propertyType.equals(PropertyType.REFERENCE)){
            throw new UnsupportedOperationException();
        }
        return referencedTableName;
    }
}
