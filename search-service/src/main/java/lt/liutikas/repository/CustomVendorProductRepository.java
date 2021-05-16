package lt.liutikas.repository;

import lt.liutikas.dto.VendorProductCountAggregate;
import lt.liutikas.model.Vendor;

import java.util.List;

public interface CustomVendorProductRepository {

    List<VendorProductCountAggregate> groupBy(List<Vendor> vendors);

}
