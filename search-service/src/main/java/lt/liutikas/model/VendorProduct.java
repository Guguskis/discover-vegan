package lt.liutikas.model;

import javax.persistence.*;

@Entity
@Table(schema = "SEARCH_SERVICE")
public class VendorProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long vendorProductId;

    @ManyToOne
    @JoinColumn(name = "VENDOR_ID")
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private Float price;

    public Long getVendorProductId() {
        return vendorProductId;
    }

    public void setVendorProductId(Long vendorProductId) {
        this.vendorProductId = vendorProductId;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
