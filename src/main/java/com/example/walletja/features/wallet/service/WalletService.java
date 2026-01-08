package com.example.walletja.features.wallet.service;

import com.example.walletja.features.wallet.constant.TransactionType;
import com.example.walletja.features.wallet.entity.WalletEntity;
import com.example.walletja.features.wallet.entity.WalletTransactionEntity;
import com.example.walletja.features.wallet.repository.WalletRepository;
import com.example.walletja.features.wallet.repository.WalletTransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository  walletTransactionRepository;

    @Transactional
    public void createWallet(String accountId) {
        log.info("accid {}", accountId);
        if(walletRepository.findByAccountId(accountId).isPresent()) {
            log.warn("Wallet for accountId: {} already exists", accountId);
            return;
        }

        WalletEntity wallet = new WalletEntity();
        wallet.setAccountId(accountId);
        wallet.setBalance(BigDecimal.ZERO);

        WalletEntity createdWallet = walletRepository.save(wallet);

        log.info("wallet id {}", createdWallet.getId());
        log.info("Create wallet for accountId: {} successfully", accountId);
    }

    @Transactional
    public void deposit(String accountId, BigDecimal amount) {
        log.info("เข้า deposit");
        WalletEntity wallet = walletRepository.findByAccountId(accountId).orElseThrow(() -> new RuntimeException(
            String.format("Wallet doesnt exists for accountId: %s", accountId)
        ));

        // ความหมายเหมือนกัน
        // BigDecimal currentBalance = wallet.getBalance();
        // BigDecimal updatedBalance = currentBalance.add(amount);
        BigDecimal updatedBalance = wallet.getBalance().add(amount);

        wallet.setBalance(updatedBalance);

        walletRepository.save(wallet);

        // tx
        WalletTransactionEntity tx = new WalletTransactionEntity();

        tx.setAccountId(accountId);
        tx.setAmount(amount);
        tx.setBalanceAfter(updatedBalance);
        tx.setType(TransactionType.DEPOSIT);

        walletTransactionRepository.save(tx);

        // log.info("accid:{} ,old:{}, new:{}", accountId, currentBalance, updatedBalance);
    }

    @Transactional
    public void withdraw(String accountId, BigDecimal amount) {
        log.info("เข้า withdraw");

        // ไม่มี account ใน wallet
        WalletEntity wallet = walletRepository.findByAccountId(accountId).orElseThrow(() -> new RuntimeException(
            String.format("ไม่มี accountid: %s", accountId)
        ));

        //ยอดเงินน้อยกว่าที่ส่งเข้ามาถอน
        if(wallet.getBalance().compareTo(amount) < 0) {
            log.warn("ตังไม่พอ เหลืออยู่:{} แต่ส่งมาถอน:{}", wallet.getBalance(), amount);
            throw new RuntimeException(String.format("ตังไม่พอ เหลืออยู่:%s แต่ส่งมาถอน:%s", wallet.getBalance(), amount));
        }

        // BigDecimal currentBalance = wallet.getBalance();
        // BigDecimal updatedBalance = currentBalance.subtract(amount);
        BigDecimal updatedBalance = wallet.getBalance().subtract(amount);

        // setter
        wallet.setBalance(updatedBalance);

        walletRepository.save(wallet);

        // transac history
        WalletTransactionEntity tx = new WalletTransactionEntity();
        tx.setAccountId(accountId);
        tx.setType(TransactionType.WITHDRAW);
        tx.setAmount(amount);
        tx.setBalanceAfter(updatedBalance);

        walletTransactionRepository.save(tx);

        // wallet.setBalance(updatedBalance);
        // walletRepository.save(wallet);

        // log.info("ถอน Success เดิม:{} เหลือ:{}", currentBalance, updatedBalance);
    }

    @Transactional(readOnly = true)
    public List<WalletTransactionEntity> getTransactionHistory(String accountId) {
        walletRepository.findByAccountId(accountId).orElseThrow(() -> new RuntimeException("ไม่เจอ accid นี้ใน wallet"));

        return walletTransactionRepository.findByAccountIdOrderByCreatedDateDesc(accountId);
    }
}
