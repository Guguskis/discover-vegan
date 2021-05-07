package lt.liutikas.model;

public enum ReviewType {
    RECOMMENDED("Recommended"),
    NOT_RECOMMENDED("Not_recommended"),
    CANT_FIND("Cant_find"),
    NOT_VEGAN("Not_vegan");

    private final String code;

    ReviewType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
