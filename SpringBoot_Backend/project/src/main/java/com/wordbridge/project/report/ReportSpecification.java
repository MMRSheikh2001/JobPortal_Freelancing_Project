package com.wordbridge.project.report;

import com.wordbridge.project.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReportSpecification {


    public static Specification<Report> filter(
            ReportFilterRequestDTO dto
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            //--------------------------------
            // Keyword
            //--------------------------------

            if (dto.getKeyword() != null &&
                    !dto.getKeyword().trim().isBlank()) {

                String keyword =
                        "%" + dto.getKeyword().trim().toLowerCase() + "%";

                Join<Report, User> user =
                        root.join("user");

                predicates.add(

                        cb.or(

                                cb.like(
                                        cb.lower(root.get("subject")),
                                        keyword
                                ),

                                cb.like(
                                        cb.lower(root.get("description")),
                                        keyword
                                ),

                                cb.like(
                                        cb.lower(user.get("email")),
                                        keyword
                                )

                        )

                );

            }

            //--------------------------------
            // User
            //--------------------------------

            if (dto.getUserId() != null &&
                    dto.getUserId() > 0) {

                predicates.add(

                        cb.equal(
                                root.get("user").get("id"),
                                dto.getUserId()
                        )

                );

            }

            //--------------------------------
            // User Role
            //--------------------------------

            if (dto.getUserRole() != null) {

                predicates.add(

                        cb.equal(
                                root.get("user").get("role"),
                                dto.getUserRole()
                        )

                );

            }

            //--------------------------------
            // Type
            //--------------------------------

            if (dto.getType() != null) {

                predicates.add(

                        cb.equal(
                                root.get("type"),
                                dto.getType()
                        )

                );

            }

            //--------------------------------
            // Status
            //--------------------------------

            if (dto.getStatus() != null) {

                predicates.add(

                        cb.equal(
                                root.get("status"),
                                dto.getStatus()
                        )

                );

            }

            //--------------------------------
            // Created From
            //--------------------------------

            if (dto.getCreatedFrom() != null) {

                predicates.add(

                        cb.greaterThanOrEqualTo(

                                root.get("createdAt"),

                                dto.getCreatedFrom().atStartOfDay()

                        )

                );

            }

            //--------------------------------
            // Created To
            //--------------------------------

            if (dto.getCreatedTo() != null) {

                predicates.add(

                        cb.lessThanOrEqualTo(

                                root.get("createdAt"),

                                dto.getCreatedTo().atTime(LocalTime.MAX)

                        )

                );

            }

            query.orderBy(
                    cb.desc(root.get("createdAt"))
            );

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );

        };

    }


}
