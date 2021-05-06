package lt.liutikas.service;

import lt.liutikas.assembler.SearchRequestAssembler;
import lt.liutikas.configuration.exception.BadRequestException;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.*;
import lt.liutikas.model.Product;
import lt.liutikas.model.SearchRequest;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.SearchRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TrendService {

    private static final Logger LOG = LoggerFactory.getLogger(TrendService.class);

    private final SearchRequestRepository searchRequestRepository;
    private final ProductRepository productRepository;
    private final SearchRequestAssembler searchRequestAssembler;

    public TrendService(SearchRequestRepository searchRequestRepository, ProductRepository productRepository, SearchRequestAssembler searchRequestAssembler) {
        this.searchRequestRepository = searchRequestRepository;
        this.productRepository = productRepository;
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

    public List<SearchRequestsTrend> getProductTrends(LocalDate fromDate, LocalDate toDate, Integer stepCount, Integer productId) {

        if (toDate.isBefore(fromDate)) {
            throw new BadRequestException("toDate must be after fromDate");
        }

        Product product = assertProductFound(productId);

        Period difference = Period.between(fromDate, toDate.plusDays(1));
        int differenceDays = difference.getDays();
        double differenceInSeconds = differenceDays * 24 * 60 * 60;
        double stepSizeInSeconds = differenceInSeconds / stepCount;

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

        LOG.info(String.format("Returned search requests { productId: %d, fromDate: %s, toDate: %s, stepCount: %d }",
                productId, fromDate, toDate, stepCount));

        return searchRequestsTrends;
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
