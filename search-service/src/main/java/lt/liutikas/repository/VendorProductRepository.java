package lt.liutikas.repository;

import lt.liutikas.model.Product;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VendorProductRepository extends MongoRepository<VendorProduct, String>, CustomVendorProductRepository {

    Page<VendorProduct> findAllByVendor(Vendor vendor, Pageable pageable);

    Page<VendorProduct> findAllByProduct(Product product, Pageable pageable);

    Optional<VendorProduct> findByProductAndVendor(Product product, Vendor vendor);
}
