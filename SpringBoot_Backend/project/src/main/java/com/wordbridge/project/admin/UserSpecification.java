package com.wordbridge.project.admin;

import com.wordbridge.project.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {


    public static Specification<User> search(
            UserSearchRequestDTO request
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            //=========================================
            // Keyword (Email)
            //=========================================

            if (request.getKeyword() != null &&
                    !request.getKeyword().trim().isEmpty()) {

                String keyword =
                        "%" +
                                request.getKeyword()
                                        .trim()
                                        .toLowerCase()
                                + "%";

                predicates.add(

                        cb.like(

                                cb.lower(
                                        root.get("email")
                                ),

                                keyword

                        )

                );

            }

            //=========================================
            // Role
            //=========================================

            if (request.getRole() != null) {

                predicates.add(

                        cb.equal(

                                root.get("role"),

                                request.getRole()

                        )

                );

            }

            //=========================================
            // Verified
            //=========================================

            if (request.getIsVerified() != null) {

                predicates.add(

                        cb.equal(

                                root.get("isVerified"),

                                request.getIsVerified()

                        )

                );

            }

            //=========================================
            // Active
            //=========================================

            if (request.getIsActive() != null) {

                predicates.add(

                        cb.equal(

                                root.get("isActive"),

                                request.getIsActive()

                        )

                );

            }

            //=========================================
            // Suspended
            //=========================================

            if (request.getIsSuspended() != null) {

                predicates.add(

                        cb.equal(

                                root.get("isSuspended"),

                                request.getIsSuspended()

                        )

                );

            }

            return cb.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );

        };

    }

}
