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
public class ImageStorageService {
    @Value("${image.upload.dir}")
    private String uploadDir;

    public String uploadImage(
            MultipartFile file,
            String name,
            String folderName) {

        try {

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // Validate content type
            String contentType = file.getContentType();

            if (contentType == null ||
                    !(contentType.equalsIgnoreCase("image/jpeg")
                            || contentType.equalsIgnoreCase("image/jpg")
                            || contentType.equalsIgnoreCase("image/png")
                            || contentType.equalsIgnoreCase("image/webp")
                            || contentType.equalsIgnoreCase("image/gif"))) {

                throw new RuntimeException(
                        "Only JPG, JPEG, PNG, WEBP and GIF images are allowed");
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
            List<String> allowedExtensions =
                    List.of(".jpg", ".jpeg", ".png", ".webp", ".gif");

            if (!allowedExtensions.contains(ext)) {
                throw new RuntimeException("Invalid image format");
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
                    "Image upload failed: " + e.getMessage()
            );
        }
    }


    public void deleteImage(String folderName, String fileName) {

        try {

            if (fileName == null || fileName.isBlank()) {
                return;
            }

            Path path = Paths.get(uploadDir, folderName, fileName);

            Files.deleteIfExists(path);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Image delete failed: " + e.getMessage()
            );
        }
    }

}
