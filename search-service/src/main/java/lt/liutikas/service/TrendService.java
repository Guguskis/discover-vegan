package lt.liutikas.service;

import lt.liutikas.assembler.SearchRequestAssembler;
import lt.liutikas.dto.GetProductsTrendRequest;
import lt.liutikas.dto.SearchRequestGroupedByProduct;
import lt.liutikas.dto.TrendPageDto;
import lt.liutikas.repository.SearchRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class TrendService {

    private final SearchRequestRepository searchRequestRepository;
    private final SearchRequestAssembler searchRequestAssembler;

    public TrendService(SearchRequestRepository searchRequestRepository, SearchRequestAssembler searchRequestAssembler) {
        this.searchRequestRepository = searchRequestRepository;
        this.searchRequestAssembler = searchRequestAssembler;
    }

    public TrendPageDto getProductTrends(GetProductsTrendRequest request) {

        Page<SearchRequestGroupedByProduct> searchRequestsPage = searchRequestRepository.findSearchRequestCount(request.getPageable());
//        List<SearchRequestGroupedByProduct> searchRequestsPage = searchRequestRepository.findSearchRequestCount();


        TrendPageDto trendPageDto = new TrendPageDto();

        return null;
    }

}
