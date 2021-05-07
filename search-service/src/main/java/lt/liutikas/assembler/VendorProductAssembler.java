package lt.liutikas.assembler;

import lt.liutikas.dto.CreateVendorProductDto;
import lt.liutikas.dto.VendorProductDto;
import lt.liutikas.model.Product;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import lt.liutikas.model.VendorProductChange;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class VendorProductAssembler {

    public VendorProduct assembleVendorProduct(Integer userId, CreateVendorProductDto createVendorProductDto, Vendor vendor, Product product) {
        VendorProduct vendorProduct = new VendorProduct();

        vendorProduct.setVendor(vendor);
        vendorProduct.setProduct(product);

        VendorProductChange vendorProductChange = new VendorProductChange();
        vendorProductChange.setUserId(userId);
        vendorProductChange.setPrice(createVendorProductDto.getPrice());

        vendorProduct.setVendorProductChanges(Collections.singletonList(vendorProductChange));

        return vendorProduct;
    }

    public VendorProductDto assembleVendorProductDto(VendorProduct vendorProduct) {
        VendorProductDto vendorProductDto = new VendorProductDto();
        Product product = vendorProduct.getProduct();

        vendorProductDto.setProductId(product.getProductId());
        vendorProductDto.setName(product.getName());
        vendorProductDto.setImageUrl(product.getImageUrl());
        vendorProductDto.setProducer(product.getProducer());

        List<VendorProductChange> vendorProductChanges = vendorProduct.getVendorProductChanges();

        if (vendorProductChanges.size() > 1) {
            vendorProductChanges.sort(Comparator.comparing(VendorProductChange::getCreatedAt).reversed());
        }

        vendorProductDto.setPrice(vendorProductChanges.get(0).getPrice());

        return vendorProductDto;
    }
}
