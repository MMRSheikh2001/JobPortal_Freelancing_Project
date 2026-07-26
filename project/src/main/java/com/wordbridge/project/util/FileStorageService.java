package com.wordbridge.project.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${image.upload.dir}")
    private String uploadDir;


    public String uploadFile(
            MultipartFile file,
            String name,
            String folderName) {

        try {

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            long maxSize = 10 * 1024 * 1024; // 5 MB

            if (file.getSize() > maxSize) {
                throw new RuntimeException(
                        "File size cannot exceed 10 MB"
                );
            }

            // Validate content type
            String contentType = file.getContentType();

            if (contentType == null ||
                    !(contentType.equals("application/pdf")
                            || contentType.equals("image/jpeg")
                            || contentType.equals("image/png"))) {

                throw new RuntimeException(
                        "Only PDF, JPG and PNG files are allowed"
                );
            }

            Path path = Paths.get(uploadDir, folderName);

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String original = file.getOriginalFilename();

            String ext = "";

            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."))
                        .toLowerCase();
            }
            List<String> allowed =
                    List.of(".pdf", ".jpg", ".jpeg", ".png");

            if (!allowed.contains(ext)) {
                throw new RuntimeException("Invalid file format");
            }

            String fileName =
                    name.trim().replaceAll("\\s+", "_")
                            + "_"
                            + UUID.randomUUID()
                            + ext;

            Files.copy(
                    file.getInputStream(),
                    path.resolve(fileName)
            );

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException(
                    "File upload failed: " + e.getMessage()
            );
        }
    }


    public void deleteFile(String folderName, String fileName) {

        try {

            if (fileName == null || fileName.isBlank()) {
                return;
            }

            Path path = Paths.get(uploadDir, folderName, fileName);

            Files.deleteIfExists(path);

        } catch (Exception e) {
            throw new RuntimeException(
                    "File deleting failed: " + e.getMessage()
            );
        }
    }

    public String uploadPortfolioFile(
            MultipartFile file,
            String name,
            String folderName) {

        try {

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            long maxSize = 1000 * 1024 * 1024; // 1000 MB

            if (file.getSize() > maxSize) {
                throw new RuntimeException("Portfolio file cannot exceed 1000 MB");
            }

            String original = file.getOriginalFilename();
            String ext = "";

            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."))
                        .toLowerCase();
            }

            List<String> allowed = List.of(
                    ".pdf",
                    ".zip",
                    ".rar",
                    ".7z",
                    ".doc",
                    ".docx",
                    ".ppt",
                    ".pptx",
                    ".xls",
                    ".xlsx",
                    ".txt",
                    ".jpg",
                    ".jpeg",
                    ".png",
                    ".gif",
                    ".mp4",
                    ".mov",
                    ".avi",
                    ".mkv"
            );

            if (!allowed.contains(ext)) {
                throw new RuntimeException("Invalid portfolio file format");
            }

            Path path = Paths.get(uploadDir, folderName);

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String fileName =
                    name.trim().replaceAll("\\s+", "_")
                            + "_"
                            + UUID.randomUUID()
                            + ext;

            Files.copy(
                    file.getInputStream(),
                    path.resolve(fileName)
            );

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Portfolio upload failed: " + e.getMessage()
            );
        }
    }


}
