package lt.liutikas.controller;

import lt.liutikas.dto.*;
import lt.liutikas.service.TrendService;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trend")
@Validated
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

    @GetMapping("/product/{productId}/search")
    public List<SearchRequestsTrend> getProductSearchRequests(
            @RequestParam LocalDate fromDate,
            @RequestParam(defaultValue = "today") LocalDate toDate,
            @RequestParam @Min(0) @Max(50) Integer stepCount,
            @PathVariable Integer productId) {
        return trendService.getProductSearchTrends(fromDate, toDate, stepCount, productId);
    }

    @GetMapping("/vendor/{vendorId}/product/{productId}/price")
    public List<PriceTrend> getProductPrice(
            @PathVariable String productId,
            @PathVariable String vendorId) {
        return trendService.getProductPriceTrends(productId, vendorId);
    }

    @GetMapping("/vendor/{vendorId}/product/{productId}/review")
    public List<ReviewTrend> getReviewTrends(
            @PathVariable String productId,
            @PathVariable String vendorId,
            @RequestParam LocalDate fromDate,
            @RequestParam(defaultValue = "today") LocalDate toDate,
            @RequestParam @Min(0) @Max(50) Integer stepCount) {
        return trendService.getReviewTrends(productId, vendorId, fromDate, toDate, stepCount);
    }
}
