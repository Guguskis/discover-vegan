package lt.liutikas.assembler;

import lt.liutikas.dto.VendorByProductDto;
import lt.liutikas.dto.VendorProductDto;
import lt.liutikas.model.Product;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import org.springframework.stereotype.Component;

@Component
public class ProductVendorAssembler {

    private final VendorAssembler vendorAssembler;

    public ProductVendorAssembler(VendorAssembler vendorAssembler) {
        this.vendorAssembler = vendorAssembler;
    }

    public VendorByProductDto assemble(VendorProduct vendorProduct) {

        VendorByProductDto vendorByProductDto = new VendorByProductDto();
        Product product = vendorProduct.getProduct();
        Vendor vendor = vendorProduct.getVendor();

        vendorByProductDto.setProduct(assembleVendorProductDto(vendorProduct, product));
        vendorByProductDto.setVendor(vendorAssembler.assembleVendor(vendor));

        return vendorByProductDto;
    }

    private VendorProductDto assembleVendorProductDto(VendorProduct vendorProduct, Product product) {
        VendorProductDto vendorProductDto = new VendorProductDto();
        vendorProductDto.setProductId(product.getProductId());
        vendorProductDto.setName(product.getName());
        vendorProductDto.setImageUrl(product.getImageUrl());
        vendorProductDto.setProducer(product.getProducer());
        vendorProductDto.setPrice(vendorProduct.getPrice());
        return vendorProductDto;
    }
}
