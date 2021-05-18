package lt.liutikas.service;

import lt.liutikas.assembler.ProductAssembler;
import lt.liutikas.assembler.SearchRequestAssembler;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.GetProductsTrendRequest;
import lt.liutikas.dto.PriceTrend;
import lt.liutikas.dto.ProductDto;
import lt.liutikas.dto.ReviewTrend;
import lt.liutikas.dto.SearchRequestAggregate;
import lt.liutikas.dto.SearchRequestsTrend;
import lt.liutikas.dto.TrendDto;
import lt.liutikas.dto.TrendPageDto;
import lt.liutikas.model.Product;
import lt.liutikas.model.Review;
import lt.liutikas.model.ReviewType;
import lt.liutikas.model.SearchRequest;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import lt.liutikas.model.VendorProductChange;
import lt.liutikas.model.VendorType;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.ReviewRepository;
import lt.liutikas.repository.SearchRequestRepository;
import lt.liutikas.repository.VendorProductRepository;
import lt.liutikas.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TrendServiceTest {

    @Mock
    private VendorProductRepository vendorProductRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private VendorRepository vendorRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private SearchRequestRepository searchRequestRepository;


    private TrendService trendService;

    @Before
    public void setUp() {

        trendService = new TrendService(
                new SearchRequestAssembler(new ProductAssembler()),
                vendorProductRepository,
                productRepository,
                vendorRepository,
                reviewRepository,
                searchRequestRepository
        );

    }

    @Test
    public void getProductTrends_singleTrend_returnsSingleTrend() {
        GetProductsTrendRequest request = new GetProductsTrendRequest() {{
            setPageSize(10);
            setPageToken(0);
            setSortDirection(Sort.Direction.DESC);
            setFromDate(LocalDate.of(2021, 6, 1));
            setToDate(LocalDate.of(2021, 6, 10));
        }};

        Product product = createProduct("1", "Tofu");
        SearchRequestAggregate searchRequestAggregate = new SearchRequestAggregate(product, 5);

        when(searchRequestRepository.groupByProductAndCreatedAtBetween(any(), any(), any()))
                .thenReturn(Arrays.asList(searchRequestAggregate));

        TrendPageDto productTrends = trendService.getProductTrends(request);

        TrendDto trendDto = productTrends.getTrends().get(0);
        ProductDto returnedProduct = trendDto.getProduct();

        assertEquals(product.getId(), returnedProduct.getProductId());
        assertEquals(product.getName(), returnedProduct.getName());
        assertEquals(product.getProducer(), returnedProduct.getProducer());
        assertEquals(product.getImageUrl(), returnedProduct.getImageUrl());
        assertEquals((Integer) 5, trendDto.getSearchCount());
    }

    @Test
    public void getProductTrends_multipleTrends_returnsMultipleTrends() {
        GetProductsTrendRequest request = new GetProductsTrendRequest() {{
            setPageSize(10);
            setPageToken(0);
            setSortDirection(Sort.Direction.DESC);
            setFromDate(LocalDate.of(2021, 6, 1));
            setToDate(LocalDate.of(2021, 6, 10));
        }};

        Product tofu = createProduct("1", "Tofu");
        Product cheese = createProduct("2", "Cheese");
        Product milk = createProduct("3", "Milk");

        List<SearchRequestAggregate> searchRequestAggregates = Arrays.asList(
                new SearchRequestAggregate(tofu, 5),
                new SearchRequestAggregate(cheese, 5),
                new SearchRequestAggregate(milk, 5)
        );

        when(searchRequestRepository.groupByProductAndCreatedAtBetween(any(), any(), any()))
                .thenReturn(searchRequestAggregates);

        TrendPageDto productTrends = trendService.getProductTrends(request);

        assertEquals(3, productTrends.getTrends().size());
    }

//    @Test(expected = NotFoundException.class)
//    public void getProductTrends_productDoesntExist_throwsNotFound() {
//        GetProductsTrendRequest request = new GetProductsTrendRequest() {{
//            setPageSize(10);
//            setPageToken(0);
//            setSortDirection(Sort.Direction.DESC);
//            setFromDate(LocalDate.of(2021, 6, 1));
//            setToDate(LocalDate.of(2021, 6, 10));
//        }};
//
//        when(productRepository.findById(any()))
//                .thenReturn(Optional.empty());
//
//        trendService.getProductTrends(request);
//    }


    @Test
    public void getProductSearchTrends_stepSizeOne_returnsSingleSearchTrend() {
        LocalDate fromDate = LocalDate.of(2021, 6, 1);
        LocalDate toDate = LocalDate.of(2021, 6, 2);

        Product product = createProduct("1", "Tofu");
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(product));
        when(searchRequestRepository.findAllByProductAndCreatedAtBetween(any(), any(), any()))
                .thenReturn(Arrays.asList(
                        new SearchRequest(),
                        new SearchRequest(),
                        new SearchRequest()
                ));

        List<SearchRequestsTrend> productSearchTrends = trendService.getProductSearchTrends(fromDate, toDate, 1, "1");
        SearchRequestsTrend searchRequestsTrend = productSearchTrends.get(0);

        assertEquals((Integer) 3, searchRequestsTrend.getCount());
        assertEquals(LocalDate.of(2021, 6, 2).atStartOfDay(), searchRequestsTrend.getDateTime());
    }

    @Test
    public void getProductSearchTrends_stepSizeThree_returnsMultipleSearchTrends() {
        LocalDate fromDate = LocalDate.of(2021, 6, 1);
        LocalDate toDate = LocalDate.of(2021, 6, 2);

        Product product = createProduct("1", "Tofu");
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(product));
        when(searchRequestRepository.findAllByProductAndCreatedAtBetween(any(), any(), any()))
                .thenReturn(Arrays.asList(
                        new SearchRequest(),
                        new SearchRequest(),
                        new SearchRequest()
                ));

        List<SearchRequestsTrend> productSearchTrends = trendService.getProductSearchTrends(fromDate, toDate, 3, "1");

        assertEquals(3, productSearchTrends.size());
    }

    @Test(expected = NotFoundException.class)
    public void getProductSearchTrends_productDoesntExist_throwsNotFound() {
        LocalDate fromDate = LocalDate.of(2021, 6, 1);
        LocalDate toDate = LocalDate.of(2021, 6, 2);

        when(productRepository.findById(any()))
                .thenReturn(Optional.empty());
        trendService.getProductSearchTrends(fromDate, toDate, 3, "1");
    }

    @Test
    public void getProductPriceTrends_singlePriceChange_returnsSinglePriceChange() {
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Product()));
        when(vendorRepository.findById(any()))
                .thenReturn(Optional.of(new Vendor()));
        VendorProduct vendorProduct = new VendorProduct();
        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(1f, 2020, 1, 1, 1, 1)
        ));
        when(vendorProductRepository.findByProductAndVendor(any(), any()))
                .thenReturn(Optional.of(vendorProduct));

        List<PriceTrend> productPriceTrends = trendService.getProductPriceTrends("1", "1");

        PriceTrend priceTrend = productPriceTrends.get(0);

        assertEquals(LocalDateTime.of(2020, 1, 1, 1, 1), priceTrend.getDateTime());
        assertEquals((Float) 1f, priceTrend.getPrice());
    }

    @Test
    public void getProductPriceTrends_unorderedPriceChanges_returnsOrderedPriceChanges() {
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Product()));
        when(vendorRepository.findById(any()))
                .thenReturn(Optional.of(new Vendor()));
        VendorProduct vendorProduct = new VendorProduct();
        vendorProduct.setVendorProductChanges(Arrays.asList(
                createVendorProductChange(2f, 2020, 1, 1, 1, 2),
                createVendorProductChange(3f, 2020, 1, 1, 1, 3),
                createVendorProductChange(1f, 2020, 1, 1, 1, 1)
        ));
        when(vendorProductRepository.findByProductAndVendor(any(), any()))
                .thenReturn(Optional.of(vendorProduct));

        List<PriceTrend> productPriceTrends = trendService.getProductPriceTrends("1", "1");

        assertEquals((Float) 1f, productPriceTrends.get(0).getPrice());
        assertEquals((Float) 2f, productPriceTrends.get(1).getPrice());
        assertEquals((Float) 3f, productPriceTrends.get(2).getPrice());
    }

    @Test(expected = NotFoundException.class)
    public void getProductPriceTrends_vendorDoesntExist_throwsNotFound() {
        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Product()));
        when(vendorRepository.findById(any()))
                .thenReturn(Optional.empty());

        trendService.getProductPriceTrends("1", "1");
    }

    @Test(expected = NotFoundException.class)
    public void getProductPriceTrends_productDoesntExist_throwsNotFound() {
        when(productRepository.findById(any()))
                .thenReturn(Optional.empty());

        trendService.getProductPriceTrends("1", "1");
    }

    @Test
    public void getReviewTrends_stepSizeOne_returnsSingleReviewTrend() {
        LocalDate fromDate = LocalDate.of(2021, 6, 1);
        LocalDate toDate = LocalDate.of(2021, 6, 2);

        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Product()));
        when(vendorRepository.findById(any()))
                .thenReturn(Optional.of(new Vendor()));
        when(vendorProductRepository.findByProductAndVendor(any(), any()))
                .thenReturn(Optional.of(new VendorProduct()));
        when(reviewRepository.findAllByVendorProductAndCreatedAtBetween(any(), any(), any()))
                .thenReturn(Arrays.asList(
                        createReview(ReviewType.NOT_RECOMMENDED),
                        createReview(ReviewType.NOT_VEGAN),
                        createReview(ReviewType.NOT_VEGAN),
                        createReview(ReviewType.CANT_FIND),
                        createReview(ReviewType.CANT_FIND),
                        createReview(ReviewType.CANT_FIND)
                ));

        List<ReviewTrend> reviewTrends = trendService.getReviewTrends("1", "1", fromDate, toDate, 1);
        ReviewTrend reviewTrend = reviewTrends.get(0);

        assertEquals(LocalDate.of(2021, 6, 3).atStartOfDay(), reviewTrend.getDateTime());
        assertEquals((Integer) 0, reviewTrend.getCounts().get(ReviewType.RECOMMENDED));
        assertEquals((Integer) 1, reviewTrend.getCounts().get(ReviewType.NOT_RECOMMENDED));
        assertEquals((Integer) 2, reviewTrend.getCounts().get(ReviewType.NOT_VEGAN));
        assertEquals((Integer) 3, reviewTrend.getCounts().get(ReviewType.CANT_FIND));
    }

    @Test
    public void getReviewTrends_stepSizeThree_returnsMultipleReviewTrends() {
        LocalDate fromDate = LocalDate.of(2021, 6, 1);
        LocalDate toDate = LocalDate.of(2021, 6, 2);

        when(productRepository.findById(any()))
                .thenReturn(Optional.of(new Product()));
        when(vendorRepository.findById(any()))
                .thenReturn(Optional.of(new Vendor()));
        when(vendorProductRepository.findByProductAndVendor(any(), any()))
                .thenReturn(Optional.of(new VendorProduct()));
        when(reviewRepository.findAllByVendorProductAndCreatedAtBetween(any(), any(), any()))
                .thenReturn(Arrays.asList(
                        createReview(ReviewType.NOT_RECOMMENDED)
                ));

        List<ReviewTrend> reviewTrends = trendService.getReviewTrends("1", "1", fromDate, toDate, 3);

        assertEquals(3, reviewTrends.size());
    }

    @Test(expected = NotFoundException.class)
    public void getReviewTrends_productDoesntExist_throwsNotFound() {
        LocalDate fromDate = LocalDate.of(2021, 6, 1);
        LocalDate toDate = LocalDate.of(2021, 6, 2);

        when(vendorRepository.findById(any()))
                .thenReturn(Optional.of(new Vendor()));
        when(productRepository.findById(any()))
                .thenReturn(Optional.empty());

        trendService.getReviewTrends("1", "1", fromDate, toDate, 3);
    }

    @Test(expected = NotFoundException.class)
    public void getReviewTrends_vendorDoesntExist_throwsNotFound() {
        LocalDate fromDate = LocalDate.of(2021, 6, 1);
        LocalDate toDate = LocalDate.of(2021, 6, 2);

        when(vendorRepository.findById(any()))
                .thenReturn(Optional.empty());

        trendService.getReviewTrends("1", "1", fromDate, toDate, 3);
    }

    private Review createReview(ReviewType reviewType) {
        Review review = new Review();
        review.setReviewType(reviewType);
        return review;
    }

    private Product createProduct(String id, String tofu) {
        return new Product() {{
            setId(id);
            setName(tofu);
            setProducer("Sun wheat");
            setImageUrl("https://www.test.com/image.png");
        }};
    }

    private Vendor createVendor(String id) {
        return new Vendor() {{
            setId(id);
            setExternalPlaceId("externalPlaceId");
            setLatitude(1.);
            setLongitude(1.);
            setVendorType(VendorType.STORE);
            setAddress("test");
            setImageUrl("url");
            setName("Iki");
        }};
    }

    private VendorProductChange createVendorProductChange(float price, int year, int month, int day, int hour, int minute) {
        return new VendorProductChange() {{
            setPrice(price);
            setCreatedAt(LocalDateTime.of(year, month, day, hour, minute));
        }};
    }

}