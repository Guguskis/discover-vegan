package lt.liutikas.assembler;

import lt.liutikas.dto.CreateVendorProductDto;
import lt.liutikas.dto.VendorProductDto;
import lt.liutikas.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class VendorProductAssembler {

    public MongoVendorProduct assembleVendorProduct(String userId, CreateVendorProductDto createVendorProductDto, MongoVendor vendor, MongoProduct product) {
        MongoVendorProduct vendorProduct = new MongoVendorProduct();

        vendorProduct.setVendor(vendor);
        vendorProduct.setProduct(product);

        MongoVendorProductChange vendorProductChange = new MongoVendorProductChange();
        vendorProductChange.setUserId(userId);
        vendorProductChange.setPrice(createVendorProductDto.getPrice());
        vendorProductChange.setCreatedAt(LocalDateTime.now());

        vendorProduct.setVendorProductChanges(Collections.singletonList(vendorProductChange));

        return vendorProduct;
    }

    public VendorProductDto assembleVendorProductDto(VendorProduct vendorProduct) {
        VendorProductDto vendorProductDto = new VendorProductDto();
        Product product = vendorProduct.getProduct();

//        vendorProductDto.setProductId(product.getProductId());
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

    public VendorProductDto assembleVendorProductDto(MongoVendorProduct vendorProduct) {
        VendorProductDto vendorProductDto = new VendorProductDto();
        MongoProduct product = vendorProduct.getProduct();

        vendorProductDto.setProductId(product.getId());
        vendorProductDto.setName(product.getName());
        vendorProductDto.setImageUrl(product.getImageUrl());
        vendorProductDto.setProducer(product.getProducer());

        List<MongoVendorProductChange> vendorProductChanges = vendorProduct.getVendorProductChanges();

        if (vendorProductChanges.size() > 1) {
            vendorProductChanges.sort(Comparator.comparing(MongoVendorProductChange::getCreatedAt).reversed());
        }

        vendorProductDto.setPrice(vendorProductChanges.get(0).getPrice());

        return vendorProductDto;
    }
}
