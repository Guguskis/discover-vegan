package lt.liutikas.service;

import lt.liutikas.assembler.ProductAssembler;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.entity.Product;
import lt.liutikas.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @Before
    public void setUp() {
        productService = new ProductService(new ProductAssembler(), productRepository);
    }

    @Test
    public void createProduct_givenAllData_createdProduct() {

        CreateProductDto createProductDto = new CreateProductDto() {{
            setName("Tofu");
            setProducer("Sun wheat");
        }};

        Product product = new Product() {{
            setProductId(1);
            setName("Tofu");
            setProducer("Sun wheat");
            setImageUrl("https://www.test.com/image.png");
        }};

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        Product createdProduct = productService.createProduct(createProductDto);

        verify(productRepository, times(1))
                .save(any(Product.class));

        assertEquals(product.getProductId(), createdProduct.getProductId());
        assertEquals(product.getName(), createdProduct.getName());
        assertEquals(product.getProducer(), createdProduct.getProducer());
        assertEquals(product.getImageUrl(), createdProduct.getImageUrl());
    }

    @Test(expected = BadRequestException.class)
    public void createProduct_providedInvalidDataModel_throwsBadRequestException() {

        CreateProductDto createProductDto = new CreateProductDto() {{
            setName("Tofu");
        }};

        when(productRepository.save(any(Product.class)))
                .thenThrow(DataIntegrityViolationException.class);

        productService.createProduct(createProductDto);
    }

}