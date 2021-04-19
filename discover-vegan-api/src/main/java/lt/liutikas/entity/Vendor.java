package lt.liutikas.entity;

import javax.persistence.*;

@Entity
@Table(schema = "DISCOVER_VEGAN_API")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer vendorId;
    private String name;
    private String imageUrl;
    private Double latitude;
    private Double longitude;

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer id) {
        this.vendorId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
