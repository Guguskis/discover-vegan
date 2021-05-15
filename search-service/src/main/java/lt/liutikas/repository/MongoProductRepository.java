package lt.liutikas.repository;

import lt.liutikas.model.MongoProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoProductRepository extends MongoRepository<MongoProduct, String> {

    Page<MongoProduct> findByNameLikeIgnoreCaseOrderByNameAsc(Pageable pageable, String name);

    Page<MongoProduct> findByNameRegexOrderByNameAsc(Pageable pageable, String name);

}
