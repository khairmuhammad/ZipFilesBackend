package com.khair.zipfilesbackend;


import com.khair.zipfilesbackend.controller.FileUploadCtrl;
import com.khair.zipfilesbackend.model.UploadStats;
import com.khair.zipfilesbackend.repository.UploadStatsRepository;
import com.khair.zipfilesbackend.service.FileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileUploadCtrl.class)
public class FileUploadCtrlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @MockBean
    private UploadStatsRepository uploadStatsRepository;

    /**
     * Tests the uploadFiles method by simulating a multipart file upload request.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testUploadFiles() throws Exception {
        // Create mock multipart files for the test
        MockMultipartFile file1 = new MockMultipartFile("files", "file1.txt", "text/plain", "test file 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "file2.txt", "text/plain", "test file 2".getBytes());

        // Mock the fileService's processFiles method to return an empty byte array
        Mockito.when(fileService.processFiles(Mockito.any(), Mockito.anyString()))
                .thenReturn(new byte[0]);

        // Perform a multipart file upload request to the /api/upload endpoint
        mockMvc.perform(multipart("/api/upload")
                        .file(file1)
                        .file(file2))
                .andExpect(status().isOk()); // Expect a 200 OK status in the response
    }

    /**
     * Tests the uploadFiles method to ensure it validates file sizes correctly.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testUploadFiles_FileSizeValidation() throws Exception {
        // Create a mock multipart file that exceeds the size limit (assuming the limit is 1MB)
        byte[] largeFileContent = new byte[2 * 1024 * 1024]; // 2MB
        MockMultipartFile largeFile = new MockMultipartFile("files", "largeFile.txt", "text/plain", largeFileContent);

        // Mock the fileService to throw an IOException when processing large files
        Mockito.doThrow(new IOException("File size exceeds limit of 1MB."))
                .when(fileService).processFiles(Mockito.any(), Mockito.anyString());

        // Perform a multipart file upload request and expect a 400 Bad Request status
        mockMvc.perform(multipart("/api/upload")
                        .file(largeFile))
                .andExpect(status().isBadRequest());
    }


    /**
     * Tests the uploadFiles method to ensure it correctly archives the uploaded files.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testUploadFiles_ArchivingFunctionality() throws Exception {
        // Create mock multipart files for the test
        MockMultipartFile file1 = new MockMultipartFile("files", "file1.txt", "text/plain", "test file 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "file2.txt", "text/plain", "test file 2".getBytes());

        // Mock the fileService's processFiles method to return a non-empty byte array
        byte[] zipData = {1, 2, 3}; // Sample zip data
        Mockito.when(fileService.processFiles(Mockito.any(), Mockito.anyString()))
                .thenReturn(zipData);

        // Perform a multipart file upload request and expect a 200 OK status
        mockMvc.perform(multipart("/api/upload")
                        .file(file1)
                        .file(file2))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=files.zip"))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(zipData));
    }

    /**
     * Tests the uploadFiles method to ensure it updates the usage statistics correctly.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testUploadFiles_UsageStatisticsTracking() throws Exception {
        // Create mock multipart files for the test
        MockMultipartFile file1 = new MockMultipartFile("files", "file1.txt", "text/plain", "test file 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "file2.txt", "text/plain", "test file 2".getBytes());

        // Mock the fileService's processFiles method to return a non-empty byte array
        byte[] zipData = {1, 2, 3}; // Sample zip data
        Mockito.when(fileService.processFiles(Mockito.any(), Mockito.anyString()))
                .thenReturn(zipData);

        // Mock the repository to return an empty Optional when searching for existing stats
        Mockito.when(uploadStatsRepository.findByIpAndDate(Mockito.anyString(), Mockito.any(LocalDate.class)))
                .thenReturn(Optional.empty());

        // Mock the save method to return the UploadStats object and verify it
        Mockito.doAnswer(invocation -> {
            UploadStats stats = invocation.getArgument(0);
            // Additional assertions to verify stats if needed
            System.out.println("Saving stats: " + stats);
            return stats;
        }).when(uploadStatsRepository).save(any(UploadStats.class));

        // Perform a multipart file upload request
        mockMvc.perform(multipart("/api/upload")
                        .file(file1)
                        .file(file2))
                .andExpect(status().isOk());

        // Verify that the upload statistics were updated
        Mockito.verify(uploadStatsRepository, Mockito.times(1)).save(Mockito.any(UploadStats.class));
    }
}
