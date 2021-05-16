package lt.liutikas.repository;

import lt.liutikas.model.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VendorRepository extends MongoRepository<Vendor, String> {

    List<Vendor> findByExternalPlaceIdIn(List<String> placesIds);
}
