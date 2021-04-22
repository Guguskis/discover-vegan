package lt.liutikas.dto;

import lt.liutikas.entity.VendorType;

public class GetVendorDto {

    private Location location;
    private VendorType type;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public VendorType getType() {
        return type;
    }

    public void setType(VendorType type) {
        this.type = type;
    }
}
