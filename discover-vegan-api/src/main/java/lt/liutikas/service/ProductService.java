package lt.liutikas.service;

import lt.liutikas.assembler.ProductAssembler;
import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.entity.Product;
import lt.liutikas.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final ProductAssembler productAssembler;
    private final ProductRepository productRepository;

    public ProductService(ProductAssembler productAssembler, ProductRepository productRepository) {
        this.productAssembler = productAssembler;
        this.productRepository = productRepository;
    }

    public List<Product> getProducts(String name) {
        LOG.info(String.format("Returned products {name: \"%s\"}", name));

        return Arrays.asList(
                new Product() {{
                    setProductId(1);
                    setName("Tofu A");
                    setImageUrl("https://www.veggo.lt/991-home_default/organic-tofu-picknicker-50g-viana.jpg");
                    setProducer("Saulės grūdas");
                }},
                new Product() {{
                    setProductId(2);
                    setName("Tofu B");
                    setImageUrl("https://www.veggo.lt/147-home_default/ekologiskas-keptas-tempeh.jpg");
                    setProducer("Saulės grūdas");
                }},
                new Product() {{
                    setProductId(3);
                    setName("Tofu C");
                    setImageUrl("https://www.veggo.lt/839-home_default/ekologiskas-fermentuotas-tofu-su-laiskiniais-cesnakais-130g-lord-of-tofu.jpg");
                    setProducer("Saulės grūdas");
                }},
                new Product() {{
                    setProductId(4);
                    setName("Tofu D");
                    setImageUrl("https://www.veggo.lt/549-home_default/ekologiskas-silkinis-tofu.jpg");
                    setProducer("Saulės grūdas");
                }},
                new Product() {{
                    setProductId(5);
                    setName("Tofu E");
                    setImageUrl("https://www.veggo.lt/991-home_default/organic-tofu-picknicker-50g-viana.jpg");
                    setProducer("Saulės grūdas");
                }},
                new Product() {{
                    setProductId(6);
                    setName("Tofu F");
                    setImageUrl("https://www.veggo.lt/147-home_default/ekologiskas-keptas-tempeh.jpg");
                    setProducer("Saulės grūdas");
                }}
        );
    }

    public Product createProduct(CreateProductDto createProductDto) {
        Product product = productAssembler.assembleProduct(createProductDto);
        product = productRepository.save(product);
        return product;
    }
}
