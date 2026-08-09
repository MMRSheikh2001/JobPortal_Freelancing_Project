package com.wordbridge.project.resumeimport;

import com.wordbridge.project.ai.service.GeminiService;
import com.wordbridge.project.dto.responsedto.ResumeResponseDTO;
import com.wordbridge.project.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;


@Component
@RequiredArgsConstructor
public class GeminiResumeParserImpl implements GeminiResumeParser {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;


    @Override
    public ResumeImportPreviewDTO parseResume(
            String resumeText,
            ResumeResponseDTO currentResume
    ) {

        try {

            // Build prompt
            String prompt = buildPrompt(
                    resumeText,
                    currentResume
            );

            System.out.println("========== GEMINI PROMPT ==========");
            System.out.println(prompt);

            // Ask Gemini
            String response = geminiService.askGemini(prompt);

            // Gemini sometimes wraps JSON in ```json ... ```
            response = cleanJson(response);

            System.out.println("========== GEMINI RESPONSE ==========");
            System.out.println(response);

            // Convert JSON into DTO
            return objectMapper.readValue(
                    response,
                    ResumeImportPreviewDTO.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to parse resume using AI.",
                    e
            );

        }

    }

    private String cleanJson(String response) {

        if (response == null) {
            return "";
        }

        response = response.trim();

        if (response.startsWith("```json")) {

            response = response.substring(7);

        } else if (response.startsWith("```")) {

            response = response.substring(3);

        }

        if (response.endsWith("```")) {

            response =
                    response.substring(
                            0,
                            response.length() - 3
                    );

        }

        return response.trim();

    }


    private String buildPrompt(
            String resumeText,
            ResumeResponseDTO currentResume
    ) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String currentProfileJson =
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(currentResume);

        return """
                You are an expert AI Resume Parser for the WorkBridge Job Portal.
                
                ========================================================
                YOUR TASK
                ========================================================
                
                Compare the CURRENT PROFILE with the uploaded RESUME.
                
                Your job is ONLY to extract NEW information that should be imported into the user's profile.
                
                The purpose of this feature is to help lazy users automatically fill missing profile information.
                
                ========================================================
                VERY IMPORTANT RULES
                ========================================================
                
                1. NEVER overwrite existing profile information.
                
                2. Compare CURRENT PROFILE with RESUME.
                
                3. Return ONLY information that is missing from the CURRENT PROFILE.
                
                4. Ignore duplicate information.
                
                5. Never guess.
                
                6. Never invent information.
                
                7. If confidence is low, leave the field null or the list empty.
                
                8. Preserve names exactly as they appear in the resume.
                
                9. Never rename JSON properties.
                
                10. Never create additional properties.
                
                11. Return ONLY valid JSON.
                
                12. Do NOT wrap JSON inside markdown.
                
                13. Do NOT explain anything.
                
                14. Do NOT include comments.
                
                15. If a section has nothing new, return an empty list.
                
                16. If profile contains nothing new, return null.
                17. Never generate values for backend generated fields such as
                
                id
                
                createdAt
                
                updatedAt
                
                userId
                
                userProfileId
                
                address ids
                
                country ids
                
                district ids
                
                division ids
                
                policeStation ids
                
                Leave those fields null.
                18.
                
                All dates must use
                
                yyyy-MM-dd
                
                Example
                
                2023-05-12
                
                Do not use month names.
                
                ========================================================
                ENUMS
                ========================================================
                
                Whenever an enum field is returned,
                use the EXACT enum constant below.
                
                %s
                
                ========================================================
                OUTPUT DTO DEFINITIONS
                ========================================================
                
                The returned JSON MUST match these Java DTOs exactly.
                
                %s
                
                ========================================================
                EXAMPLE JSON
                ========================================================
                
                {
                  "profile": {
                    "professionalTitle": "Java Developer",
                    "summary": "Spring Boot Developer",
                    "gender": "MALE"
                  },
                
                  "educations": [],
                
                  "experiences": [],
                
                  "skills": [
                    {
                      "skillName": "Spring Boot"
                    }
                  ],
                
                  "languages": [],
                
                  "trainings": [],
                
                  "portfolios": [],
                
                  "references": [],
                
                  "extracurriculars": []
                }
                
                ========================================================
                Treat CURRENT PROFILE as the source of truth.
                
                Treat the resume only as a source of missing information.
                CURRENT PROFILE
                ========================================================
                
                %s
                
                ========================================================
                RESUME TEXT
                ========================================================
                
                %s
                
                ========================================================
                FINAL REMINDER
                ========================================================
                
                Return ONLY the JSON object.
                
                Do NOT explain your answer.
                
                Do NOT use markdown.
                
                Do NOT return anything except JSON.
                If information already exists in CURRENT PROFILE,
                DO NOT return it.
                
                Return ONLY newly discovered information.
                
                Do not return unchanged values.
                
                """
                .formatted(
                        getEnumDefinitions(),
                        getDtoDefinitions(),
                        currentProfileJson,
                        resumeText
                );
    }


    private String getDtoDefinitions() {
        return """
                            public class ResumeImportPreviewDTO {
                
                                private UserProfileResponseDTO profile;
                
                                private List<EducationResponseDTO> educations;
                
                                private List<ExperienceResponseDTO> experiences;
                
                                private List<UserSkillResponseDTO> skills;
                
                                private List<UserLanguageResponseDTO> languages;
                
                                private List<TrainingResponseDTO> trainings;
                
                                private List<PortfolioResponseDTO> portfolios;
                
                                private List<ReferenceResponseDTO> references;
                
                                private List<ExtracurricularResponseDTO> extracurriculars;
                
                            }
                
                            public class UserProfileResponseDTO {
                                private Long id;
                
                                private Long userId;
                                private String userEmail;
                
                                private String name;
                                private String phone;
                                private String image;
                
                                private String headline;
                                private String professionalSummary;
                                private String bio;
                
                                private LocalDate dateOfBirth;
                
                                private GenderType gender;
                                private String nationality;
                                private String religion;
                                private String maritalStatus;
                
                                private String fatherName;
                                private String motherName;
                
                                private String nidNumber;
                                private String passportNumber;
                
                                private String githubLink;
                                private String linkedinLink;
                                private String portfolioWebsite;
                
                
                                private BigDecimal expectedSalary;
                                private BigDecimal currentSalary;
                
                
                                private JobType preferredJobType;
                                private WorkPlaceType preferredWorkplace;
                
                                private String careerObjective;
                                private String freelancerTitle;
                
                                private Long presentAddressId;
                                private String presentAddressDetails;
                                private String presentAddressPostCode;
                
                                private Long presentCountryId;
                                private String presentCountryName;
                                private String presentCountryCode;
                
                                private Long presentDivisionId;
                                private String presentDivisionName;
                
                                private Long presentDistrictId;
                                private String presentDistrictName;
                
                                private Long presentPoliceStationId;
                                private String presentPoliceStationName;
                
                                private Long permanentAddressId;
                                private String permanentAddressDetails;
                                private String permanentAddressPostCode;
                
                
                                private Long permanentCountryId;
                                private String permanentCountryName;
                                private String permanentCountryCode;
                
                                private Long permanentDivisionId;
                                private String permanentDivisionName;
                
                                private Long permanentDistrictId;
                                private String permanentDistrictName;
                
                                private Long permanentPoliceStationId;
                                private String permanentPoliceStationName;
                
                
                                private Boolean profileCompleted;
                                private LocalDateTime createdAt;
                                private LocalDateTime updatedAt;
                
                
                            }
                
                            public class EducationResponseDTO {
                                private Long id;
                
                
                                private EducationLevel educationLevel;
                
                                private String board;
                
                                private String institution;
                
                                private String fieldOfStudy;
                
                
                                private ResultType resultType;
                                private Double result;
                                private Double outOf;
                                private String gradeOrDivision;
                
                                private LocalDate startDate;
                                private LocalDate endDate;
                
                                private Boolean currentlyStudying;
                
                
                                private LocalDateTime createdAt;
                
                                private Long userProfileId;
                                private Long userId;
                                private String userName;
                                private String userEmail;
                
                
                            }
                
                            public class ExperienceResponseDTO {
                                private Long id;
                
                                private String companyName;
                                private String position;
                                private String responsibilities;
                                private String achievements;
                
                                private LocalDate startDate;
                                private LocalDate endDate;
                
                                private EmploymentType employmentType;
                
                                private Boolean currentlyWorking;
                
                                private LocalDateTime createdAt;
                                private LocalDateTime updatedAt;
                
                
                                private Long userProfileId;
                                private Long userId;
                
                                private String userName;
                                private String userEmail;
                
                
                            }
                
                            public class UserSkillResponseDTO {
                
                                private Long id;
                
                                private ProficiencyLevel proficiencyLevel;
                                private Integer yearsOfExperience;
                                private LocalDateTime createdAt;
                
                
                                private Long userProfileId;
                                private String userFullName;
                                private String userHeadline;
                
                                private Long userId;
                                private String userEmail;
                
                                private Long skillId;
                
                                private String skillName;
                
                                private Long categoryId;
                
                                private String categoryName;
                
                                private String categoryDescription;
                
                
                            }
                
                            public class UserLanguageResponseDTO {
                
                                private Long id;
                                private LanguageProficiency proficiency;
                
                                private Long languageId;
                                private String languageName;
                
                                private Long userProfileId;
                                private String userName;
                                private String userEmail;
                
                
                            }
                
                            public class TrainingResponseDTO {
                
                                private Long id;
                
                                private String name;
                                private String description;
                
                                private String institution;
                
                                private LocalDate startDate;
                                private LocalDate endDate;
                                private Boolean completed;
                                private String duration;
                
                                private String certificateFile;
                                private String certificateVerificationUrl;
                                private String certificateId;
                
                                private TrainingType trainingType;
                
                                private LocalDateTime createdAt;
                                private LocalDateTime updatedAt;
                
                                private Long userProfileId;
                                private String userName;
                
                                private Long userId;
                                private String userEmail;
                
                            }
                
                            public class PortfolioResponseDTO {
                
                                private Long id;
                
                                private String title;
                                private String description;
                
                                private String projectUrl;
                
                
                                private String fileUrl;
                
                                private String technologies;
                
                
                                private LocalDateTime createdAt;
                
                
                                private LocalDateTime updatedAt;
                
                                private Long userProfileId;
                                private String userName;
                
                                private Long userId;
                                private String userEmail;
                
                
                            }
                
                
                            public class ReferenceResponseDTO {
                                private Long id;
                                private String name;
                
                                private String organization;
                                private String designation;
                
                                private String phone;
                                private String email;
                                private String address;
                
                
                                private String relation;
                
                                private Long userProfileId;
                                private String userName;
                
                                private Long userId;
                                private String userEmail;
                
                            }
                
                            public class ExtracurricularResponseDTO {
                
                                private Long id;
                
                                private String title;
                                private String description;
                
                                private String organization;
                                private String role;
                
                                private Long userProfileId;
                                private Long userId;
                                private String userName;
                                private String userEmail;
                
                
                            }
                
                """;
    }


    private String getEnumDefinitions() {

        return
                enumToPrompt(GenderType.class) +
                        enumToPrompt(JobType.class) +
                        enumToPrompt(ProficiencyLevel.class) +
                        enumToPrompt(TrainingType.class) +
                        enumToPrompt(EducationLevel.class) +
                        enumToPrompt(ResultType.class) +
                        enumToPrompt(EmploymentType.class) +
                        enumToPrompt(WorkPlaceType.class) +
                        enumToPrompt(LanguageProficiency.class);

    }


    private String enumToPrompt(Class<? extends Enum<?>> clazz) {
        StringBuilder sb = new StringBuilder();

        sb.append(clazz.getSimpleName()).append("\n\n");

        for (Enum<?> value : clazz.getEnumConstants()) {
            sb.append(value.name()).append("\n");
        }

        sb.append("\n");

        return sb.toString();
    }
}
