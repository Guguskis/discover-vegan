package lt.liutikas.service;

import lt.liutikas.dto.UploadFileDto;
import lt.liutikas.repository.StorageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class StorageServiceTest {

    @Mock
    private StorageRepository storageRepository;

    private StorageService storageService;

    @Before
    public void setUp() {
        storageService = new StorageService(storageRepository);
    }

    @Test
    public void uploadFile_validFile_uploadedFileUrlReturned() throws IOException, InterruptedException {
        String uploadedFileName = "25c06e1a-a059-11eb-bcbc-0242ac130002.png";
        MockMultipartFile image = new MockMultipartFile(
                "image-file",
                "smoked-tofu.png",
                "image/png",
                "bytes-for-image".getBytes()
        );

        when(storageRepository.uploadFile(any(), anyString()))
                .thenReturn(uploadedFileName);

        UploadFileDto uploadFileDto = storageService.uploadFile(image);

        assertEquals(uploadedFileName, uploadFileDto.getFileUrl());
    }
}
