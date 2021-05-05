package lt.liutikas.dto;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public class GetProductsTrendRequest {

    private Pageable pageable;
    private TrendOrderBy orderBy;
    private OrderByDirection orderByDirection;
    private LocalDate fromDate;
    private LocalDate toDate;

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
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
