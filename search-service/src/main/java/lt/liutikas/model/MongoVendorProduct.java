package lt.liutikas.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document
public class MongoVendorProduct {

    @Id
    private String id;
    @DBRef
    private MongoVendor vendor;
    @DBRef
    private MongoProduct product;
    private List<MongoVendorProductChange> vendorProductChanges;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MongoVendor getVendor() {
        return vendor;
    }

    public void setVendor(MongoVendor vendor) {
        this.vendor = vendor;
    }

    public MongoProduct getProduct() {
        return product;
    }

    public void setProduct(MongoProduct product) {
        this.product = product;
    }

    public List<MongoVendorProductChange> getVendorProductChanges() {
        return vendorProductChanges;
    }

    public void setVendorProductChanges(List<MongoVendorProductChange> vendorProductChanges) {
        this.vendorProductChanges = vendorProductChanges;
    }
}
