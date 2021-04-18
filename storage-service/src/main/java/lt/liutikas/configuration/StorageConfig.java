package lt.liutikas.configuration;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import lt.liutikas.configuration.properties.AmazonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

//    @Bean
//    public S3AsyncClient s3client(AmazonProperties properties) {
//        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
//                .writeTimeout(Duration.ZERO)
//                .maxConcurrency(64)
//                .build();
//        S3Configuration serviceConfiguration = S3Configuration.builder()
//                .checksumValidationEnabled(false)
//                .chunkedEncodingEnabled(true)
//                .build();
//        S3AsyncClientBuilder b = S3AsyncClient.builder().httpClient(httpClient)
//                .region(properties.getRegion())
//                .credentialsProvider(SystemPropertyCredentialsProvider.create())
//                .serviceConfiguration(serviceConfiguration);
//
//        return b.build();
//    }

    @Bean
    public AmazonS3 getAmazonS3(AmazonProperties amazonProperties) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withRegion(amazonProperties.getRegion())
                .build();
    }

    @Bean
    public TransferManager getTransferManager(AmazonS3 amazonS3) {
        return TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                .build();
    }
}