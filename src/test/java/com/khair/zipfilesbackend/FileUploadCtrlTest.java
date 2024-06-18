package com.khair.zipfilesbackend;


import com.khair.zipfilesbackend.service.FileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class FileUploadCtrlTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    public void testUploadFiles() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("files", "file1.txt", "text/plain", "test file 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "file2.txt", "text/plain", "test file 2".getBytes());

        Mockito.when(fileService.processFiles(Mockito.any(), Mockito.anyString()))
                .thenReturn(new byte[0]);

        mockMvc.perform(multipart("/api/upload")
                        .file(file1)
                        .file(file2))
                .andExpect(status().isOk());
    }
}
