package lt.liutikas.repository;

import lt.liutikas.dto.SearchRequestAggregate;
import lt.liutikas.model.Product;
import lt.liutikas.model.SearchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchRequestRepository extends MongoRepository<SearchRequest, String> {

    @Aggregation("{ $group: { _id: $product, count: { $sum: 1 } } }")
    List<SearchRequestAggregate> groupByProductAndCreatedAtBetween(LocalDateTime fromDateTime, LocalDateTime toDateTime, Pageable page);

    List<SearchRequest> findAllByProductAndCreatedAtBetween(Product product, LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);

}
