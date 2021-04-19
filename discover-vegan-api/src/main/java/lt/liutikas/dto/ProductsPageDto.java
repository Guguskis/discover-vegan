package lt.liutikas.dto;

import lt.liutikas.entity.Product;

import java.util.List;

public class ProductsPageDto extends PaginationResponse {

    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
