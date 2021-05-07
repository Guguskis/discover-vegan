package lt.liutikas.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema = "SEARCH_SERVICE")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long reviewId;
    @Column(nullable = false)
    private Integer userId;
    @Column(nullable = false)
    private ReviewType reviewType;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "VENDOR_PRODUCT_ID")
    private VendorProduct vendorProduct;

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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

    public VendorProduct getVendorProduct() {
        return vendorProduct;
    }

    public void setVendorProduct(VendorProduct vendorProduct) {
        this.vendorProduct = vendorProduct;
    }
}
