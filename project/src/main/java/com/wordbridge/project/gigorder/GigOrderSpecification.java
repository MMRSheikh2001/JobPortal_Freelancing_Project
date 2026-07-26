package com.wordbridge.project.gigorder;

import com.wordbridge.project.entity.CompanyProfile;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.entity.UserProfile;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GigOrderSpecification {


    public static Specification<GigOrder> filter(
            GigOrderFilterRequestDTO dto
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

                Join<GigOrder, User> buyer =
                        root.join("buyer", JoinType.LEFT);

                Join<User, UserProfile> buyerUserProfile =
                        buyer.join("userProfile", JoinType.LEFT);

                Join<User, CompanyProfile> buyerCompanyProfile =
                        buyer.join("companyProfile", JoinType.LEFT);

                predicates.add(

                        cb.or(

                                // Gig title
                                cb.like(
                                        cb.lower(
                                                root.get("gig")
                                                        .get("title")
                                        ),
                                        keyword
                                ),

                                // Buyer email
                                cb.like(
                                        cb.lower(
                                                buyer.get("email")
                                        ),
                                        keyword
                                ),

                                // Buyer user profile name
                                cb.like(
                                        cb.lower(
                                                buyerUserProfile.get("name")
                                        ),
                                        keyword
                                ),

                                // Buyer company name
                                cb.like(
                                        cb.lower(
                                                buyerCompanyProfile.get("name")
                                        ),
                                        keyword
                                ),

                                // Seller name
                                cb.like(
                                        cb.lower(
                                                root.get("gig")
                                                        .get("userProfile")
                                                        .get("name")
                                        ),
                                        keyword
                                )

                        )

                );

            }

            //--------------------------------
            // Buyer
            //--------------------------------

            if (dto.getBuyerId() != null &&
                    dto.getBuyerId() > 0) {

                predicates.add(

                        cb.equal(
                                root.get("buyer").get("id"),
                                dto.getBuyerId()
                        )

                );

            }

            //--------------------------------
            // Seller
            //--------------------------------

            if (dto.getSellerId() != null &&
                    dto.getSellerId() > 0) {

                predicates.add(

                        cb.equal(

                                root.get("gig")
                                        .get("userProfile")
                                        .get("id"),

                                dto.getSellerId()

                        )

                );

            }

            //--------------------------------
            // Gig
            //--------------------------------

            if (dto.getGigId() != null &&
                    dto.getGigId() > 0) {

                predicates.add(

                        cb.equal(
                                root.get("gig").get("id"),
                                dto.getGigId()
                        )

                );

            }

            //--------------------------------
            // Category
            //--------------------------------

            if (dto.getCategoryId() != null &&
                    dto.getCategoryId() > 0) {

                predicates.add(

                        cb.equal(

                                root.get("gig")
                                        .get("category")
                                        .get("id"),

                                dto.getCategoryId()

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
            // Payment Locked
            //--------------------------------

            if (dto.getPaymentLocked() != null) {

                predicates.add(

                        cb.equal(
                                root.get("paymentLocked"),
                                dto.getPaymentLocked()
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
