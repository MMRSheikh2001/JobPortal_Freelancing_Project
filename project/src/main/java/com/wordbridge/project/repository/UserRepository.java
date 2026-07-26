package com.wordbridge.project.repository;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);

    Optional<User> findByRole(UserRole role);



    Long countByIsSuspendedTrue();


}
