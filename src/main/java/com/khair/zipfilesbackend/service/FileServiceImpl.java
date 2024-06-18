package com.khair.zipfilesbackend.service;

import com.khair.zipfilesbackend.model.UploadStats;
import com.khair.zipfilesbackend.repository.UploadStatsRepository;
import com.khair.zipfilesbackend.util.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private UploadStatsRepository uploadStatsRepository;

    private final int MBs = 1;

    @Override
    public byte[] processFiles(MultipartFile[] files, String clientIP) throws IOException {
        if (files.length == 0) {
            throw new IOException("No files provided.");
        }

        for (MultipartFile file : files) {
            if (file.getSize() > (MBs * 1024 * 1024)) { // 1MB = 1048576 bytes
                throw new IOException("File size exceeds limit of 1MB.");
            }
        }

        updateUploadStats(clientIP);
        return ZipUtils.zipFiles(files);
    }

    private void updateUploadStats(String clientIP) {
        LocalDate today = LocalDate.now();
        UploadStats stats = uploadStatsRepository.findByIpAndDate(clientIP, today)
                .orElse(new UploadStats(clientIP, today, 0));
        stats.setCount(stats.getCount() + 1);
        uploadStatsRepository.save(stats);
    }
}
