package com.khair.zipfilesbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public byte[] processFiles(MultipartFile[] files, String clientIP) throws IOException {
        return new byte[0];
    }
}
