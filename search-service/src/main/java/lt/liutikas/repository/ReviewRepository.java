package lt.liutikas.repository;

import lt.liutikas.model.Review;
import lt.liutikas.model.VendorProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByVendorProductAndCreatedAtBetween(VendorProduct vendorProduct, LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);

}
