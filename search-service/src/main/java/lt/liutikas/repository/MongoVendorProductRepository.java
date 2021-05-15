package lt.liutikas.repository;

import lt.liutikas.model.MongoProduct;
import lt.liutikas.model.MongoVendor;
import lt.liutikas.model.MongoVendorProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoVendorProductRepository extends MongoRepository<MongoVendorProduct, String> {

    Page<MongoVendorProduct> findAllByVendor(MongoVendor vendor, Pageable pageable);

    List<MongoVendorProduct> findAllByVendor(MongoVendor vendor);

    Page<MongoVendorProduct> findAllByProduct(MongoProduct product, Pageable pageable);

    MongoVendorProduct findAllByProductAndVendor(MongoProduct product, MongoVendor vendor);
}
