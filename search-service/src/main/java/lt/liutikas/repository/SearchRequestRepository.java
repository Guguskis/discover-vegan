package lt.liutikas.repository;

import lt.liutikas.dto.ProductsBySearchCount;
import lt.liutikas.model.Product;
import lt.liutikas.model.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SearchRequestRepository extends JpaRepository<SearchRequest, Long> {

    @Query(
            value = "SELECT sr.product as product, COUNT(sr) as searchCount " +
                    "FROM SearchRequest sr " +
                    "WHERE sr.createdAt >= :fromDate " +
                    "AND sr.createdAt <= :toDate " +
                    "GROUP BY sr.product"
    )
    Page<ProductsBySearchCount> findSearchRequestCount(Pageable pageable, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    List<SearchRequest> findAllByProductAndCreatedAtBetween(Product product, LocalDateTime localDateTimeStart, LocalDateTime localDateTimeEnd);
}
