package lt.liutikas.dto;

import java.util.List;

public class TrendPageDto extends PaginationResponse {

    public List<TrendDto> trends;

    public List<TrendDto> getTrends() {
        return trends;
    }

    public void setTrends(List<TrendDto> trends) {
        this.trends = trends;
    }

}
