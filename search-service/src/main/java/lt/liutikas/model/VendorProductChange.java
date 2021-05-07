package lt.liutikas.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema = "SEARCH_SERVICE")
public class VendorProductChange {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long vendorProductChangeId;
    @Column(nullable = false)
    private Integer userId;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private Float price;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "VENDOR_PRODUCT_ID")
    private VendorProduct vendorProduct;

    public Long getVendorProductChangeId() {
        return vendorProductChangeId;
    }

    public void setVendorProductChangeId(Long vendorProductChangeId) {
        this.vendorProductChangeId = vendorProductChangeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public VendorProduct getVendorProduct() {
        return vendorProduct;
    }

    public void setVendorProduct(VendorProduct vendorProduct) {
        this.vendorProduct = vendorProduct;
    }
}
