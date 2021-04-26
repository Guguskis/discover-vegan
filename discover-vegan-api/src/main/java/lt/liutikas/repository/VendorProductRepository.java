package lt.liutikas.repository;

import lt.liutikas.model.Product;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorProductRepository extends JpaRepository<VendorProduct, Long> {


    Page<VendorProduct> findAllByVendor(Vendor vendor, Pageable pageable);

    Page<VendorProduct> findAllByProduct(Product product, Pageable pageable);
}
