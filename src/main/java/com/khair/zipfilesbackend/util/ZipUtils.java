package com.khair.zipfilesbackend.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    public static byte[] zipFiles(MultipartFile[] files) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            for (MultipartFile file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getOriginalFilename());
                zipEntry.setSize(file.getSize());
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(file.getBytes());
                zipOutputStream.closeEntry();
            }

            zipOutputStream.finish();
            return byteArrayOutputStream.toByteArray();
        }
    }
}
