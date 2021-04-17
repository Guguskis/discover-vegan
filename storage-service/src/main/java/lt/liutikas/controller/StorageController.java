package lt.liutikas.controller;

import lt.liutikas.dto.UploadFileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file/")
public class StorageController {

    @PostMapping("/upload")
    public ResponseEntity<UploadFileDto> uploadFile(@RequestBody UploadFileDto request) {

        throw new RuntimeException("Test");
    }
}
