package com.wordbridge.project.job;

import com.wordbridge.project.enums.EmploymentType;
import com.wordbridge.project.enums.WorkPlaceType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class JobSpecification {


    public static Specification<Job> filter(

            String keyword,
            Long categoryId,
            Long countryId,
            Long divisionId,
            Long districtId,
            Long policeStationId,
            EmploymentType employmentType,
            WorkPlaceType workPlaceType,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            Boolean active

    ) {

        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (keyword != null && !keyword.isBlank()) {

                String value = "%" + keyword.toLowerCase() + "%";

                predicate = cb.and(
                        predicate,
                        cb.or(

                                cb.like(
                                        cb.lower(root.get("title")),
                                        value
                                ),

                                cb.like(
                                        cb.lower(
                                                root.get("companyProfile")
                                                        .get("name")
                                        ),
                                        value
                                )

                        )
                );
            }

            if (categoryId != null && categoryId > 0) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("category").get("id"),
                                categoryId
                        )
                );
            }

            if (countryId != null && countryId > 0) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("jobLocation")
                                        .get("district")
                                        .get("division")
                                        .get("country")
                                        .get("id"),
                                countryId
                        )
                );
            }

            if (divisionId != null && divisionId > 0) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("jobLocation")
                                        .get("district")
                                        .get("division")
                                        .get("id"),
                                divisionId
                        )
                );
            }

            if (districtId != null && districtId > 0) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("jobLocation")
                                        .get("district")
                                        .get("id"),
                                districtId
                        )
                );
            }

            if (policeStationId != null && policeStationId > 0) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("jobLocation").get("id"),
                                policeStationId
                        )
                );
            }

            if (employmentType != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("employmentType"),
                                employmentType
                        )
                );
            }

            if (workPlaceType != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("workPlaceType"),
                                workPlaceType
                        )
                );
            }

            if (minSalary != null && minSalary.compareTo(BigDecimal.ZERO) > 0) {

                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("salaryMin"),
                                minSalary
                        )
                );
            }

            if (maxSalary != null && maxSalary.compareTo(BigDecimal.ZERO) > 0) {

                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(
                                root.get("salaryMax"),
                                maxSalary
                        )
                );
            }

            if (active != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("isActive"),
                                active
                        )
                );
            }

            return predicate;

        };

    }


}
