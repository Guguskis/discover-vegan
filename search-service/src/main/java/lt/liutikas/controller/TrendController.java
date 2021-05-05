package lt.liutikas.controller;

import lt.liutikas.dto.GetProductsTrendRequest;
import lt.liutikas.dto.TrendPageDto;
import lt.liutikas.service.TrendService;
import org.springframework.data.domain.Sort.Direction;
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
            @RequestParam(defaultValue = "DESC") Direction sortDirection,
            @RequestParam LocalDate fromDate,
            @RequestParam(defaultValue = "today") LocalDate toDate
    ) {
        GetProductsTrendRequest request = new GetProductsTrendRequest();
        request.setPageSize(pageSize);
        request.setPageToken(pageToken);
        request.setSortDirection(sortDirection);
        request.setFromDate(fromDate);
        request.setToDate(toDate);

        return trendService.getProductTrends(request);
    }

}
