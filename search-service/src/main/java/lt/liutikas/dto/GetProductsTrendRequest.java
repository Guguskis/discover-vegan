package lt.liutikas.dto;

import java.time.LocalDate;

public class GetProductsTrendRequest {

    private Integer pageToken;
    private Integer pageSize;
    private TrendOrderBy orderBy;
    private OrderByDirection orderByDirection;
    private LocalDate fromDate;
    private LocalDate toDate;

    public Integer getPageToken() {
        return pageToken;
    }

    public void setPageToken(Integer pageToken) {
        this.pageToken = pageToken;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public TrendOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(TrendOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public OrderByDirection getOrderByDirection() {
        return orderByDirection;
    }

    public void setOrderByDirection(OrderByDirection orderByDirection) {
        this.orderByDirection = orderByDirection;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}
