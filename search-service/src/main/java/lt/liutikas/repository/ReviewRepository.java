package lt.liutikas.repository;

import lt.liutikas.model.Review;
import lt.liutikas.model.VendorProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findAllByVendorProductAndCreatedAtBetween(VendorProduct vendorProduct, LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);

}
