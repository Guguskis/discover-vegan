package lt.liutikas.repository;

import lt.liutikas.dto.VendorProductCountAggregate;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomVendorProductRepositoryImpl implements CustomVendorProductRepository {

    private final MongoOperations mongoOperations;

    public CustomVendorProductRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<VendorProductCountAggregate> groupBy(List<Vendor> vendors) {


        MatchOperation match = Aggregation.match(Criteria.where("vendor").in(vendors));
        GroupOperation group = Aggregation.group("vendor").count().as("count");

        Aggregation aggregate = Aggregation.newAggregation(
                match,
                group
        );

        AggregationResults<VendorProductCountAggregate> orderAggregate = mongoOperations.aggregate(
                aggregate,
                VendorProduct.class,
                VendorProductCountAggregate.class
        );

        return orderAggregate.getMappedResults();
    }

}
