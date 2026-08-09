package com.wordbridge.project.gig;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class GigSpecification {


    public static Specification<Gig> filter(

            String keyword,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer maxDeliveryDays,
            Boolean active,
            Integer minimumRating,
            Integer minimumOrders

    ) {

        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            // ==========================
            // Keyword
            // ==========================

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
                                        cb.lower(root.get("shortDescription")),
                                        value
                                )

                        )
                );

            }

            // ==========================
            // Category
            // ==========================

            if (categoryId != null && categoryId > 0) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("category").get("id"),
                                categoryId
                        )
                );

            }

            // ==========================
            // Minimum Price
            // ==========================

            if (minPrice != null &&
                    minPrice.compareTo(BigDecimal.ZERO) > 0) {

                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("startingPrice"),
                                minPrice
                        )
                );

            }

            // ==========================
            // Maximum Price
            // ==========================

            if (maxPrice != null &&
                    maxPrice.compareTo(BigDecimal.ZERO) > 0) {

                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(
                                root.get("startingPrice"),
                                maxPrice
                        )
                );

            }

            // ==========================
            // Delivery Days
            // ==========================

            if (maxDeliveryDays != null &&
                    maxDeliveryDays > 0) {

                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(
                                root.get("deliveryDays"),
                                maxDeliveryDays
                        )
                );

            }

            // ==========================
            // Active
            // ==========================

            if (active != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("isActive"),
                                active
                        )
                );

            }

            // ==========================
            // Minimum Rating
            // ==========================

            if (minimumRating != null &&
                    minimumRating >= 0) {

                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("averageRating"),
                                minimumRating.doubleValue()
                        )
                );

            }

            // ==========================
            // Minimum Completed Orders
            // ==========================

            if (minimumOrders != null &&
                    minimumOrders >= 0) {

                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("completedOrders"),
                                minimumOrders
                        )
                );

            }

            return predicate;

        };

    }


}
