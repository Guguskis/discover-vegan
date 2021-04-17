package lt.liutikas.controller;

import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.UploadFileDto;
import lt.liutikas.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file/")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadFileDto> uploadFile(@RequestBody UploadFileDto request) {

        throw new NotFoundException("Test");
    }
}
