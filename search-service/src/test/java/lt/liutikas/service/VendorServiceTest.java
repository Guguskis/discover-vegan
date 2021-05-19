package lt.liutikas.service;

import lt.liutikas.assembler.VendorAssembler;
import lt.liutikas.assembler.VendorProductAssembler;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.CreateVendorProductDto;
import lt.liutikas.dto.Geometry;
import lt.liutikas.dto.GetVendorDto;
import lt.liutikas.dto.Location;
import lt.liutikas.dto.PatchVendorProductDto;
import lt.liutikas.dto.Place;
import lt.liutikas.dto.VendorDto;
import lt.liutikas.dto.VendorProductCountAggregate;
import lt.liutikas.dto.VendorProductDto;
import lt.liutikas.dto.VendorProductPageDto;
import lt.liutikas.model.Product;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import lt.liutikas.model.VendorProductChange;
import lt.liutikas.model.VendorType;
import lt.liutikas.repository.PlaceRepository;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.VendorProductRepository;
import lt.liutikas.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    public void getVendors_noNewVendors_returnsVendor() {

        GetVendorDto getVendorDto = new GetVendorDto();
        getVendorDto.setLocation(new Location() {{
            setLng(4.);
            setLat(20.);
        }});

        Place place = createPlace("asd");
        Vendor vendor = createVendor("1", "extId");

        when(placeRepository.getFoodPlaces(any(Location.class), any(VendorType.class)))
                .thenReturn(Arrays.asList(place));
        when(vendorRepository.findByExternalPlaceIdIn(any(List.class)))
                .thenReturn(Arrays.asList(vendor));
        when(vendorProductRepository.groupBy(any(List.class)))
                .thenReturn(Arrays.asList(new VendorProductCountAggregate(vendor, 5)));

        List<VendorDto> vendors = vendorService.getVendors(getVendorDto);
        VendorDto returnedVendor = vendors.get(0);

        assertEquals(vendor.getId(), returnedVendor.getVendorId());
        assertEquals(vendor.getVendorType(), returnedVendor.getVendorType());
        assertEquals(vendor.getName(), returnedVendor.getName());
        assertEquals(vendor.getAddress(), returnedVendor.getAddress());
        assertEquals(vendor.getLatitude(), returnedVendor.getLatitude());
        assertEquals(vendor.getLongitude(), returnedVendor.getLongitude());
        assertEquals((Integer) 5, returnedVendor.getProductCount());
    }

    @Test
    public void getVendors_noNewVendors_returnsVendors() {

        GetVendorDto getVendorDto = new GetVendorDto();
        getVendorDto.setLocation(new Location() {{
            setLng(4.);
            setLat(20.);
        }});

        List<Place> places = Arrays.asList(
                createPlace("ext1"),
                createPlace("ext2"),
                createPlace("ext3")
        );

        List<Vendor> vendors = Arrays.asList(
                createVendor("1", "ext1"),
                createVendor("2", "ext2"),
                createVendor("3", "ext3")
        );

        when(placeRepository.getFoodPlaces(any(Location.class), any(VendorType.class)))
                .thenReturn(places);
        when(vendorRepository.findByExternalPlaceIdIn(any(List.class)))
                .thenReturn(vendors);
        when(vendorProductRepository.groupBy(any(List.class)))
                .thenReturn(Arrays.asList(new VendorProductCountAggregate(createVendor("1", "ext1"), 5)));

        List<VendorDto> returnedVendors = vendorService.getVendors(getVendorDto);
        assertEquals(3, returnedVendors.size());
    }

    @Test
    public void getVendors_newVendor_createsVendor() {

        GetVendorDto getVendorDto = new GetVendorDto();
        getVendorDto.setLocation(new Location() {{
            setLng(4.);
            setLat(20.);
        }});

        List<Place> places = Arrays.asList(
                createPlace("ext1"),
                createPlace("ext2")
        );

        List<Vendor> vendors = Arrays.asList(
                createVendor("1", "ext1")
        );

        when(placeRepository.getFoodPlaces(any(Location.class), any(VendorType.class)))
                .thenReturn(places);
        when(vendorRepository.findByExternalPlaceIdIn(any(List.class)))
                .thenReturn(vendors);
        when(vendorProductRepository.groupBy(any(List.class)))
                .thenReturn(Arrays.asList(new VendorProductCountAggregate(createVendor("1", "ext1"), 5)));

        vendorService.getVendors(getVendorDto);

        verify(vendorRepository, times(1)).saveAll(any());
    }

    @Test
    public void getProducts_singleProduct_returnsSingleProduct() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Product product = createProduct("1");
        VendorProduct vendorProduct = new VendorProduct() {{
            setId("1");
            setProduct(product);
        }};
        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(12f, 1))
        );

        List<VendorProduct> vendorProducts = Collections.singletonList(vendorProduct);

        when(vendorRepository.findById("10"))
                .thenReturn(Optional.of(new Vendor()));
        when(vendorProductRepository.findAllByVendor(any(Vendor.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(vendorProducts, pageRequest, vendorProducts.size()));

        VendorProductPageDto vendorProductPageDto = vendorService.getProducts("10", pageRequest);
        VendorProductDto returnedVendorProductDto = vendorProductPageDto.getProducts().get(0);

        assertEquals(product.getId(), returnedVendorProductDto.getProductId());
        assertEquals(product.getName(), returnedVendorProductDto.getName());
        assertEquals(product.getImageUrl(), returnedVendorProductDto.getImageUrl());
        assertEquals(product.getProducer(), returnedVendorProductDto.getProducer());
    }

    @Test
    public void getProducts_multipleProducts_returnsMultipleProducts() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Product product = createProduct("1");
        VendorProduct vendorProduct = new VendorProduct() {{
            setId("1");
            setProduct(product);
        }};
        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(12f, 1))
        );

        List<VendorProduct> vendorProducts = Arrays.asList(
                vendorProduct,
                vendorProduct,
                vendorProduct
        );

        when(vendorRepository.findById("10"))
                .thenReturn(Optional.of(new Vendor()));
        when(vendorProductRepository.findAllByVendor(any(Vendor.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(vendorProducts, pageRequest, vendorProducts.size()));

        VendorProductPageDto vendorProductPageDto = vendorService.getProducts("10", pageRequest);
        List<VendorProductDto> products = vendorProductPageDto.getProducts();

        assertEquals(3, products.size());
    }

    @Test
    public void getProducts_hasNextPage_returnsNextPageToken() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Product product = createProduct("1");
        VendorProduct vendorProduct = new VendorProduct() {{
            setId("1");
            setProduct(product);
        }};

        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(12f, 1))
        );

        List<VendorProduct> products = Arrays.asList(
                vendorProduct,
                vendorProduct,
                vendorProduct
        );
        when(vendorRepository.findById("10"))
                .thenReturn(Optional.of(new Vendor()));
        when(vendorProductRepository.findAllByVendor(any(Vendor.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(products, pageRequest, products.size()));

        VendorProductPageDto vendorProductPageDto = vendorService.getProducts("10", pageRequest);
        assertEquals((Integer) 1, vendorProductPageDto.getNextPageToken());
    }

    @Test
    public void getProducts_doesntHaveNextPage_nextPageTokenIsNull() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Product product = createProduct("1");
        VendorProduct vendorProduct = new VendorProduct() {{
            setId("1");
            setProduct(product);
        }};

        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(12f, 1))
        );

        List<VendorProduct> products = Arrays.asList(
                vendorProduct,
                vendorProduct,
                vendorProduct
        );
        when(vendorRepository.findById("10"))
                .thenReturn(Optional.of(new Vendor()));
        when(vendorProductRepository.findAllByVendor(any(Vendor.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(products, pageRequest, products.size()));

        VendorProductPageDto vendorProductPageDto = vendorService.getProducts("10", pageRequest);
        assertNull(vendorProductPageDto.getNextPageToken());
    }

    @Test
    public void createVendorProduct_productExists_createsProduct() {
        CreateVendorProductDto createVendorProductDto = new CreateVendorProductDto();
        createVendorProductDto.setProductId("10");
        createVendorProductDto.setPrice(2f);

        Vendor vendor = new Vendor() {{
            setId("1");
        }};
        Product product = createProduct("1");
        VendorProduct vendorProduct = new VendorProduct() {{
            setId("1");
            setProduct(product);
        }};
        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(2f, 1))
        );

        when(vendorRepository.findById("1"))
                .thenReturn(Optional.of(vendor));
        when(productRepository.findById("10"))
                .thenReturn(Optional.of(product));
        when(vendorProductRepository.save(any(VendorProduct.class)))
                .thenReturn(vendorProduct);

        VendorProductDto vendorProductDto = vendorService.createProduct("1", "1", createVendorProductDto);

        assertEquals(product.getId(), vendorProductDto.getProductId());
        assertEquals(product.getName(), vendorProductDto.getName());
        assertEquals(product.getImageUrl(), vendorProductDto.getImageUrl());
        assertEquals(product.getProducer(), vendorProductDto.getProducer());
        assertEquals(createVendorProductDto.getPrice(), vendorProductDto.getPrice());
    }

    @Test(expected = NotFoundException.class)
    public void createVendorProduct_vendorDoesntExist_throwsNotFound() {
        CreateVendorProductDto createVendorProductDto = new CreateVendorProductDto();
        createVendorProductDto.setProductId("10");
        createVendorProductDto.setPrice(2f);

        when(vendorRepository.findById("1"))
                .thenReturn(Optional.empty());

        vendorService.createProduct("1", "1", createVendorProductDto);
    }

    @Test(expected = NotFoundException.class)
    public void createVendorProduct_productDoesntExist_throwsNotFound() {
        CreateVendorProductDto createVendorProductDto = new CreateVendorProductDto();
        createVendorProductDto.setProductId("10");
        createVendorProductDto.setPrice(2f);

        when(vendorRepository.findById("1"))
                .thenReturn(Optional.of(new Vendor()));
        when(productRepository.findById("10"))
                .thenReturn(Optional.empty());

        vendorService.createProduct("1", "1", createVendorProductDto);
    }

    @Test
    public void patchProduct_productExists_pricePatched() {
        PatchVendorProductDto patchVendorProductDto = new PatchVendorProductDto() {{
            setPrice(12f);
        }};

        Product product = createProduct("1");

        ArrayList<VendorProductChange> vendorProductChanges = new ArrayList<>();
        vendorProductChanges.add(createVendorProductChange(11f, 1));

        ArrayList<VendorProductChange> savedVendorProductChanges = new ArrayList<>();
        savedVendorProductChanges.add(createVendorProductChange(11f, 1));
        savedVendorProductChanges.add(createVendorProductChange(12f, 2));

        VendorProduct vendorProduct = new VendorProduct() {{
            setId("1");
            setProduct(product);
            setVendorProductChanges(vendorProductChanges);
        }};
        VendorProduct savedVendorProduct = new VendorProduct() {{
            setId("1");
            setProduct(product);
            setVendorProductChanges(savedVendorProductChanges);
        }};

        when(vendorRepository.findById("1"))
                .thenReturn(Optional.of(new Vendor()));
        when(productRepository.findById("1"))
                .thenReturn(Optional.of(new Product()));
        when(vendorProductRepository.findByProductAndVendor(any(), any()))
                .thenReturn(Optional.of(vendorProduct));
        when(vendorProductRepository.save(any()))
                .thenReturn(savedVendorProduct);

        VendorProductDto vendorProductDto = vendorService.patchProduct("1", "1", "1", patchVendorProductDto);

        assertEquals(savedVendorProduct.getId(), vendorProductDto.getProductId());
        assertEquals(product.getProducer(), vendorProductDto.getProducer());
        assertEquals((Float) 12f, vendorProductDto.getPrice());
        assertEquals(product.getName(), vendorProductDto.getName());
        assertEquals(product.getImageUrl(), vendorProductDto.getImageUrl());
    }

    @Test(expected = NotFoundException.class)
    public void patchProduct_vendorDoesntExist_throwsNotFound() {
        when(vendorRepository.findById(any()))
                .thenReturn(Optional.empty());

        vendorService.patchProduct("1", "1", "1", new PatchVendorProductDto());
    }

    @Test(expected = NotFoundException.class)
    public void patchProduct_productDoesntExist_throwsNotFound() {
        when(vendorRepository.findById(any()))
                .thenReturn(Optional.of(new Vendor()));
        when(productRepository.findById(any()))
                .thenReturn(Optional.empty());

        vendorService.patchProduct("1", "1", "1", new PatchVendorProductDto());
    }

    private Product createProduct(final String id) {
        return new Product() {{
            setId(id);
            setName("Tofu");
            setProducer("Sun wheat");
            setImageUrl("https://www.test.com/image.png");
        }};
    }

    private Vendor createVendor(final String id, final String externalPlaceId) {
        return new Vendor() {{
            setId(id);
            setExternalPlaceId(externalPlaceId);
            setLatitude(1.);
            setLongitude(1.);
            setVendorType(VendorType.STORE);
            setAddress("test");
            setName("Iki");
        }};
    }

    private Place createPlace(final String placeId) {
        Location location = new Location() {{
            setLat(1.);
            setLng(2.);
        }};
        Geometry geometry = new Geometry() {{
            setLocation(location);
        }};
        Place place = new Place() {{
            setPlace_id(placeId);
            setName("test");
            setVendorType(VendorType.STORE);
            setGeometry(geometry);
        }};
        return place;
    }

    private VendorProductChange createVendorProductChange(float price, int day) {
        return new VendorProductChange() {{
            setPrice(price);
            setCreatedAt(LocalDateTime.of(2020, 1, day, 1, 1));
        }};
    }
}