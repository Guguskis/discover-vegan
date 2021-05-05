package lt.liutikas.repository;

import lt.liutikas.dto.ProductsBySearchCount;
import lt.liutikas.model.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRequestRepository extends JpaRepository<SearchRequest, Long> {

    @Query(
            value = "SELECT sr.product as product, COUNT(sr) as searchCount " +
                    "FROM SearchRequest sr " +
                    "GROUP BY sr.product"
    )
    Page<ProductsBySearchCount> findSearchRequestCount(Pageable pageable);

}
