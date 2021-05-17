package lt.liutikas.service;

import lt.liutikas.assembler.ProductAssembler;
import lt.liutikas.assembler.ProductVendorAssembler;
import lt.liutikas.assembler.VendorAssembler;
import lt.liutikas.assembler.VendorProductAssembler;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.CreateProductDto;
import lt.liutikas.dto.ProductDto;
import lt.liutikas.dto.ProductVendorPage;
import lt.liutikas.dto.ProductsPageDto;
import lt.liutikas.dto.VendorByProductDto;
import lt.liutikas.dto.VendorDto;
import lt.liutikas.dto.VendorProductDto;
import lt.liutikas.model.Product;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import lt.liutikas.model.VendorProductChange;
import lt.liutikas.model.VendorType;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.SearchRequestRepository;
import lt.liutikas.repository.VendorProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private VendorProductRepository vendorProductRepository;
    @Mock
    private SearchRequestRepository searchRequestRepository;

    private ProductService productService;

    @Before
    public void setUp() {
        productService = new ProductService(new ProductAssembler(),
                productRepository,
                vendorProductRepository,
                searchRequestRepository,
                new ProductVendorAssembler(new VendorAssembler(), new VendorProductAssembler()));
    }

    @Test
    public void createProduct_providedData_returnsProduct() {

        CreateProductDto createProductDto = new CreateProductDto() {{
            setName("Tofu");
            setProducer("Sun wheat");
        }};

        Product product = new Product() {{
            setId("1");
            setName("Tofu");
            setProducer("Sun wheat");
            setImageUrl("https://www.test.com/image.png");
        }};

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        ProductDto createdProduct = productService.createProduct(createProductDto);

        assertEquals(product.getId(), createdProduct.getProductId());
        assertEquals(product.getName(), createdProduct.getName());
        assertEquals(product.getProducer(), createdProduct.getProducer());
        assertEquals(product.getImageUrl(), createdProduct.getImageUrl());
    }

    @Test(expected = BadRequestException.class)
    public void createProduct_providedOnlyName_throwsBadRequestException() {

        CreateProductDto createProductDto = new CreateProductDto() {{
            setName("Tofu");
        }};

        when(productRepository.save(any(Product.class)))
                .thenThrow(DataIntegrityViolationException.class);

        productService.createProduct(createProductDto);
    }

    @Test
    public void getProducts_singleProduct_returnsProduct() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Product product = new Product() {{
            setId("1");
            setName("Tofu");
            setProducer("Sun wheat");
            setImageUrl("https://www.test.com/image.png");
        }};

        List<Product> products = Collections.singletonList(product);
        when(productRepository.findByNameRegexOrderByNameAsc(any(PageRequest.class), any()))
                .thenReturn(new PageImpl<>(products, pageRequest, products.size()));

        ProductsPageDto productsPageDto = productService.getProducts(pageRequest, "test query");
        assertEquals(1, productsPageDto.getProducts().size());
        ProductDto returnedProduct = productsPageDto.getProducts().get(0);

        assertEquals(product.getId(), returnedProduct.getProductId());
        assertEquals(product.getName(), returnedProduct.getName());
        assertEquals(product.getProducer(), returnedProduct.getProducer());
        assertEquals(product.getImageUrl(), returnedProduct.getImageUrl());
    }

    @Test
    public void getProducts_multipleProducts_returnsMultipleProducts() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<Product> products = Arrays.asList(
                new Product(),
                new Product(),
                new Product()
        );

        when(productRepository.findByNameRegexOrderByNameAsc(any(PageRequest.class), anyString()))
                .thenReturn(new PageImpl<>(products, pageRequest, 3));

        ProductsPageDto productsPageDto = productService.getProducts(pageRequest, "test query");

        assertEquals(3, productsPageDto.getProducts().size());
    }

    @Test
    public void getProducts_hasNextPage_returnsNextPageToken() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        List<Product> products = Arrays.asList(
                new Product(),
                new Product()
        );

        when(productRepository.findByNameRegexOrderByNameAsc(any(PageRequest.class), anyString()))
                .thenReturn(new PageImpl<>(products, pageRequest, 2));

        ProductsPageDto productsPageDto = productService.getProducts(pageRequest, "test query");

        assertNotNull(productsPageDto.getNextPageToken());
    }

    @Test
    public void getProducts_doesntHaveNextPage_nextPageTokenIsNull() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Product> products = Arrays.asList(
                new Product(),
                new Product()
        );

        when(productRepository.findByNameRegexOrderByNameAsc(any(PageRequest.class), anyString()))
                .thenReturn(new PageImpl<>(products, pageRequest, 2));

        ProductsPageDto productsPageDto = productService.getProducts(pageRequest, "test query");

        assertNull(productsPageDto.getNextPageToken());
    }

    @Test
    public void getVendorsAndProductDetails_singleDetail_returnsSingleDetail() {
        String productId = "5";
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(productRepository.findById(anyString()))
                .thenReturn(Optional.of(new Product()));

        Product product = createProduct();
        Vendor vendor = createVendor();

        VendorProduct vendorProduct = new VendorProduct() {{
            setProduct(product);
            setVendor(vendor);
        }};

        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(12f, 1))
        );

        List<VendorProduct> vendorProducts = Collections.singletonList(vendorProduct);

        when(vendorProductRepository.findAllByProduct(any(Product.class), eq(pageRequest)))
                .thenReturn(new PageImpl<>(vendorProducts, pageRequest, 2));

        ProductVendorPage productsPageDto = productService.getVendorsAndProductDetails(pageRequest, productId);
        List<VendorByProductDto> details = productsPageDto.getDetails();

        VendorByProductDto vendorByProductDto = details.get(0);
        VendorProductDto returnedProduct = vendorByProductDto.getProduct();
        VendorDto returnedVendor = vendorByProductDto.getVendor();

        assertEquals(product.getId(), returnedProduct.getProductId());
        assertEquals(product.getName(), returnedProduct.getName());
        assertEquals(product.getProducer(), returnedProduct.getProducer());
        assertEquals(product.getImageUrl(), returnedProduct.getImageUrl());
        assertEquals(vendor.getId(), returnedVendor.getVendorId());
        assertEquals(vendor.getVendorType(), returnedVendor.getVendorType());
        assertEquals(vendor.getName(), returnedVendor.getName());
        assertEquals(vendor.getAddress(), returnedVendor.getAddress());
        assertEquals(vendor.getLatitude(), returnedVendor.getLatitude());
        assertEquals(vendor.getLongitude(), returnedVendor.getLongitude());
    }

    @Test
    public void getVendorsAndProductDetails_multipleDetails_returnsMultipleDetails() {
        String productId = "5";
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(productRepository.findById(anyString()))
                .thenReturn(Optional.of(new Product()));

        Product product = createProduct();

        Vendor vendor = createVendor();

        VendorProduct vendorProduct = new VendorProduct() {{
            setProduct(product);
            setVendor(vendor);
        }};

        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(12f, 1))
        );

        List<VendorProduct> vendorProducts = Arrays.asList(
                vendorProduct,
                vendorProduct,
                vendorProduct
        );

        when(vendorProductRepository.findAllByProduct(any(Product.class), eq(pageRequest)))
                .thenReturn(new PageImpl<>(vendorProducts, pageRequest, 2));

        ProductVendorPage productsPageDto = productService.getVendorsAndProductDetails(pageRequest, productId);
        List<VendorByProductDto> details = productsPageDto.getDetails();

        assertEquals(3, details.size());
    }

    @Test
    public void getVendorsAndProductDetails_multipleChanges_returnsLatestPrice() {
        String productId = "5";
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(productRepository.findById(anyString()))
                .thenReturn(Optional.of(new Product()));

        Product product = createProduct();

        Vendor vendor = createVendor();

        VendorProduct vendorProduct = new VendorProduct() {{
            setProduct(product);
            setVendor(vendor);
        }};

        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(1f, 2),
                createVendorProductChange(5f, 3),
                createVendorProductChange(10f, 1)
        ));

        List<VendorProduct> vendorProducts = Arrays.asList(
                vendorProduct,
                vendorProduct,
                vendorProduct
        );

        when(vendorProductRepository.findAllByProduct(any(Product.class), eq(pageRequest)))
                .thenReturn(new PageImpl<>(vendorProducts, pageRequest, 2));

        ProductVendorPage productsPageDto = productService.getVendorsAndProductDetails(pageRequest, productId);
        List<VendorByProductDto> details = productsPageDto.getDetails();

        VendorByProductDto vendorByProductDto = details.get(0);
        VendorProductDto returnedProduct = vendorByProductDto.getProduct();

        assertEquals((Float) 5f, returnedProduct.getPrice());
    }


    @Test(expected = NotFoundException.class)
    public void getVendorsAndProductDetails_nonExistingProduct_throwsNotFoundException() {
        String productId = "5";
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(productRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        productService.getVendorsAndProductDetails(pageRequest, productId);
    }

    @Test
    public void getVendorsAndProductDetails_hasNextPage_returnsNextPageToken() {
        String productId = "5";
        PageRequest pageRequest = PageRequest.of(0, 1);

        when(productRepository.findById(anyString()))
                .thenReturn(Optional.of(new Product()));

        Product product = createProduct();

        Vendor vendor = createVendor();

        VendorProduct vendorProduct = new VendorProduct() {{
            setProduct(product);
            setVendor(vendor);
        }};

        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(12f, 1))
        );

        List<VendorProduct> vendorProducts = Arrays.asList(
                vendorProduct,
                vendorProduct,
                vendorProduct
        );

        when(vendorProductRepository.findAllByProduct(any(Product.class), eq(pageRequest)))
                .thenReturn(new PageImpl<>(vendorProducts, pageRequest, 2));

        ProductVendorPage productsPageDto = productService.getVendorsAndProductDetails(pageRequest, productId);

        assertEquals((Integer) 1, productsPageDto.getNextPageToken());
    }

    @Test
    public void getVendorsAndProductDetails_doesntHaveNextPage_nextPageTokenIsNull() {
        String productId = "5";
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(productRepository.findById(anyString()))
                .thenReturn(Optional.of(new Product()));

        Product product = createProduct();

        Vendor vendor = createVendor();

        VendorProduct vendorProduct = new VendorProduct() {{
            setProduct(product);
            setVendor(vendor);
        }};

        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(12f, 1))
        );

        List<VendorProduct> vendorProducts = Arrays.asList(
                vendorProduct,
                vendorProduct,
                vendorProduct
        );

        when(vendorProductRepository.findAllByProduct(any(Product.class), eq(pageRequest)))
                .thenReturn(new PageImpl<>(vendorProducts, pageRequest, 2));

        ProductVendorPage productsPageDto = productService.getVendorsAndProductDetails(pageRequest, productId);

        assertNull(productsPageDto.getNextPageToken());
    }

    private VendorProductChange createVendorProductChange(float price, int day) {
        return new VendorProductChange() {{
            setPrice(price);
            setCreatedAt(LocalDateTime.of(2020, 1, day, 1, 1));
        }};
    }

    private Vendor createVendor() {
        return new Vendor() {{
            setId("1");
            setName("Iki");
            setAddress("test address");
            setExternalPlaceId("ext");
            setLatitude(4.);
            setLongitude(20.);
            setVendorType(VendorType.STORE);
            setImageUrl("asd");
        }};
    }

    private Product createProduct() {
        Product product = new Product() {{
            setId("1");
            setName("Tofu");
            setProducer("Sun wheat");
            setImageUrl("https://www.test.com/image.png");
        }};
        return product;
    }

}