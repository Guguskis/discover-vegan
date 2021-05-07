package lt.liutikas.service;

import lt.liutikas.assembler.SearchRequestAssembler;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.*;
import lt.liutikas.model.*;
import lt.liutikas.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TrendService {

    private static final Logger LOG = LoggerFactory.getLogger(TrendService.class);

    private final SearchRequestRepository searchRequestRepository;
    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final VendorProductRepository vendorProductRepository;
    private final ReviewRepository reviewRepository;

    private final SearchRequestAssembler searchRequestAssembler;

    public TrendService(SearchRequestRepository searchRequestRepository, ProductRepository productRepository, VendorRepository vendorRepository, VendorProductRepository vendorProductRepository, ReviewRepository reviewRepository, SearchRequestAssembler searchRequestAssembler) {
        this.searchRequestRepository = searchRequestRepository;
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
        this.vendorProductRepository = vendorProductRepository;
        this.reviewRepository = reviewRepository;
        this.searchRequestAssembler = searchRequestAssembler;
    }

    public TrendPageDto getProductTrends(GetProductsTrendRequest request) {

        Sort sort = Sort.by(request.getSortDirection(), "searchCount");
        Pageable pageable = PageRequest.of(request.getPageToken(), request.getPageSize(), sort);


        Page<ProductsBySearchCount> searchRequestsPage = searchRequestRepository.findSearchRequestCount(
                pageable,
                request.getFromDate().atStartOfDay(),
                request.getToDate().plusDays(1).atStartOfDay()
        );
        Pageable nextPageable = searchRequestsPage.nextPageable();

        List<TrendDto> trendDtos = searchRequestsPage.getContent()
                .stream()
                .map(searchRequestAssembler::assemble)
                .collect(Collectors.toList());

        TrendPageDto trendPageDto = new TrendPageDto();
        trendPageDto.setTrends(trendDtos);

        if (nextPageable.isPaged()) {
            trendPageDto.setNextPageToken(nextPageable.getPageNumber());
        }

        LOG.info(String.format("Returned product trends { pageToken: %d, pageSize: %d, fromDate: %s, toDate: %s, sortDirection: %s }",
                request.getPageToken(), request.getPageSize(), request.getFromDate(), request.getToDate(), request.getSortDirection().toString())
        );

        return trendPageDto;
    }

    public List<SearchRequestsTrend> getProductSearchTrends(LocalDate fromDate, LocalDate toDate, Integer stepCount, Integer productId) {

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

        LOG.info(String.format("Returned search requests trend { productId: %d, fromDate: %s, toDate: %s, stepCount: %d }",
                productId, fromDate, toDate, stepCount));

        return searchRequestsTrends;
    }

    private double getStepSizeInSeconds(LocalDate fromDate, LocalDate toDate, Integer stepCount) {
        int differenceDays = (int) ChronoUnit.DAYS.between(fromDate, toDate);
        double differenceInSeconds = differenceDays * 24 * 60 * 60;
        return differenceInSeconds / stepCount;
    }

    public List<PriceTrend> getProductPriceTrends(Integer productId, Integer vendorId) {

        Product product = assertProductFound(productId);
        Vendor vendor = assertVendorFound(vendorId);

        VendorProduct vendorProduct = vendorProductRepository.findAllByProductAndVendor(product, vendor);

        List<PriceTrend> priceTrends = vendorProduct.getVendorProductChanges()
                .stream()
                .map(vendorProductChange -> {
                    PriceTrend priceTrend = new PriceTrend();
                    priceTrend.setDateTime(vendorProductChange.getCreatedAt());
                    priceTrend.setPrice(vendorProductChange.getPrice());
                    return priceTrend;
                })
                .collect(Collectors.toList());

        if (priceTrends.size() > 1) {
            priceTrends.sort(Comparator.comparing(PriceTrend::getDateTime));
        }

        LOG.info(String.format("Returned price trend { productId: %d }", productId));

        return priceTrends;
    }

    public List<ReviewTrend> getReviewTrends(Integer productId, Integer vendorId, LocalDate fromDate, LocalDate toDate, Integer stepCount) {

        Vendor vendor = assertVendorFound(vendorId);
        Product product = assertProductFound(productId);

        VendorProduct vendorProduct = vendorProductRepository.findAllByProductAndVendor(product, vendor);

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

    private Product assertProductFound(Integer productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            String message = String.format("Product not found {productId: %d}", productId);
            LOG.error(message);
            throw new NotFoundException(message);
        }
        return product.get();
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
}
