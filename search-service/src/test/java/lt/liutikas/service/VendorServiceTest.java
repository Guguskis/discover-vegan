package lt.liutikas.service;

import lt.liutikas.assembler.VendorAssembler;
import lt.liutikas.assembler.VendorProductAssembler;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.CreateVendorProductDto;
import lt.liutikas.dto.VendorProductDto;
import lt.liutikas.dto.VendorProductPageDto;
import lt.liutikas.repository.PlaceRepository;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.VendorProductRepository;
import lt.liutikas.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;
    @Mock
    private VendorProductRepository vendorProductRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PlaceRepository placeRepository;

    private VendorService vendorService;

    @Before
    public void setUp() {
        vendorService = new VendorService(
                new VendorAssembler(),
                new VendorProductAssembler(),
                vendorRepository,
                placeRepository,
                productRepository,
                vendorProductRepository);
    }

    @Test
    public void createVendorProduct_givenExistingProductId_createsProduct() {
        CreateVendorProductDto createVendorProductDto = new CreateVendorProductDto();
        createVendorProductDto.setProductId("10");
        createVendorProductDto.setPrice(2f);

//        Vendor vendor = new Vendor() {{
//            setVendorId(1);
//        }};
//        Product product = new Product() {{
//            setProductId(1);
//            setName("Tofu");
//            setProducer("Sun wheat");
//            setImageUrl("https://www.test.com/image.png");
//        }};

//        when(vendorRepository.findById(1))
//                .thenReturn(Optional.of(vendor));
//        when(productRepository.findById(10))
//                .thenReturn(Optional.of(product));
//        when(vendorProductRepository.save(any(VendorProduct.class)))
//                .thenReturn(new VendorProduct() {{
//                    setVendorProductId(1L);
//                    setProduct(product);
//                }});

        VendorProductDto vendorProductDto = vendorService.createProduct("1", "1", createVendorProductDto);

//        verify(vendorRepository, times(1))
//                .findById(1);
//        verify(productRepository, times(1))
//                .findById(10);
//        verify(vendorProductRepository, times(1))
//                .save(any(VendorProduct.class));

//        assertEquals(product.getProductId(), vendorProductDto.getProductId());
//        assertEquals(product.getName(), vendorProductDto.getName());
//        assertEquals(product.getImageUrl(), vendorProductDto.getImageUrl());
//        assertEquals(product.getProducer(), vendorProductDto.getProducer());
//        assertEquals(createVendorProductDto.getPrice(), vendorProductDto.getPrice());
    }

    @Test(expected = NotFoundException.class)
    public void createVendorProduct_givenNonExistingVendorId_throwsNotFound() {
        CreateVendorProductDto createVendorProductDto = new CreateVendorProductDto();
        createVendorProductDto.setProductId("10");
        createVendorProductDto.setPrice(2f);

//        when(vendorRepository.findById(1))
//                .thenReturn(Optional.empty());

        vendorService.createProduct("1", "1", createVendorProductDto);

//        verify(vendorRepository, times(1))
//                .findById(1);
    }

    @Test(expected = NotFoundException.class)
    public void createVendorProduct_givenNonExistingProductId_throwsNotFound() {
        CreateVendorProductDto createVendorProductDto = new CreateVendorProductDto();
        createVendorProductDto.setProductId("10");
        createVendorProductDto.setPrice(2f);

//        when(vendorRepository.findById(1))
//                .thenReturn(Optional.of(new Vendor()));
//        when(productRepository.findById(10))
//                .thenReturn(Optional.empty());

        vendorService.createProduct("1", "1", createVendorProductDto);

//        verify(vendorRepository, times(1))
//                .findById(1);
//        verify(productRepository, times(1))
//                .findById(10);
    }

    @Test
    public void getProducts_queriedOneProduct_returnsFullyMappedProduct() {
        PageRequest pageRequest = PageRequest.of(0, 1);
//        Product product = new Product() {{
//            setProductId(1);
//            setName("Tofu");
//            setProducer("Sun wheat");
//            setImageUrl("https://www.test.com/image.png");
//        }};
//        VendorProduct vendorProduct = new VendorProduct() {{
//            setVendorProductId(1L);
//            setProduct(product);
//        }};
//
//        List<VendorProduct> vendorProducts = Collections.singletonList(vendorProduct);
//        when(vendorRepository.findById(10))
//                .thenReturn(Optional.of(new Vendor()));
//        when(vendorProductRepository.findAllByVendor(any(Vendor.class), any(PageRequest.class)))
//                .thenReturn(new PageImpl<>(vendorProducts, pageRequest, vendorProducts.size()));

        VendorProductPageDto vendorProductPageDto = vendorService.getProducts("10", pageRequest);
        assertEquals(1, vendorProductPageDto.getProducts().size());
        VendorProductDto returnedVendorProductDto = vendorProductPageDto.getProducts().get(0);

//        assertEquals(product.getProductId(), returnedVendorProductDto.getProductId());
//        assertEquals(product.getName(), returnedVendorProductDto.getName());
//        assertEquals(product.getImageUrl(), returnedVendorProductDto.getImageUrl());
//        assertEquals(product.getProducer(), returnedVendorProductDto.getProducer());
    }

    @Test
    public void getProducts_pageSizeSmallerThanProductCount_returnedNextPageToken() {
        PageRequest pageRequest = PageRequest.of(0, 1);
//        Product product = new Product() {{
//            setProductId(1);
//            setName("Tofu");
//            setProducer("Sun wheat");
//            setImageUrl("https://www.test.com/image.png");
//        }};
//        VendorProduct vendorProduct = new VendorProduct() {{
//            setVendorProductId(1L);
//            setProduct(product);
//        }};
//
//        List<VendorProduct> products = Arrays.asList(
//                vendorProduct,
//                vendorProduct,
//                vendorProduct
//        );
//        when(vendorRepository.findById(10))
//                .thenReturn(Optional.of(new Vendor()));
//        when(vendorProductRepository.findAllByVendor(any(Vendor.class), any(PageRequest.class)))
//                .thenReturn(new PageImpl<>(products, pageRequest, products.size()));

        VendorProductPageDto vendorProductPageDto = vendorService.getProducts("10", pageRequest);
        assertEquals(Integer.valueOf(1), vendorProductPageDto.getNextPageToken());
    }

    @Test
    public void getProducts_pageSizeBiggerThanProductCount_nextPageTokenIsNull() {
        PageRequest pageRequest = PageRequest.of(0, 5);
//        Product product = new Product() {{
//            setProductId(1);
//            setName("Tofu");
//            setProducer("Sun wheat");
//            setImageUrl("https://www.test.com/image.png");
//        }};
//        VendorProduct vendorProduct = new VendorProduct() {{
//            setVendorProductId(1L);
//            setProduct(product);
//        }};
//
//        List<VendorProduct> products = Arrays.asList(
//                vendorProduct,
//                vendorProduct,
//                vendorProduct
//        );
//        when(vendorRepository.findById(10))
//                .thenReturn(Optional.of(new Vendor()));
//        when(vendorProductRepository.findAllByVendor(any(Vendor.class), any(PageRequest.class)))
//                .thenReturn(new PageImpl<>(products, pageRequest, products.size()));

        VendorProductPageDto vendorProductPageDto = vendorService.getProducts("10", pageRequest);
        assertNull(vendorProductPageDto.getNextPageToken());
    }

}