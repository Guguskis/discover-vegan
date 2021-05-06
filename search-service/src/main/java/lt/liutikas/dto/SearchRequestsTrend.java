package lt.liutikas.dto;

import java.time.LocalDateTime;

public class SearchRequestsTrend {

    private LocalDateTime dateTime;
    private Integer count;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
