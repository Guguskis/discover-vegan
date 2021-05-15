package lt.liutikas.service;

import lt.liutikas.assembler.ReviewAssembler;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.CreateReviewDto;
import lt.liutikas.dto.ReviewDto;
import lt.liutikas.model.MongoProduct;
import lt.liutikas.model.MongoReview;
import lt.liutikas.model.MongoVendor;
import lt.liutikas.model.MongoVendorProduct;
import lt.liutikas.repository.MongoProductRepository;
import lt.liutikas.repository.MongoReviewRepository;
import lt.liutikas.repository.MongoVendorProductRepository;
import lt.liutikas.repository.MongoVendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    private final MongoProductRepository mongoProductRepository;
    private final MongoVendorRepository mongoVendorRepository;
    private final ReviewAssembler reviewAssembler;
    private final MongoVendorProductRepository mongoVendorProductRepository;
    private final MongoReviewRepository mongoReviewRepository;

    public ReviewService(MongoVendorRepository mongoVendorRepository, MongoProductRepository mongoProductRepository, ReviewAssembler reviewAssembler, MongoVendorProductRepository mongoVendorProductRepository, MongoReviewRepository mongoReviewRepository) {
        this.mongoVendorRepository = mongoVendorRepository;
        this.mongoProductRepository = mongoProductRepository;
        this.reviewAssembler = reviewAssembler;
        this.mongoVendorProductRepository = mongoVendorProductRepository;
        this.mongoReviewRepository = mongoReviewRepository;
    }

    public ReviewDto createReview(CreateReviewDto createReviewDto, String userId) {

        MongoVendor vendor = assertVendorFound(createReviewDto.getVendorId());
        MongoProduct product = assertProductFound(createReviewDto.getProductId());

        MongoVendorProduct vendorProduct = mongoVendorProductRepository.findByProductAndVendor(product, vendor).get();

        MongoReview review = new MongoReview();
        review.setReviewType(createReviewDto.getReviewType());
        review.setUserId(userId);
        review.setVendorProduct(vendorProduct);
        review.setCreatedAt(LocalDateTime.now());

        review = mongoReviewRepository.save(review);

        LOG.info(String.format("Created review {reviewType: %s, vendorProductId: %s }", createReviewDto.getReviewType(), vendorProduct.getId()));

        return reviewAssembler.assembleReviewDto(review);
    }

    private MongoVendor assertVendorFound(String vendorId) {
        Optional<MongoVendor> vendor = mongoVendorRepository.findById(vendorId);

        if (vendor.isEmpty()) {
            String message = String.format("Vendor not found {vendorId: %d}", vendorId);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        return vendor.get();
    }

    private MongoProduct assertProductFound(String productId) {
        Optional<MongoProduct> product = mongoProductRepository.findById(productId);

        if (product.isEmpty()) {
            String message = String.format("Product not found {productId: %s}", productId);
            LOG.error(message);
            throw new NotFoundException(message);
        }
        return product.get();
    }
}
