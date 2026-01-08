package com.example.walletja.features.wallet.service;

import com.example.walletja.features.wallet.entity.WalletEntity;
import com.example.walletja.features.wallet.repository.WalletRepositoty;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepositoty walletRepository;

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

        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal updatedBalance = currentBalance.add(amount);

        wallet.setBalance(updatedBalance);

        walletRepository.save(wallet);

        log.info("accid:{} ,old:{}, new:{}", accountId, currentBalance, updatedBalance);
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

        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal updatedBalance = currentBalance.subtract(amount);

        wallet.setBalance(updatedBalance);
        walletRepository.save(wallet);

        log.info("ถอน Success เดิม:{} เหลือ:{}", currentBalance, updatedBalance);

    }
}
