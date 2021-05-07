package lt.liutikas.dto;

import lt.liutikas.model.ReviewType;

import java.time.LocalDateTime;
import java.util.Map;

public class ReviewTrend {

    private LocalDateTime dateTime;
    private Map<ReviewType, Integer> counts;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Map<ReviewType, Integer> getCounts() {
        return counts;
    }

    public void setCounts(Map<ReviewType, Integer> counts) {
        this.counts = counts;
    }
}
