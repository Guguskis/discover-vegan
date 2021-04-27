package lt.liutikas.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "DISCOVER_VEGAN_API")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer vendorId;
    @Column(nullable = false)
    private String externalPlaceId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;
    private String address;
    private String imageUrl;
    @Column(nullable = false)
    private VendorType vendorType;
    @OneToMany
    @JoinColumn(name = "VENDOR_ID")
    private List<VendorProduct> vendorProducts;

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer id) {
        this.vendorId = id;
    }

    public String getExternalPlaceId() {
        return externalPlaceId;
    }

    public void setExternalPlaceId(String externalPlaceId) {
        this.externalPlaceId = externalPlaceId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public VendorType getVendorType() {
        return vendorType;
    }

    public void setVendorType(VendorType vendorType) {
        this.vendorType = vendorType;
    }

    public List<VendorProduct> getVendorProducts() {
        return vendorProducts;
    }

    public void setVendorProducts(List<VendorProduct> vendorProducts) {
        this.vendorProducts = vendorProducts;
    }
}
