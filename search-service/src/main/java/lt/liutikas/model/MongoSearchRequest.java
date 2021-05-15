package lt.liutikas.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document
public class MongoSearchRequest {

    @Id
    private String id;
    private LocalDateTime createdAt;
    @DBRef
    private MongoProduct product;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public MongoProduct getProduct() {
        return product;
    }

    public void setProduct(MongoProduct product) {
        this.product = product;
    }
}
