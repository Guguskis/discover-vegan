package lt.liutikas.repository;

import lt.liutikas.entity.Vendor;
import lt.liutikas.entity.VendorProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorProductRepository extends JpaRepository<VendorProduct, Long> {

    List<VendorProduct> findAllByVendor(Vendor vendor);
}
