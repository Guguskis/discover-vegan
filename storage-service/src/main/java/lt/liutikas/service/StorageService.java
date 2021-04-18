package lt.liutikas.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import lt.liutikas.configuration.exception.FileUploadException;
import lt.liutikas.configuration.properties.AmazonProperties;
import lt.liutikas.dto.UploadFileDto;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class StorageService {

    private static final Logger LOG = LoggerFactory.getLogger(StorageService.class);

    private final TransferManager transferManager;
    private final AmazonProperties amazonProperties;

    public StorageService(TransferManager transferManager, AmazonProperties amazonProperties) {
        this.transferManager = transferManager;
        this.amazonProperties = amazonProperties;
    }

    public UploadFileDto uploadFile(MultipartFile multipartFile) {
        UUID uuid = UUID.randomUUID();
        String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String fileName = uuid.toString() + "." + fileExtension;

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());
            Upload upload = transferManager.upload(amazonProperties.getBucket(), fileName, multipartFile.getInputStream(), objectMetadata);
            upload.waitForCompletion();
        } catch (IOException | InterruptedException e) {
            LOG.error(String.format("Failed to upload file to cloud storage {name: %s, sizeBytes: %d}", fileName, multipartFile.getSize()));
            throw new FileUploadException(e);
        }

        String fileUrl = String.format("https://%s.s3.amazonaws.com/%s", amazonProperties.getBucket(), fileName);
        System.out.println(fileUrl);

        LOG.info(String.format("Upload file to cloud storage {name: %s, sizeBytes: %d}", fileName, multipartFile.getSize()));

        UploadFileDto response = new UploadFileDto();
        response.setFileUrl(fileUrl);
        return response;
    }

}
