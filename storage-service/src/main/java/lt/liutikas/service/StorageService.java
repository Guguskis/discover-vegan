package lt.liutikas.service;

import lt.liutikas.configuration.exception.FileUploadException;
import lt.liutikas.dto.UploadFileDto;
import lt.liutikas.repository.StorageRepository;
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

    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public UploadFileDto uploadFile(MultipartFile multipartFile) {
        String fileName = generateFileName(multipartFile);

        String fileUrl;
        try {
            fileUrl = storageRepository.uploadFile(multipartFile, fileName);
        } catch (IOException | InterruptedException e) {
            LOG.error(String.format("Failed to upload file to cloud storage {name: %s, sizeBytes: %d}", fileName, multipartFile.getSize()), e);
            throw new FileUploadException(e);
        }

        LOG.info(String.format("Upload file to cloud storage {name: %s, sizeBytes: %d}", fileName, multipartFile.getSize()));

        UploadFileDto response = new UploadFileDto();
        response.setFileUrl(fileUrl);
        return response;
    }

    private String generateFileName(MultipartFile multipartFile) {
        UUID uuid = UUID.randomUUID();
        String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        return uuid.toString() + "." + fileExtension;
    }

}
