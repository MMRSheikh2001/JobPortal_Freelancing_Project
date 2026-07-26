package com.wordbridge.project.wallet;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.TransactionType;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final UserRepository userRepository;
    private final TransactionService transactionService;


    @Override
    public WalletResponseDTO createWallet(Long userId) {
        if (walletRepository.existsByUserId(userId)) {
            throw new RuntimeException("A wallet already exists for this user");
        }
        Wallet wallet = new Wallet();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No User found"));
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setFrozenBalance(BigDecimal.ZERO);

        Wallet saved = walletRepository.save(wallet);

        return walletMapper.toDTO(saved);
    }

    @Override
    public WalletResponseDTO getById(Long id) {
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));
        return walletMapper.toDTO(wallet);
    }

    @Override
    public WalletResponseDTO getByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));
        return walletMapper.toDTO(wallet);
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));
        return wallet.getBalance();
    }

    @Override
    public BigDecimal getFrozenBalance(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));
        return wallet.getFrozenBalance();
    }

    @Override
    @Transactional
    public WalletResponseDTO deposit(Long userId, BigDecimal amount) {
        validateAmount(amount);
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));
        wallet.setBalance(wallet.getBalance().add(amount));
        Wallet saved = walletRepository.save(wallet);

        transactionService.createTransaction(TransactionType.DEPOSIT, null, saved.getUser(), amount, "Wallet deposit via SSLCommerz ");

        return walletMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public WalletResponseDTO withdraw(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));

        validateAmount(amount);

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));

        Wallet saved = walletRepository.save(wallet);

        transactionService.createTransaction(TransactionType.WITHDRAW, saved.getUser(), null, amount, "Wallet Withdraw");

        return walletMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void freezeAmount(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));

        validateAmount(amount);

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance to freeze");
        }

        wallet.setFrozenBalance(wallet.getFrozenBalance().add(amount));
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        transactionService.createTransaction(TransactionType.FREEZE, wallet.getUser(), wallet.getUser(), amount, "Amount Frozen");

    }

    @Override
    @Transactional
    public void unfreezeAmount(Long userId, BigDecimal amount) {
        validateAmount(amount);
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));

        wallet.setFrozenBalance(wallet.getFrozenBalance().subtract(amount));
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        transactionService.createTransaction(TransactionType.REFUND, wallet.getUser(), wallet.getUser(), amount, "Amount UNFrozen");


    }

    @Override
    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        validateAmount(amount);

        Wallet fromUserWallet = walletRepository.findByUserId(fromUserId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));
        Wallet toUserWallet = walletRepository.findByUserId(toUserId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (fromUserWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromUserWallet.setBalance(fromUserWallet.getBalance().subtract(amount));
        toUserWallet.setBalance(toUserWallet.getBalance().add(amount));

        walletRepository.save(fromUserWallet);
        walletRepository.save(toUserWallet);




    }

    @Override
    @Transactional
    public void releasePayment(Long buyerId, Long sellerId, BigDecimal agreedPrice, BigDecimal sellerAmount) {

        validateAmount(agreedPrice);
        validateAmount(sellerAmount);
        Wallet buyerWallet = walletRepository.findByUserId(buyerId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));
        Wallet sellerWallet = walletRepository.findByUserId(sellerId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));
        if (buyerWallet.getFrozenBalance().compareTo(agreedPrice) < 0) {
            throw new RuntimeException("Insufficient frozen balance");
        }

        buyerWallet.setFrozenBalance(buyerWallet.getFrozenBalance().subtract(agreedPrice));
        sellerWallet.setBalance(sellerWallet.getBalance().add(sellerAmount));

        //Admin Commission
        User admin = userRepository.findByRole(UserRole.ADMIN)
                .orElseThrow(() -> new RuntimeException("No admin found"));
        Wallet adminWallet = walletRepository.findByUserId(admin.getId())
                .orElseThrow(() -> new RuntimeException("No Wallet found"));

        BigDecimal adminAmount = agreedPrice.subtract(sellerAmount);
        validateAmount(adminAmount);
        adminWallet.setBalance(adminWallet.getBalance().add(adminAmount));

        walletRepository.save(adminWallet);

        walletRepository.save(buyerWallet);
        walletRepository.save(sellerWallet);


    }

    @Override
    @Transactional
    public void refundBuyer(Long buyerId, BigDecimal amount) {
        validateAmount(amount);
        Wallet wallet = walletRepository.findByUserId(buyerId)
                .orElseThrow(() -> new RuntimeException("No Wallet found"));
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setFrozenBalance(wallet.getFrozenBalance().subtract(amount));
        walletRepository.save(wallet);


    }

    @Override
    public BigDecimal getTotalPlatformMoney() {
        Wallet adminWallet=walletRepository.findByUserRole(UserRole.ADMIN)
                .orElseThrow(()->new RuntimeException("No Admin Wallet found"));


        return adminWallet.getBalance();
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new RuntimeException("Amount is required.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero.");
        }
    }
}
