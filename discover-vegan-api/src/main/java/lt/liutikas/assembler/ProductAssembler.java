package lt.liutikas.assembler;

import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductAssembler {

    public Product assembleProduct(CreateProductDto createProductDto) {
        Product product = new Product();

        product.setName(createProductDto.getName());
        product.setImageUrl(createProductDto.getImageUrl());
        product.setProducer(createProductDto.getProducer());

        return product;
    }

}
