package lt.liutikas.service;

import lt.liutikas.assembler.VendorAssembler;
import lt.liutikas.assembler.VendorProductAssembler;
import lt.liutikas.dto.CreateVendorProductDto;
import lt.liutikas.dto.VendorProductDto;
import lt.liutikas.entity.Product;
import lt.liutikas.entity.Vendor;
import lt.liutikas.entity.VendorProduct;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.VendorProductRepository;
import lt.liutikas.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;
    @Mock
    private VendorProductRepository vendorProductRepository;
    @Mock
    private ProductRepository productRepository;

    private VendorService vendorService;

    @Before
    public void setUp() {
        vendorService = new VendorService(
                new VendorAssembler(),
                new VendorProductAssembler(),
                vendorRepository,
                vendorProductRepository,
                productRepository);
    }

    @Test
    public void createVendorProduct_givenExistingProductId_createsProduct() {
        CreateVendorProductDto createVendorProductDto = new CreateVendorProductDto();
        createVendorProductDto.setProductId(10);
        createVendorProductDto.setPrice(2f);

        Vendor vendor = new Vendor() {{
            setVendorId(1);
        }};
        Product product = new Product() {{
            setProductId(1);
            setName("Tofu");
            setProducer("Sun wheat");
            setImageUrl("https://www.test.com/image.png");
        }};

        when(vendorRepository.findById(1))
                .thenReturn(Optional.of(vendor));
        when(productRepository.findById(10))
                .thenReturn(Optional.of(product));
        when(vendorProductRepository.save(any(VendorProduct.class)))
                .thenReturn(new VendorProduct() {{
                    setVendorProductId(1L);
                    setProduct(product);
                    setPrice(2f);
                }});

        VendorProductDto vendorProductDto = vendorService.createProduct(1, createVendorProductDto);

        verify(vendorRepository, times(1))
                .findById(1);
        verify(productRepository, times(1))
                .findById(10);
        verify(vendorProductRepository, times(1))
                .save(any(VendorProduct.class));

        assertEquals(product.getProductId(), vendorProductDto.getProductId());
        assertEquals(product.getName(), vendorProductDto.getName());
        assertEquals(product.getImageUrl(), vendorProductDto.getImageUrl());
        assertEquals(product.getProducer(), vendorProductDto.getProducer());
        assertEquals(createVendorProductDto.getPrice(), vendorProductDto.getPrice());
    }
}