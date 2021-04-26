package lt.liutikas.dto;

import java.util.List;

public class ProductVendorPage extends PaginationResponse {

    private List<VendorByProductDto> details;

    public List<VendorByProductDto> getDetails() {
        return details;
    }

    public void setDetails(List<VendorByProductDto> details) {
        this.details = details;
    }
}
