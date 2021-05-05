package lt.liutikas.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema = "SEARCH_SERVICE")
public class SearchRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long searchRequestId;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    public Long getSearchRequestId() {
        return searchRequestId;
    }

    public void setSearchRequestId(Long searchRequestId) {
        this.searchRequestId = searchRequestId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
