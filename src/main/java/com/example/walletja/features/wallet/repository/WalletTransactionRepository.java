package com.example.walletja.features.wallet.repository;

import com.example.walletja.features.wallet.entity.WalletTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransactionEntity, UUID> {
    List<WalletTransactionEntity> findByAccountIdOrderByCreatedDateDesc(String accountId);
}
