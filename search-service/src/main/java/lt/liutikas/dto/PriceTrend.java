package lt.liutikas.dto;

import java.time.LocalDateTime;

public class PriceTrend {

    private LocalDateTime dateTime;
    private Float price;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
