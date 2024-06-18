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

    /**
     * Processes the uploaded files by validating their size, updating upload statistics, and zipping the files.
     *
     * @param files  Array of MultipartFile objects representing the files uploaded by the client.
     * @param clientIP  String representing the IP address of the client.
     * @return A byte array containing the zipped files.
     * @throws IOException  If no files are provided or if any file exceeds the size limit.
     */
    @Override
    public byte[] processFiles(MultipartFile[] files, String clientIP) throws IOException {
        // Check if no files are provided
        if (files.length == 0) {
            throw new IOException("No files provided.");
        }

        // Validate each file's size to ensure it does not exceed the limit of 1MB
        for (MultipartFile file : files) {
            if (file.getSize() > (MBs * 1024 * 1024)) { // 1MB = 1048576 bytes
                throw new IOException("File size exceeds limit of 1MB.");
            }
        }

        // Update upload statistics for the client
        updateUploadStats(clientIP);

        // Zip the files and return the resulting byte array
        return ZipUtils.zipFiles(files);
    }

    /**
     * Updates the upload statistics for a given client's IP address.
     *
     * @param clientIP  String representing the IP address of the client.
     */
    private void updateUploadStats(String clientIP) {
        // Get today's date
        LocalDate today = LocalDate.now();

        // Find existing upload statistics for the client IP and today's date, or create a new record
        UploadStats stats = uploadStatsRepository.findByIpAndDate(clientIP, today)
                .orElse(new UploadStats(clientIP, today, 0));

        // Increment the upload count
        stats.setCount(stats.getCount() + 1);

        // Save the updated statistics
        uploadStatsRepository.save(stats);
    }
}
