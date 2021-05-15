package lt.liutikas.repository;

import lt.liutikas.model.MongoSearchRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoSearchRequestRepository extends MongoRepository<MongoSearchRequest, String> {

    //    @Query(
//            value = "SELECT sr.product as product, COUNT(sr) as searchCount " +
//                    "FROM SearchRequest sr " +
//                    "WHERE sr.createdAt >= :fromDate " +
//                    "AND sr.createdAt <= :toDate " +
//                    "GROUP BY sr.product"
//    )
//    Page<ProductsBySearchCount> findSearchRequestCount(Pageable pageable, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
//
//    List<MongoSearchRequest> findAllByProductAndCreatedAtBetween(MongoProduct product, LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);
}
