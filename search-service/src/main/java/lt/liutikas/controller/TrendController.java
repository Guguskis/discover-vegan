package lt.liutikas.controller;

import lt.liutikas.dto.GetProductsTrendRequest;
import lt.liutikas.dto.OrderByDirection;
import lt.liutikas.dto.TrendOrderBy;
import lt.liutikas.dto.TrendPageDto;
import lt.liutikas.service.TrendService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/trend")
public class TrendController {

    private final TrendService trendService;

    public TrendController(TrendService trendService) {
        this.trendService = trendService;
    }

    @GetMapping("/products")
    public TrendPageDto getProducts(
            @Min(0) @RequestParam(defaultValue = "0") Integer pageToken,
            @Min(1) @RequestParam(defaultValue = "50") Integer pageSize,
            @RequestParam(defaultValue = "SEARCH_COUNT") TrendOrderBy orderBy,
            @RequestParam(defaultValue = "DESCENDING") OrderByDirection orderByDirection,
            @RequestParam LocalDate fromDate,
            @RequestParam(defaultValue = "today") LocalDate toDate
    ) {
        GetProductsTrendRequest request = new GetProductsTrendRequest();
        request.setPageable(PageRequest.of(pageToken, pageSize));
        request.setOrderBy(orderBy);
        request.setOrderByDirection(orderByDirection);
        request.setFromDate(fromDate);
        request.setToDate(toDate);

        return trendService.getProductTrends(request);
    }

}
