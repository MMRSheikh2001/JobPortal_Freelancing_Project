package com.wordbridge.project.job;

import com.wordbridge.project.entity.CompanyProfile;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CompanyProfileSpecification {


    public static Specification<CompanyProfile> search(
            CompanySearchRequestDTO request
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // ==========================================
            // Keyword
            // ==========================================

            if (request.getKeyword() != null &&
                    !request.getKeyword().trim().isEmpty()) {

                String keyword =
                        "%" +
                                request.getKeyword()
                                        .trim()
                                        .toLowerCase()
                                + "%";

                predicates.add(

                        cb.or(

                                cb.like(
                                        cb.lower(root.get("name")),
                                        keyword
                                ),

                                cb.like(
                                        cb.lower(root.get("companyDescription")),
                                        keyword
                                ),

                                cb.like(
                                        cb.lower(root.get("industry")),
                                        keyword
                                ),

                                cb.like(
                                        cb.lower(root.get("companyWebsite")),
                                        keyword
                                )

                        )

                );

            }

            // ==========================================
            // Industry
            // ==========================================

            if (request.getIndustry() != null &&
                    !request.getIndustry().trim().isEmpty()) {

                predicates.add(

                        cb.equal(

                                cb.lower(root.get("industry")),

                                request.getIndustry()
                                        .trim()
                                        .toLowerCase()

                        )

                );

            }

            // ==========================================
// Country
// ==========================================

            if (request.getCountryId() != null &&
                    request.getCountryId() > 0) {

                predicates.add(

                        cb.equal(

                                root.get("location")
                                        .get("policeStation")
                                        .get("district")
                                        .get("division")
                                        .get("country")
                                        .get("id"),

                                request.getCountryId()

                        )

                );

            }

// ==========================================
// Division
// ==========================================

            if (request.getDivisionId() != null &&
                    request.getDivisionId() > 0) {

                predicates.add(

                        cb.equal(

                                root.get("location")
                                        .get("policeStation")
                                        .get("district")
                                        .get("division")
                                        .get("id"),

                                request.getDivisionId()

                        )

                );

            }

// ==========================================
// District
// ==========================================

            if (request.getDistrictId() != null &&
                    request.getDistrictId() > 0) {

                predicates.add(

                        cb.equal(

                                root.get("location")
                                        .get("policeStation")
                                        .get("district")
                                        .get("id"),

                                request.getDistrictId()

                        )

                );

            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );

        };

    }

}
