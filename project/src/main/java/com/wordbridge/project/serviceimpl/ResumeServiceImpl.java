package com.wordbridge.project.serviceimpl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.wordbridge.project.dto.responsedto.ResumeResponseDTO;
import com.wordbridge.project.dto.responsedto.UserSkillResponseDTO;
import com.wordbridge.project.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final UserProfileService userProfileService;
    private final EducationService educationService;
    private final ExperienceService experienceService;
    private final UserSkillService userSkillService;
    private final UserLanguageService userLanguageService;
    private final TrainingService trainingService;
    private final PortfolioService portfolioService;
    private final ReferenceService referenceService;
    private final ExtracurricularService extracurricularService;

    // Spring auto-configures this bean from spring-boot-starter-thymeleaf
    private final TemplateEngine templateEngine;

    // Non-final so @Value injection works alongside @RequiredArgsConstructor
    @Value("${image.upload.dir}")
    private String uploadDir;

    // ── generateResume ────────────────────────────────────────────

    @Override
    public ResumeResponseDTO generateResume(Long userProfileId) {
        ResumeResponseDTO dto = new ResumeResponseDTO();
        dto.setProfile(userProfileService.findById(userProfileId));
        dto.setEducations(educationService.findByUserProfileId(userProfileId));
        dto.setExperiences(experienceService.findByUserProfileId(userProfileId));
        dto.setSkills(userSkillService.findByUserProfileId(userProfileId));
        dto.setTrainings(trainingService.findByUserProfileId(userProfileId));
        dto.setPortfolios(portfolioService.findByUserProfileId(userProfileId));
        dto.setLanguages(userLanguageService.findByUserProfileId(userProfileId));
        dto.setReferences(referenceService.findByUserProfileId(userProfileId));
        dto.setExtracurriculars(extracurricularService.findByUserProfileId(userProfileId));
        return dto;
    }

    // ── generateHtml ─────────────────────────────────────────────

    @Override
    public String generateHtml(Long userProfileId) {
        ResumeResponseDTO resume = generateResume(userProfileId);

        Context context = new Context();
        context.setVariable("resume", resume);

        // Photo as Base64 data URI — no photo means variable is absent from context
        // Template handles null gracefully with th:if="${photoDataUri != null}"
        if (resume.getProfile() != null
                && resume.getProfile().getImage() != null
                && !resume.getProfile().getImage().isBlank()) {
            try {
                Path imagePath = Paths.get(
                        uploadDir, "userprofiles", resume.getProfile().getImage()
                );
                if (Files.exists(imagePath)) {
                    byte[] bytes = Files.readAllBytes(imagePath);
                    String base64 = Base64.getEncoder().encodeToString(bytes);
                    String mime = Files.probeContentType(imagePath);
                    if (mime == null) mime = "image/jpeg";
                    context.setVariable(
                            "photoDataUri", "data:" + mime + ";base64," + base64
                    );
                }
            } catch (Exception ignored) {
                // Photo unreadable — template renders without it
            }
        }

        // Group skills by category: { "Web": "Angular, Spring Boot", "Design": "Figma" }
        // Pre-process here so template stays clean
        if (resume.getSkills() != null && !resume.getSkills().isEmpty()) {
            Map<String, List<String>> grouped = new LinkedHashMap<>();
            for (UserSkillResponseDTO skill : resume.getSkills()) {
                String category = (skill.getCategoryName() != null
                        && !skill.getCategoryName().isBlank())
                        ? skill.getCategoryName()
                        : "General";
                grouped.computeIfAbsent(category, k -> new ArrayList<>())
                        .add(skill.getSkillName());
            }
            Map<String, String> skillsByCat = new LinkedHashMap<>();
            grouped.forEach((cat, names) ->
                    skillsByCat.put(cat, String.join(", ", names)));
            context.setVariable("skillsByCat", skillsByCat);
        }

        return templateEngine.process("resume", context);
    }

    // ── generatePdf ──────────────────────────────────────────────

    @Override
    public byte[] generatePdf(Long userProfileId) {
        String html = generateHtml(userProfileId);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed: " + e.getMessage());
        }
    }
}