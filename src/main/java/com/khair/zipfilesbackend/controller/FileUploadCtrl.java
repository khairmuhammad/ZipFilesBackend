package com.khair.zipfilesbackend.controller;

import com.khair.zipfilesbackend.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileUploadCtrl {

    @Autowired
    private FileService fileService;

    /**
     * Handles file upload requests and returns a zipped file containing the uploaded files.
     *
     * @param files  Array of MultipartFile objects representing the files uploaded by the client.
     * @param request  HttpServletRequest object to retrieve client's IP address.
     * @return ResponseEntity with the zipped file as an attachment.
     * @throws IOException  If an input or output exception occurs.
     */
    @PostMapping("/upload")
    public ResponseEntity<InputStreamResource> uploadFiles(@RequestParam("files") MultipartFile[] files,
                                                           HttpServletRequest request) throws IOException {
        // Retrieve the client's IP address from the request
        String clientIP = request.getRemoteAddr();

        // Process the uploaded files and generate a zip file containing these files
        byte[] zipData = fileService.processFiles(files, clientIP);

        // Return the zipped file as a response entity with appropriate headers
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=files.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(new ByteArrayInputStream(zipData)));
    }
}
