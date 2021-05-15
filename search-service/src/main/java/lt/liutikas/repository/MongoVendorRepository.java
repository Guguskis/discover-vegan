package lt.liutikas.repository;

import lt.liutikas.model.MongoVendor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoVendorRepository extends MongoRepository<MongoVendor, String> {

    List<MongoVendor> findByExternalPlaceIdIn(List<String> placesIds);
}
