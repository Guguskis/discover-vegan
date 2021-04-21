package lt.liutikas.repository;

import lt.liutikas.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {

    List<Vendor> findByExternalPlaceIdIn(List<String> placesIds);
}
