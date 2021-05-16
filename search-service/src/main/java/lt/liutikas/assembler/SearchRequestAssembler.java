package lt.liutikas.assembler;

import lt.liutikas.dto.SearchRequestAggregate;
import lt.liutikas.dto.TrendDto;
import org.springframework.stereotype.Component;

@Component
public class SearchRequestAssembler {

    private final ProductAssembler productAssembler;

    public SearchRequestAssembler(ProductAssembler productAssembler) {
        this.productAssembler = productAssembler;
    }

    public TrendDto assemble(SearchRequestAggregate searchRequestAggregate) {
        TrendDto trendDto = new TrendDto();

        trendDto.setProduct(productAssembler.assembleProduct(searchRequestAggregate.getProduct()));
        trendDto.setSearchCount(searchRequestAggregate.getCount());

        return trendDto;
    }
}
