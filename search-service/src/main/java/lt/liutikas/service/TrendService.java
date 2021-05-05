package lt.liutikas.service;

import lt.liutikas.assembler.SearchRequestAssembler;
import lt.liutikas.dto.GetProductsTrendRequest;
import lt.liutikas.dto.ProductsBySearchCount;
import lt.liutikas.dto.TrendDto;
import lt.liutikas.dto.TrendPageDto;
import lt.liutikas.repository.SearchRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrendService {

    private final SearchRequestRepository searchRequestRepository;
    private final SearchRequestAssembler searchRequestAssembler;

    public TrendService(SearchRequestRepository searchRequestRepository, SearchRequestAssembler searchRequestAssembler) {
        this.searchRequestRepository = searchRequestRepository;
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

        return trendPageDto;
    }

}
