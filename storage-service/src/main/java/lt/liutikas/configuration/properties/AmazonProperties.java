package lt.liutikas.configuration.properties;

import com.amazonaws.regions.Regions;
import org.springframework.boot.context.properties.ConfigurationProperties;
//import software.amazon.awssdk.regions.Region;

@ConfigurationProperties(prefix = "aws")
public class AmazonProperties {

    private String bucket;
    private Regions region;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Regions getRegion() {
        return region;
    }

    public void setRegion(Regions region) {
        this.region = region;
    }
}
