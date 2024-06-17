package com.khair.zipfilesbackend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    byte[] processFiles(MultipartFile[] files, String clientIP) throws IOException;
}
