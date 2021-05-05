package lt.liutikas.repository;

import lt.liutikas.dto.SearchRequestGroupedByProduct;
import lt.liutikas.model.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRequestRepository extends JpaRepository<SearchRequest, Long> {

    @Query(
            value = "SELECT new lt.liutikas.dto.SearchRequestGroupedByProduct(sr.product, COUNT(sr))" +
                    "FROM SearchRequest sr " +
                    "GROUP BY sr.product",
            countQuery = "SELECT COUNT(sr)" +
                    "FROM SearchRequest sr " +
                    "GROUP BY sr.product"
    )
//    List<SearchRequestGroupedByProduct> findSearchRequestCount();
    Page<SearchRequestGroupedByProduct> findSearchRequestCount(Pageable pageable);

}
