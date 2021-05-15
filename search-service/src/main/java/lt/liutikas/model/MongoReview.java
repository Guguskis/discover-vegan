package lt.liutikas.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document
public class MongoReview {

    @Id
    private String id;
    private String userId;
    private ReviewType reviewType;
    private LocalDateTime createdAt;
    @DBRef
    private MongoVendorProduct vendorProduct;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ReviewType getReviewType() {
        return reviewType;
    }

    public void setReviewType(ReviewType review) {
        this.reviewType = review;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public MongoVendorProduct getVendorProduct() {
        return vendorProduct;
    }

    public void setVendorProduct(MongoVendorProduct vendorProduct) {
        this.vendorProduct = vendorProduct;
    }
}
