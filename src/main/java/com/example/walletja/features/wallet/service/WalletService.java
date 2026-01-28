package com.example.walletja.features.wallet.service;

import com.example.walletja.common.dto.response.TransferResponse;
import com.example.walletja.features.wallet.dto.TransactionHistory;
import com.example.walletja.features.wallet.constant.TransactionType;
import com.example.walletja.features.wallet.entity.WalletEntity;
import com.example.walletja.features.wallet.entity.WalletTransactionEntity;
import com.example.walletja.features.wallet.repository.WalletRepository;
import com.example.walletja.features.wallet.repository.WalletTransactionRepository;

import com.example.walletja.features.wallet.exception.WalletException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    @Transactional
    public void createWallet(String accountId) {
        log.info("accid {}", accountId);
        if (walletRepository.findByAccountId(accountId).isPresent()) {
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
        WalletEntity wallet = walletRepository.findByAccountId(accountId).orElseThrow(() -> new WalletException(
                String.format("Wallet doesnt exists for accountId: %s", accountId)));

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

        // log.info("accid:{} ,old:{}, new:{}", accountId, currentBalance,
        // updatedBalance);
    }

    @Transactional
    public void withdraw(String accountId, BigDecimal amount) {
        log.info("เข้า withdraw");

        // ไม่มี account ใน wallet
        WalletEntity wallet = walletRepository.findByAccountId(accountId).orElseThrow(() -> new WalletException(
                String.format("ไม่มี accountid: %s", accountId)));

        // ยอดเงินน้อยกว่าที่ส่งเข้ามาถอน
        if (wallet.getBalance().compareTo(amount) < 0) {
            // log.warn("ตังไม่พอ เหลืออยู่:{} แต่ส่งมาถอน:{}", wallet.getBalance(), amount);
            throw new WalletException(
                    String.format("ตังไม่พอ เหลืออยู่:%s แต่ส่งมาถอน:%s", wallet.getBalance(), amount));
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
    public List<TransactionHistory> getTransactionHistory(String accountId) {
        walletRepository.findByAccountId(accountId).orElseThrow(() -> new WalletException("ไม่เจอ accid นี้ใน wallet"));

        // ปรับจาก datetime ให้เป็น string
        DateTimeFormatter stringFormat = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.ENGLISH);

        return walletTransactionRepository.findByAccountIdOrderByCreatedDateDesc(accountId)
            .stream()
            .map(entity -> {
                TransactionHistory dto = new TransactionHistory();
                dto.setDisplayTitle(displayTitle(entity));
                dto.setAmount(entity.getAmount());
                dto.setStatus("SUCCESS");
                dto.setRemark(entity.getRemark());
                dto.setDatetime(entity.getCreatedDate() != null ? entity.getCreatedDate().format(stringFormat) : "");
                return dto;
            })
            .collect(Collectors.toList());
            
        // return walletTransactionRepository.findByAccountIdOrderByCreatedDateDesc(accountId);

    }

    private String displayTitle(WalletTransactionEntity entity) {
        return switch (entity.getType()) {
            case DEPOSIT -> "Testฝากเงิน";
            case WITHDRAW -> "ถอนเงิน";
            case TRANSFER_OUT -> "โอนเงินให้ " + entity.getTargetAccountId();
            case TRANSFER_IN -> "ได้รับเงินจาก " + entity.getTargetAccountId();
            default -> "รายการ";
        };
    }

    @Transactional
    public TransferResponse transfer(String fromAccountId, String toAccountId, BigDecimal amount, String remark) {
        log.info("testtestjaa");

        // ไม่ให้โอนหาตัวเอง
        if (fromAccountId.equals(toAccountId)) {
            throw new WalletException("ไม่ให้โอนหาตัวเอง");
        }

        // // หักเงินจาก fromAcc
        // withdraw(fromAccountId, amount);
        // // เพิ่มเงินให้ toAcc
        // deposit(toAccountId, amount);

        // เช็คว่ามีบัญชีทั้งสองฝั่ง
        WalletEntity getFromWallet = walletRepository.findByAccountId(fromAccountId)
                .orElseThrow(() -> new WalletException("ไม่เจอบัญชีต้นทาง"));
        WalletEntity getToWallet = walletRepository.findByAccountId(toAccountId)
                .orElseThrow(() -> new WalletException("ไม่เจอบัญชีปลายทาง"));

        // ดักไม่ให้โอนเงินติดลบ
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletException("โอนเงินติดลบทำไม");
        }

        // เช็คว่าต้นทางมีเงินพอ
        if (getFromWallet.getBalance().compareTo(amount) < 0) {
            throw new WalletException("เงินต้นทางไม่พอ");
        }

        // ถ้าพอก็โอน
        BigDecimal fromUpdateBalance = getFromWallet.getBalance().subtract(amount);
        BigDecimal toUpdateBalance = getToWallet.getBalance().add(amount);

        getFromWallet.setBalance(fromUpdateBalance);
        getToWallet.setBalance(toUpdateBalance);

        walletRepository.save(getFromWallet);
        walletRepository.save(getToWallet);

        // record tx ฝั่งต้นทาง
        WalletTransactionEntity txFrom = new WalletTransactionEntity();
        txFrom.setAccountId(fromAccountId);
        txFrom.setType(TransactionType.TRANSFER_OUT);
        txFrom.setAmount(amount);
        txFrom.setBalanceAfter(fromUpdateBalance);
        txFrom.setTargetAccountId(toAccountId);
        txFrom.setRemark(remark);
        // walletTransactionRepository.save(txFrom);
        WalletTransactionEntity TransId = walletTransactionRepository.save(txFrom);

        // record tx ฝั่งปลายทาง
        WalletTransactionEntity txTo = new WalletTransactionEntity();
        txTo.setAccountId(toAccountId);
        txTo.setType(TransactionType.TRANSFER_IN);
        txTo.setAmount(amount);
        txTo.setBalanceAfter(toUpdateBalance);
        txTo.setTargetAccountId(fromAccountId);
        txTo.setRemark(remark);
        walletTransactionRepository.save(txTo);

        // เอา data ออกไปทำสลิป
        TransferResponse receipt = new TransferResponse();
        receipt.setTransactionId(TransId.getId().toString());
        receipt.setFromAccountId(fromAccountId);
        receipt.setToAccountId(toAccountId);
        receipt.setAmount(amount);
        receipt.setBalanceAfter(fromUpdateBalance);
        receipt.setRemark(remark);

        return receipt;
    }
}
