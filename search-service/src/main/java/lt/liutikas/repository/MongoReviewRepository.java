package lt.liutikas.repository;

import lt.liutikas.model.MongoReview;
import lt.liutikas.model.VendorProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MongoReviewRepository extends MongoRepository<MongoReview, String> {

    List<MongoReview> findAllByVendorProductAndCreatedAtBetween(VendorProduct vendorProduct, LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);

}
