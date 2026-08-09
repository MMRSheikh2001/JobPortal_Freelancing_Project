package com.wordbridge.project.wallet;

import com.wordbridge.project.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserId(Long userId);

    Optional<Wallet> findByUserRole(UserRole role);

    boolean existsByUserId(Long userId);

    long countByUserRole(UserRole role);
}
