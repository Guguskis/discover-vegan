package lt.liutikas.repository;

import lt.liutikas.model.VendorProductChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorProductChangeRepository extends JpaRepository<VendorProductChange, Long> {

}
