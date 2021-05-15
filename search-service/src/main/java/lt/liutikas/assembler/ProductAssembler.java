package lt.liutikas.assembler;

import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.dto.ProductDto;
import lt.liutikas.model.MongoProduct;
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

    public MongoProduct assembleMongoProduct(CreateProductDto createProductDto) {
        MongoProduct product = new MongoProduct();

        product.setName(createProductDto.getName());
        product.setImageUrl(createProductDto.getImageUrl());
        product.setProducer(createProductDto.getProducer());

        return product;
    }

    public ProductDto assembleProduct(Product product) {
        ProductDto productDto = new ProductDto();

//        productDto.setProductId(product.getProductId());
        productDto.setName(product.getName());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setProducer(product.getProducer());

        return productDto;
    }

    public ProductDto assembleMongoProduct(MongoProduct product) {
        ProductDto productDto = new ProductDto();

        productDto.setProductId(product.getId());
        productDto.setName(product.getName());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setProducer(product.getProducer());

        return productDto;
    }

}
