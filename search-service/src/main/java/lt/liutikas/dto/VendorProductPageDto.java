package lt.liutikas.dto;

import java.util.List;

public class VendorProductPageDto extends PaginationResponse {

    private List<VendorProductDto> products;

    public List<VendorProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<VendorProductDto> products) {
        this.products = products;
    }
}
