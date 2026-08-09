package com.wordbridge.project.jobapplication;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.entity.UserProfile;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationSpecification {


    public static Specification<JobApplication> filter(
            JobApplicationFilterRequestDTO dto
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            //-----------------------------------------
            // Keyword
            //-----------------------------------------

            if (dto.getKeyword() != null &&
                    !dto.getKeyword().trim().isEmpty()) {

                String keyword =
                        "%" + dto.getKeyword().trim().toLowerCase() + "%";

                Join<JobApplication, UserProfile> profile =
                        root.join("userProfile");

                Join<UserProfile, User> user =
                        profile.join("user");

                predicates.add(

                        cb.or(

                                cb.like(
                                        cb.lower(root.get("job").get("title")),
                                        keyword
                                ),

                                cb.like(
                                        cb.lower(profile.get("name")),
                                        keyword
                                ),

                                cb.like(
                                        cb.lower(user.get("email")),
                                        keyword
                                )

                        )

                );

            }

            //-----------------------------------------
            // Company
            //-----------------------------------------

            if (dto.getCompanyProfileId() != null &&
                    dto.getCompanyProfileId() > 0) {

                predicates.add(

                        cb.equal(

                                root.get("job")
                                        .get("companyProfile")
                                        .get("id"),

                                dto.getCompanyProfileId()

                        )

                );

            }

            //-----------------------------------------
            // Job
            //-----------------------------------------

            if (dto.getJobId() != null &&
                    dto.getJobId() > 0) {

                predicates.add(

                        cb.equal(
                                root.get("job").get("id"),
                                dto.getJobId()
                        )

                );

            }

            //-----------------------------------------
            // User Profile
            //-----------------------------------------

            if (dto.getUserProfileId() != null &&
                    dto.getUserProfileId() > 0) {

                predicates.add(

                        cb.equal(
                                root.get("userProfile").get("id"),
                                dto.getUserProfileId()
                        )

                );

            }

            //-----------------------------------------
            // Category
            //-----------------------------------------

            if (dto.getCategoryId() != null &&
                    dto.getCategoryId() > 0) {

                predicates.add(

                        cb.equal(

                                root.get("job")
                                        .get("category")
                                        .get("id"),

                                dto.getCategoryId()

                        )

                );

            }

            //-----------------------------------------
            // Status
            //-----------------------------------------

            if (dto.getStatus() != null) {

                predicates.add(

                        cb.equal(
                                root.get("status"),
                                dto.getStatus()
                        )

                );

            }

            //-----------------------------------------
            // AI Completed
            //-----------------------------------------

            if (dto.getAiCompleted() != null) {

                predicates.add(

                        cb.equal(
                                root.get("aiInterviewCompleted"),
                                dto.getAiCompleted()
                        )

                );

            }

            //-----------------------------------------
            // AI Shortlisted
            //-----------------------------------------

            if (dto.getAiShortlisted() != null) {

                predicates.add(

                        cb.equal(
                                root.get("aiShortlisted"),
                                dto.getAiShortlisted()
                        )

                );

            }

            //-----------------------------------------
            // Applied From
            //-----------------------------------------

            if (dto.getAppliedFrom() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("appliedAt"),
                                dto.getAppliedFrom().atStartOfDay()
                        )
                );

            }

            //-----------------------------------------
            // Applied To
            //-----------------------------------------

            if (dto.getAppliedTo() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("appliedAt"),
                                dto.getAppliedTo().atTime(LocalTime.MAX)
                        )
                );

            }

            query.orderBy(
                    cb.desc(root.get("appliedAt"))
            );

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );

        };

    }


}
