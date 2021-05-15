package lt.liutikas.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class CreateVendorProductDto {

    @NotNull
    private String productId;
    @NotNull
    @DecimalMin("0.01")
    private Float price;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
