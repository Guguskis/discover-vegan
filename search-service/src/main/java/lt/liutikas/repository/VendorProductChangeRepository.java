package lt.liutikas.repository;

import lt.liutikas.model.VendorProduct;
import lt.liutikas.model.VendorProductChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendorProductChangeRepository extends JpaRepository<VendorProductChange, Long> {

    List<VendorProductChange> findAllByVendorProductAndCreatedAtBetween(VendorProduct vendorProduct, LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);
}
