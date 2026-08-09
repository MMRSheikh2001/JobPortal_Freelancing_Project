package com.wordbridge.project.notification;

import org.springframework.data.jpa.domain.Specification;

public class NotificationSpecification {


    public static Specification<Notification> filter(
            NotificationFilterDTO filter
    ) {

        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (filter == null) {

                query.orderBy(
                        cb.desc(root.get("createdAt"))
                );

                return predicate;
            }

            // =====================================
            // Notification Type
            // =====================================

            if (filter.getType() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("type"),
                                filter.getType()
                        )
                );

            }

            // =====================================
            // Read / Unread
            // =====================================

            if (filter.getIsRead() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("isRead"),
                                filter.getIsRead()
                        )
                );

            }

            // =====================================
            // User
            // =====================================

            if (filter.getUserId() != null &&
                    filter.getUserId() > 0) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("receiver").get("id"),
                                filter.getUserId()
                        )
                );

            }

            // =====================================
            // Keyword
            // =====================================

            if (filter.getKeyword() != null &&
                    !filter.getKeyword().isBlank()) {

                String keyword =
                        "%" + filter.getKeyword().toLowerCase() + "%";

                predicate = cb.and(
                        predicate,

                        cb.or(

                                cb.like(
                                        cb.lower(root.get("title")),
                                        keyword
                                ),

                                cb.like(
                                        cb.lower(root.get("message")),
                                        keyword
                                )

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
