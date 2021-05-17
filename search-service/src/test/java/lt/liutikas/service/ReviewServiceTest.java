package lt.liutikas.service;

import lt.liutikas.assembler.ReviewAssembler;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.CreateReviewDto;
import lt.liutikas.dto.ReviewDto;
import lt.liutikas.model.Product;
import lt.liutikas.model.ReviewType;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.ReviewRepository;
import lt.liutikas.repository.VendorProductRepository;
import lt.liutikas.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTest {

    @Mock
    private VendorRepository vendorRepository;
    @Mock
    private VendorProductRepository vendorProductRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ReviewRepository reviewRepository;

    private final Clock clock = Clock.fixed(Instant.parse("2021-01-01T12:10:11Z"), ZoneOffset.UTC);

    private ReviewService reviewService;

    @Before
    public void setUp() {
        reviewService = new ReviewService(
                vendorRepository,
                productRepository,
                new ReviewAssembler(),
                vendorProductRepository,
                reviewRepository,
                clock);
    }

    @Test
    public void createReview_existingProduct_createsReview() {
        CreateReviewDto createReviewDto = new CreateReviewDto();
        createReviewDto.setReviewType(ReviewType.RECOMMENDED);
        createReviewDto.setProductId("1");
        createReviewDto.setVendorId("1");

        when(vendorRepository.findById(anyString()))
                .thenReturn(Optional.of(new Vendor()));
        when(productRepository.findById(anyString()))
                .thenReturn(Optional.of(new Product()));
        when(vendorProductRepository.findByProductAndVendor(any(), any()))
                .thenReturn(Optional.of(new VendorProduct()));
        when(reviewRepository.save(any()))
                .thenAnswer(returnsFirstArg());

        ReviewDto review = reviewService.createReview(createReviewDto, "1");

        assertEquals(createReviewDto.getReviewType(), review.getReviewType());
        assertEquals(LocalDateTime.now(clock), review.getCreatedAt());

    }

    @Test(expected = NotFoundException.class)
    public void createReview_productDoesntExist_throwsNotFoundException() {
        CreateReviewDto createReviewDto = new CreateReviewDto();
        createReviewDto.setReviewType(ReviewType.RECOMMENDED);
        createReviewDto.setProductId("1");
        createReviewDto.setVendorId("1");

        when(vendorRepository.findById(anyString()))
                .thenReturn(Optional.of(new Vendor()));
        when(productRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        reviewService.createReview(createReviewDto, "1");
    }

    @Test(expected = NotFoundException.class)
    public void createReview_vendorDoesntExist_throwsNotFoundException() {
        CreateReviewDto createReviewDto = new CreateReviewDto();
        createReviewDto.setReviewType(ReviewType.RECOMMENDED);
        createReviewDto.setProductId("1");
        createReviewDto.setVendorId("1");

        when(vendorRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        reviewService.createReview(createReviewDto, "1");
    }
}