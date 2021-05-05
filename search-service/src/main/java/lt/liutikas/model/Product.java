package lt.liutikas.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "SEARCH_SERVICE")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer productId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String imageUrl;
    private String producer;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private List<VendorProduct> vendorProducts;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer id) {
        this.productId = id;
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

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public List<VendorProduct> getVendorProducts() {
        return vendorProducts;
    }

    public void setVendorProducts(List<VendorProduct> vendorProducts) {
        this.vendorProducts = vendorProducts;
    }
}
