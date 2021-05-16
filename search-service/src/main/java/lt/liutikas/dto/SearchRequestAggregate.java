package lt.liutikas.dto;

import lt.liutikas.model.Product;
import org.springframework.data.annotation.Id;

public class SearchRequestAggregate {

    @Id
    private Product product;
    private int count;

    public SearchRequestAggregate(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
