package lt.liutikas.dto;

import lt.liutikas.model.Vendor;
import org.springframework.data.annotation.Id;

public class VendorProductCountAggregate {

    @Id
    private Vendor vendor;
    private int count;

    public VendorProductCountAggregate(Vendor vendor, int count) {
        this.vendor = vendor;
        this.count = count;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
