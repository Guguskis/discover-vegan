package lt.liutikas.assembler;

import lt.liutikas.dto.VendorByProductDto;
import lt.liutikas.model.MongoVendor;
import lt.liutikas.model.MongoVendorProduct;
import org.springframework.stereotype.Component;

@Component
public class ProductVendorAssembler {

    private final VendorAssembler vendorAssembler;
    private final VendorProductAssembler vendorProductAssembler;

    public ProductVendorAssembler(VendorAssembler vendorAssembler, VendorProductAssembler vendorProductAssembler) {
        this.vendorAssembler = vendorAssembler;
        this.vendorProductAssembler = vendorProductAssembler;
    }

    public VendorByProductDto assemble(MongoVendorProduct vendorProduct) {

        VendorByProductDto vendorByProductDto = new VendorByProductDto();
        MongoVendor vendor = vendorProduct.getVendor();

        vendorByProductDto.setProduct(vendorProductAssembler.assembleVendorProductDto(vendorProduct));
        vendorByProductDto.setVendor(vendorAssembler.assembleVendor(vendor));

        return vendorByProductDto;
    }

}
