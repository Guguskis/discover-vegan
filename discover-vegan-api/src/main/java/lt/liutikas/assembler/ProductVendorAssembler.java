package lt.liutikas.assembler;

import lt.liutikas.dto.VendorByProductDto;
import lt.liutikas.dto.VendorDto;
import lt.liutikas.dto.VendorProductDto;
import lt.liutikas.model.Product;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import org.springframework.stereotype.Component;

@Component
public class ProductVendorAssembler {

    public VendorByProductDto assemble(VendorProduct vendorProduct) {

        VendorByProductDto vendorByProductDto = new VendorByProductDto();
        Product product = vendorProduct.getProduct();
        Vendor vendor = vendorProduct.getVendor();

        vendorByProductDto.setProduct(assembleVendorProductDto(vendorProduct, product));
        vendorByProductDto.setVendor(assembleVendorDto(vendor));

        return vendorByProductDto;
    }

    private VendorDto assembleVendorDto(Vendor vendor) {
        VendorDto vendorDto = new VendorDto();
        vendorDto.setVendorId(vendor.getVendorId());
        vendorDto.setName(vendor.getName());
        vendorDto.setAddress(vendor.getAddress());
        vendorDto.setVendorType(vendor.getVendorType());
        vendorDto.setLatitude(vendor.getLatitude());
        vendorDto.setLongitude(vendor.getLongitude());
        return vendorDto;
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
