package lt.liutikas.dto;

import lt.liutikas.model.MongoProduct;
import org.springframework.data.annotation.Id;

public class SearchRequestAggregate {

    @Id
    private MongoProduct product;
    private int count;

    public SearchRequestAggregate(MongoProduct product, int count) {
        this.product = product;
        this.count = count;
    }

    public MongoProduct getProduct() {
        return product;
    }

    public void setProduct(MongoProduct product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
