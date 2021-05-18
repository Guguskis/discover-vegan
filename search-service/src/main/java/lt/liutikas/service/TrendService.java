package lt.liutikas.service;

import lt.liutikas.assembler.SearchRequestAssembler;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.GetProductsTrendRequest;
import lt.liutikas.dto.PriceTrend;
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
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.ReviewRepository;
import lt.liutikas.repository.SearchRequestRepository;
import lt.liutikas.repository.VendorProductRepository;
import lt.liutikas.repository.VendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TrendService {

    private static final Logger LOG = LoggerFactory.getLogger(TrendService.class);

    private final SearchRequestAssembler searchRequestAssembler;
    private final VendorProductRepository vendorProductRepository;
    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final ReviewRepository reviewRepository;
    private final SearchRequestRepository searchRequestRepository;

    public TrendService(SearchRequestAssembler searchRequestAssembler, VendorProductRepository vendorProductRepository, ProductRepository productRepository, VendorRepository vendorRepository, ReviewRepository reviewRepository, SearchRequestRepository searchRequestRepository) {
        this.searchRequestAssembler = searchRequestAssembler;
        this.vendorProductRepository = vendorProductRepository;
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
        this.reviewRepository = reviewRepository;
        this.searchRequestRepository = searchRequestRepository;
    }

    public TrendPageDto getProductTrends(GetProductsTrendRequest request) {
        Integer pageToken = request.getPageToken();
        Integer pageSize = request.getPageSize();

        Sort sort = Sort.by(request.getSortDirection(), "count");
        Pageable pageable = PageRequest.of(pageToken, pageSize, sort);

        LocalDateTime startDateTime = request.getFromDate().atStartOfDay();
        LocalDateTime endDateTime = request.getToDate().plusDays(1).atStartOfDay();

        List<SearchRequestAggregate> searchRequestAggregates = searchRequestRepository.groupByProductAndCreatedAtBetween(
                startDateTime,
                endDateTime,
                pageable
        );

        List<TrendDto> trendDtos = searchRequestAggregates
                .stream()
                .map(searchRequestAssembler::assemble)
                .collect(Collectors.toList());

        TrendPageDto trendPageDto = new TrendPageDto();
        trendPageDto.setTrends(trendDtos);

        if (searchRequestAggregates.size() == pageSize) {
            trendPageDto.setNextPageToken(pageToken + 1);
        }

        LOG.info(String.format("Returned product trends { pageToken: %d, pageSize: %d, fromDate: %s, toDate: %s, sortDirection: %s }",
                pageToken, pageSize, request.getFromDate(), request.getToDate(), request.getSortDirection().toString())
        );

        return trendPageDto;
    }

    public List<SearchRequestsTrend> getProductSearchTrends(LocalDate fromDate, LocalDate toDate, Integer stepCount, String productId) {

        if (toDate.isBefore(fromDate)) {
            throw new BadRequestException("toDate must be after fromDate");
        }

        Product product = assertProductFound(productId);

        double stepSizeInSeconds = getStepSizeInSeconds(fromDate, toDate, stepCount);

        List<SearchRequestsTrend> searchRequestsTrends = new ArrayList<>(stepCount);

        for (int i = 0; i < stepCount; i++) {
            double offsetStartInSeconds = stepSizeInSeconds * i;

            LocalDateTime localDateTimeStart = fromDate.atStartOfDay().plusSeconds((long) offsetStartInSeconds);
            LocalDateTime localDateTimeEnd = localDateTimeStart.plusSeconds((long) stepSizeInSeconds);

            List<SearchRequest> searchRequests = searchRequestRepository.findAllByProductAndCreatedAtBetween(product, localDateTimeStart, localDateTimeEnd);

            SearchRequestsTrend searchRequestsTrend = new SearchRequestsTrend();
            searchRequestsTrend.setDateTime(localDateTimeEnd);
            searchRequestsTrend.setCount(searchRequests.size());

            searchRequestsTrends.add(searchRequestsTrend);
        }

        LOG.info(String.format("Returned search requests trend { productId: %s, fromDate: %s, toDate: %s, stepCount: %d }",
                productId, fromDate, toDate, stepCount));

        return searchRequestsTrends;
    }

    private double getStepSizeInSeconds(LocalDate fromDate, LocalDate toDate, Integer stepCount) {
        int differenceDays = (int) ChronoUnit.DAYS.between(fromDate, toDate);
        double differenceInSeconds = differenceDays * 24 * 60 * 60;
        return differenceInSeconds / stepCount;
    }

    public List<PriceTrend> getProductPriceTrends(String productId, String vendorId) {

        Product product = assertProductFound(productId);
        Vendor vendor = assertVendorFound(vendorId);

        VendorProduct vendorProduct = vendorProductRepository.findByProductAndVendor(product, vendor).get();

        List<PriceTrend> priceTrends = vendorProduct.getVendorProductChanges()
                .stream()
                .sorted(Comparator.comparing(VendorProductChange::getCreatedAt))
                .map(vendorProductChange -> {
                    PriceTrend priceTrend = new PriceTrend();
                    priceTrend.setDateTime(vendorProductChange.getCreatedAt());
                    priceTrend.setPrice(vendorProductChange.getPrice());
                    return priceTrend;
                })
                .collect(Collectors.toList());

        LOG.info(String.format("Returned price trend { vendorProductId: %s }", vendorProduct.getId()));

        return priceTrends;
    }

    public List<ReviewTrend> getReviewTrends(String productId, String vendorId, LocalDate fromDate, LocalDate toDate, Integer stepCount) {

        Vendor vendor = assertVendorFound(vendorId);
        Product product = assertProductFound(productId);

        VendorProduct vendorProduct = vendorProductRepository.findByProductAndVendor(product, vendor).get();

        ArrayList<ReviewTrend> reviewTrends = new ArrayList<>();

        double stepSizeInSeconds = getStepSizeInSeconds(fromDate, toDate.plusDays(1), stepCount);

        for (int i = 0; i < stepCount; i++) {
            double offsetStartInSeconds = stepSizeInSeconds * i;

            LocalDateTime localDateTimeStart = fromDate.atStartOfDay().plusSeconds((long) offsetStartInSeconds);
            LocalDateTime localDateTimeEnd = localDateTimeStart.plusSeconds((long) stepSizeInSeconds);

            List<Review> reviews = reviewRepository.findAllByVendorProductAndCreatedAtBetween(vendorProduct, localDateTimeStart, localDateTimeEnd);

            Map<ReviewType, Integer> reviewTrendCounts = getReviewTrendCounts(reviews);

            ReviewTrend reviewTrend = new ReviewTrend();
            reviewTrend.setCounts(reviewTrendCounts);
            reviewTrend.setDateTime(localDateTimeEnd);
            reviewTrends.add(reviewTrend);
        }

        LOG.info(String.format("Returned review trends { vendorProductId: %s, fromDate: %s, toDate: %s, stepCount: %d }",
                vendorProduct.getId(), fromDate, toDate, stepCount));

        return reviewTrends;
    }

    private Map<ReviewType, Integer> getReviewTrendCounts(List<Review> reviews) {
        Map<ReviewType, Integer> reviewTrendCounts = new HashMap<>();

        for (ReviewType reviewType : ReviewType.values()) {

            List<Review> reviewsForType = reviews.stream()
                    .filter(review -> review.getReviewType().equals(reviewType))
                    .collect(Collectors.toList());

            reviewTrendCounts.put(reviewType, reviewsForType.size());
        }

        return reviewTrendCounts;
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

    private Vendor assertVendorFound(String vendorId) {
        Optional<Vendor> vendor = vendorRepository.findById(vendorId);

        if (vendor.isEmpty()) {
            String message = String.format("Vendor not found {vendorId: %s}", vendorId);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        return vendor.get();
    }
}
