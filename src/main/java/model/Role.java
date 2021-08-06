package model;

public enum Role {
    ADMINISTRATOR("Администратор"),
    BUYER("Закупщик"),
    HR_MANAGER("Менеджер по персоналу"),
    ACCOUNTANT("Бухгалтер");

    private final String roleLabel;

    Role(String roleLabel) {
        this.roleLabel = roleLabel;
    }

    @Override
    public String toString() {
        return roleLabel;
    }
}
