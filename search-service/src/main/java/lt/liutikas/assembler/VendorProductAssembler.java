package lt.liutikas.assembler;

import lt.liutikas.dto.CreateVendorProductDto;
import lt.liutikas.dto.VendorProductDto;
import lt.liutikas.model.Product;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import org.springframework.stereotype.Component;

@Component
public class VendorProductAssembler {

    public VendorProduct assembleVendorProduct(CreateVendorProductDto createVendorProductDto, Vendor vendor, Product product) {
        VendorProduct vendorProduct = new VendorProduct();

        vendorProduct.setVendor(vendor);
        vendorProduct.setProduct(product);
        vendorProduct.setPrice(createVendorProductDto.getPrice());

        return vendorProduct;
    }

    public VendorProductDto assembleVendorProductDto(VendorProduct vendorProduct) {
        VendorProductDto vendorProductDto = new VendorProductDto();
        Product product = vendorProduct.getProduct();

        vendorProductDto.setProductId(product.getProductId());
        vendorProductDto.setName(product.getName());
        vendorProductDto.setImageUrl(product.getImageUrl());
        vendorProductDto.setProducer(product.getProducer());
        vendorProductDto.setPrice(vendorProduct.getPrice());

        return vendorProductDto;
    }
}
