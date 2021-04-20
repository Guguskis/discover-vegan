package lt.liutikas.repository;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import lt.liutikas.configuration.properties.AmazonProperties;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Repository
public class StorageRepository {

    private final TransferManager transferManager;
    private final AmazonProperties amazonProperties;

    public StorageRepository(TransferManager transferManager, AmazonProperties amazonProperties) {
        this.transferManager = transferManager;
        this.amazonProperties = amazonProperties;
    }

    public String uploadFile(MultipartFile multipartFile, String fileName) throws IOException, InterruptedException {
        ObjectMetadata objectMetadata = getObjectMetadata(multipartFile);
        Upload upload = transferManager.upload(
                amazonProperties.getBucket(),
                fileName,
                multipartFile.getInputStream(),
                objectMetadata
        );
        upload.waitForCompletion();
        return String.format("https://%s.s3.amazonaws.com/%s", amazonProperties.getBucket(), fileName);
    }

    private ObjectMetadata getObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());
        return objectMetadata;
    }
}
