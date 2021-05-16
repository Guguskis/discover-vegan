package lt.liutikas.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class VendorProduct {

    @Id
    private String id;
    @DBRef
    private Vendor vendor;
    @DBRef
    private Product product;
    private List<VendorProductChange> vendorProductChanges;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<VendorProductChange> getVendorProductChanges() {
        return vendorProductChanges;
    }

    public void setVendorProductChanges(List<VendorProductChange> vendorProductChanges) {
        this.vendorProductChanges = vendorProductChanges;
    }
}
