package lt.liutikas.model;

public enum UserType {
    BASIC("Basic"), CUSTOMER("Customer");

    private final String code;

    public String getCode() {
        return code;
    }

    UserType(String code) {
        this.code = code;
    }
}
