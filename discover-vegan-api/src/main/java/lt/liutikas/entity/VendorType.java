package lt.liutikas.entity;

public enum VendorType {
    RESTAURANT("Restaurant"), STORE("Store");

    private final String code;

    public String getCode() {
        return code;
    }

    VendorType(String code) {
        this.code = code;
    }
}
