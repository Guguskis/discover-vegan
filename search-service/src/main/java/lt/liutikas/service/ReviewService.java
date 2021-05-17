package lt.liutikas.service;

import lt.liutikas.assembler.ReviewAssembler;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.CreateReviewDto;
import lt.liutikas.dto.ReviewDto;
import lt.liutikas.model.Product;
import lt.liutikas.model.Review;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.ReviewRepository;
import lt.liutikas.repository.VendorProductRepository;
import lt.liutikas.repository.VendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final ReviewAssembler reviewAssembler;
    private final VendorProductRepository vendorProductRepository;
    private final ReviewRepository reviewRepository;
    private final Clock clock;

    public ReviewService(VendorRepository vendorRepository, ProductRepository productRepository, ReviewAssembler reviewAssembler, VendorProductRepository vendorProductRepository, ReviewRepository reviewRepository, Clock clock) {
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
        this.reviewAssembler = reviewAssembler;
        this.vendorProductRepository = vendorProductRepository;
        this.reviewRepository = reviewRepository;
        this.clock = clock;
    }

    public ReviewDto createReview(CreateReviewDto createReviewDto, String userId) {

        Vendor vendor = assertVendorFound(createReviewDto.getVendorId());
        Product product = assertProductFound(createReviewDto.getProductId());

        VendorProduct vendorProduct = vendorProductRepository.findByProductAndVendor(product, vendor).get();

        Review review = new Review();
        review.setReviewType(createReviewDto.getReviewType());
        review.setUserId(userId);
        review.setVendorProduct(vendorProduct);
        review.setCreatedAt(LocalDateTime.now(clock));

        review = reviewRepository.save(review);

        LOG.info(String.format("Created review {reviewType: %s, vendorProductId: %s }", createReviewDto.getReviewType(), vendorProduct.getId()));

        return reviewAssembler.assembleReviewDto(review);
    }

    private Vendor assertVendorFound(String vendorId) {
        Optional<Vendor> vendor = vendorRepository.findById(vendorId);

        if (vendor.isEmpty()) {
            String message = String.format("Vendor not found {vendorId: %s}", vendorId);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        return vendor.get();
    }

    private Product assertProductFound(String productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            String message = String.format("Product not found {productId: %s}", productId);
            LOG.error(message);
            throw new NotFoundException(message);
        }
        return product.get();
    }
}
