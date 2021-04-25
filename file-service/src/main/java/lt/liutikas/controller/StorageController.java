package lt.liutikas.controller;

import lt.liutikas.dto.UploadFileDto;
import lt.liutikas.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/file")
    public ResponseEntity<UploadFileDto> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        return ResponseEntity.ok(storageService.uploadFile(multipartFile));
    }
}
