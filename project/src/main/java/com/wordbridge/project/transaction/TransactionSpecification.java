package com.wordbridge.project.transaction;

import com.wordbridge.project.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecification {


    public static Specification<Transaction> filter(
            TransactionFilterDTO filter
    ) {

        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (filter == null) {

                query.orderBy(
                        cb.desc(root.get("createdAt"))
                );

                return predicate;
            }

            // ==========================
            // Transaction Type
            // ==========================

            if (filter.getTransactionType() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("type"),
                                filter.getTransactionType()
                        )
                );

            }

            // ==========================
            // User Role
            // ==========================

            if (filter.getUserRole() != null) {

                Join<Transaction, User> fromUser =
                        root.join("fromUser", JoinType.LEFT);

                Join<Transaction, User> toUser =
                        root.join("toUser", JoinType.LEFT);

                predicate = cb.and(
                        predicate,
                        cb.or(
                                cb.equal(
                                        fromUser.get("role"),
                                        filter.getUserRole()
                                ),
                                cb.equal(
                                        toUser.get("role"),
                                        filter.getUserRole()
                                )
                        )
                );

            }

            // ==========================
            // User Id
            // ==========================

            if (filter.getUserId() != null &&
                    filter.getUserId() > 0) {

                predicate = cb.and(
                        predicate,
                        cb.or(
                                cb.equal(
                                        root.get("fromUser").get("id"),
                                        filter.getUserId()
                                ),
                                cb.equal(
                                        root.get("toUser").get("id"),
                                        filter.getUserId()
                                )
                        )
                );

            }

            // ==========================
            // Keyword
            // ==========================

            if (filter.getKeyword() != null &&
                    !filter.getKeyword().isBlank()) {

                String keyword =
                        "%" + filter.getKeyword().trim().toLowerCase() + "%";

                Join<Transaction, User> fromUser =
                        root.join("fromUser", JoinType.LEFT);

                Join<Transaction, User> toUser =
                        root.join("toUser", JoinType.LEFT);

                predicate = cb.and(
                        predicate,
                        cb.or(
                                cb.like(
                                        cb.lower(root.get("description")),
                                        keyword
                                ),
                                cb.like(
                                        cb.lower(fromUser.get("email")),
                                        keyword
                                ),
                                cb.like(
                                        cb.lower(toUser.get("email")),
                                        keyword
                                )
                        )
                );

            }

            // ==========================
            // From Date
            // ==========================

            if (filter.getFromDate() != null) {

                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getFromDate().atStartOfDay()
                        )
                );

            }

            // ==========================
            // To Date
            // ==========================

            if (filter.getToDate() != null) {

                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getToDate().atTime(23, 59, 59)
                        )
                );

            }

            query.orderBy(
                    cb.desc(root.get("createdAt"))
            );

            return predicate;

        };

    }
}
