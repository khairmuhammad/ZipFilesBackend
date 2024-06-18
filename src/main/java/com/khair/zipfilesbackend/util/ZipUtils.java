package com.khair.zipfilesbackend.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    /**
     * Creates a zip archive from an array of MultipartFile objects.
     *
     * @param files  Array of MultipartFile objects representing the files to be zipped.
     * @return A byte array containing the zipped files.
     * @throws IOException  If an input or output exception occurs during zipping.
     */
    public static byte[] zipFiles(MultipartFile[] files) throws IOException {
        // Use try-with-resources to ensure streams are properly closed
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            // Iterate through each file and add it to the zip archive
            for (MultipartFile file : files) {
                // Create a new zip entry for the file
                ZipEntry zipEntry = new ZipEntry(file.getOriginalFilename());
                zipEntry.setSize(file.getSize());

                // Add the zip entry to the ZipOutputStream
                zipOutputStream.putNextEntry(zipEntry);

                // Write the file's bytes to the ZipOutputStream
                zipOutputStream.write(file.getBytes());

                // Close the current zip entry
                zipOutputStream.closeEntry();
            }

            // Complete the zip process
            zipOutputStream.finish();

            // Return the zipped file as a byte array
            return byteArrayOutputStream.toByteArray();
        }
    }
}
