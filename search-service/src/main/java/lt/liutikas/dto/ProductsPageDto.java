package lt.liutikas.dto;

import java.util.List;

public class ProductsPageDto extends PaginationResponse {

    private List<ProductDto> products;

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
