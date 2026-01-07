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
}
