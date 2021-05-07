package lt.liutikas.service;

import lt.liutikas.assembler.ReviewAssembler;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.CreateReviewDto;
import lt.liutikas.dto.ReviewDto;
import lt.liutikas.model.*;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.ReviewRepository;
import lt.liutikas.repository.VendorProductRepository;
import lt.liutikas.repository.VendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    private final VendorProductRepository vendorProductRepository;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewAssembler reviewAssembler;

    public ReviewService(VendorProductRepository vendorProductRepository, VendorRepository vendorRepository, ProductRepository productRepository, ReviewRepository reviewRepository, ReviewAssembler reviewAssembler) {
        this.vendorProductRepository = vendorProductRepository;
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.reviewAssembler = reviewAssembler;
    }

    public List<ReviewType> getTypes() {
        return Arrays.asList(ReviewType.values().clone());
    }

    public ReviewDto createReview(CreateReviewDto createReviewDto, Integer userId) {

        Vendor vendor = assertVendorFound(createReviewDto.getVendorId());
        Product product = assertProductFound(createReviewDto.getProductId());

        VendorProduct vendorProduct = vendorProductRepository.findAllByProductAndVendor(product, vendor);

        Review review = new Review();
        review.setReviewType(createReviewDto.getReviewType());
        review.setUserId(userId);
        review.setVendorProduct(vendorProduct);

        review = reviewRepository.save(review);

        return reviewAssembler.assembleReviewDto(review);
    }

    private Vendor assertVendorFound(Integer vendorId) {
        Optional<Vendor> vendor = vendorRepository.findById(vendorId);

        if (vendor.isEmpty()) {
            String message = String.format("Vendor not found {vendorId: %d}", vendorId);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        return vendor.get();
    }

    private Product assertProductFound(Integer productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            String message = String.format("Product not found {productId: %d}", productId);
            LOG.error(message);
            throw new NotFoundException(message);
        }
        return product.get();
    }
}
