package lt.liutikas.dto;

public class VendorByProductDto {

    private VendorDto vendor;
    private VendorProductDto product;

    public VendorDto getVendor() {
        return vendor;
    }

    public void setVendor(VendorDto vendor) {
        this.vendor = vendor;
    }

    public VendorProductDto getProduct() {
        return product;
    }

    public void setProduct(VendorProductDto product) {
        this.product = product;
    }
}
