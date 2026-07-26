package com.wordbridge.project.controller;

import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    @Value("${image.upload.dir}")
    private String uploadDir;

    @GetMapping("/userprofiles/{filename}")
    public ResponseEntity<Resource> getUserProfileImage(
            @PathVariable String filename) throws IOException {

        return serveFile("userprofiles", filename);
    }

    @GetMapping("/companyprofiles/{filename}")
    public ResponseEntity<Resource> getCompanyProfileImage(
            @PathVariable String filename) throws IOException {

        return serveFile("companyprofiles", filename);
    }

    @GetMapping("/resumes/{filename}")
    public ResponseEntity<Resource> getResume(
            @PathVariable String filename) throws IOException {

        return serveFile("resumes", filename);
    }

    @GetMapping("/portfolios/{filename}")
    public ResponseEntity<Resource> getPortfolioFile(
            @PathVariable String filename) throws IOException {

        return serveFile("portfolios", filename);
    }

    @GetMapping("/trainings/{filename}")
    public ResponseEntity<Resource> getTrainingFile(
            @PathVariable String filename) throws IOException {

        return serveFile("trainings", filename);
    }

    @GetMapping("/messages/{filename}")
    public ResponseEntity<Resource> getMessageFile(
            @PathVariable String filename) throws IOException {

        return serveFile("messages", filename);
    }

    @GetMapping("/gigdeliveries/{filename}")
    public ResponseEntity<Resource> getGigDeliveryFile(
            @PathVariable String filename) throws IOException {

        return serveFile("gigdeliveries", filename);
    }

    //open api
    @GetMapping("/gigs/{filename}")
    public ResponseEntity<Resource> getGigImage(
            @PathVariable String filename) throws IOException {

        return serveFile("gigs", filename);
    }

    @GetMapping("/reports/{filename}")
    public ResponseEntity<Resource> getReportsAttachment(
            @PathVariable String filename) throws IOException {

        return serveFile("reports", filename);
    }


    //private method
    private ResponseEntity<Resource> serveFile(String folder, String filename) throws IOException {

        Path path = Paths.get(uploadDir, folder, filename);

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("File not found : " + filename);
        }

        String contentType = java.nio.file.Files.probeContentType(path);

        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }


}
