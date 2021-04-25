package lt.liutikas.model;

import javax.persistence.*;

@Entity
@Table(schema = "DISCOVER_VEGAN_API")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer productId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String imageUrl;
    private String producer;

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
}
