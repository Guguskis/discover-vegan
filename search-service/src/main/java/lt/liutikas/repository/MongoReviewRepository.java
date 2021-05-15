package lt.liutikas.repository;

import lt.liutikas.model.MongoReview;
import lt.liutikas.model.MongoVendorProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MongoReviewRepository extends MongoRepository<MongoReview, String> {

    List<MongoReview> findAllByVendorProductAndCreatedAtBetween(MongoVendorProduct vendorProduct, LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);

}
