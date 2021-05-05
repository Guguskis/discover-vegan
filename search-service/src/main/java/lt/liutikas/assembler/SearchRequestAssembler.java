package lt.liutikas.assembler;

import lt.liutikas.dto.ProductsBySearchCount;
import lt.liutikas.dto.TrendDto;
import org.springframework.stereotype.Component;

@Component
public class SearchRequestAssembler {

    private final ProductAssembler productAssembler;

    public SearchRequestAssembler(ProductAssembler productAssembler) {
        this.productAssembler = productAssembler;
    }

    public TrendDto assemble(ProductsBySearchCount productsBySearchCount) {
        TrendDto trendDto = new TrendDto();

        trendDto.setProduct(productAssembler.assembleProduct(productsBySearchCount.getProduct()));
        trendDto.setSearchCount(productsBySearchCount.getSearchCount());

        return trendDto;
    }
}
