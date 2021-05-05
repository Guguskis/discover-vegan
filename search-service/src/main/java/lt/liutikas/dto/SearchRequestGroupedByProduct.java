package lt.liutikas.dto;

import lt.liutikas.model.Product;

public class SearchRequestGroupedByProduct {

    private Product product;
    private Long count;

    public SearchRequestGroupedByProduct() {
    }

    public SearchRequestGroupedByProduct(Product product, Long count) {
        this.product = product;
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
