package com.example.walletja.features.wallet.service;

// import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.walletja.features.wallet.entity.WalletEntity;
import com.example.walletja.features.wallet.entity.WalletTransactionEntity;
import com.example.walletja.features.wallet.exception.WalletException;
import com.example.walletja.features.wallet.repository.WalletRepository;
import com.example.walletja.features.wallet.repository.WalletTransactionRepository;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletTransactionRepository walletTransactionRepository;
    
    @InjectMocks
    private WalletService walletService;

    @Test
    @DisplayName("deposit success")
    void deposit_success() {
        String accid = "accid01";
        BigDecimal initBalance = new BigDecimal("0.00");
        BigDecimal depositAmount = new BigDecimal("111.00");

        WalletEntity wallet = new WalletEntity();
        wallet.setAccountId(accid);
        wallet.setBalance(initBalance);

        when(walletRepository.findByAccountId(accid)).thenReturn(Optional.of(wallet));
        
        walletService.deposit(accid, depositAmount);

        assertTrue(depositAmount.compareTo(wallet.getBalance()) == 0);
        verify(walletRepository, times(1)).save(wallet);
        verify(walletTransactionRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("deposit failed - negative amount")
    void deposit_failed_negative_amount() {
        String accid = "accid01";
        BigDecimal initBalance = new BigDecimal("0.00");
        BigDecimal negativeAmount = new BigDecimal("-100.00");
        String expectedMessage = "เงินที่ฝากต้องมากกว่า 0";

        WalletEntity wallet = new WalletEntity();

        wallet.setAccountId(accid);
        wallet.setBalance(initBalance);

        WalletException ex = assertThrows(WalletException.class, () -> {
            walletService.deposit(accid, negativeAmount);
        });

        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    @DisplayName("deposit failed - invalid account")
    void deposit_failed_invalid_acc() {
        String accid = "test_invalid";
        BigDecimal depositAmount = new BigDecimal("100.00");

        when(walletRepository.findByAccountId(accid)).thenReturn(Optional.empty());

        assertThrows(WalletException.class, () -> {
            walletService.deposit(accid, depositAmount);
        });

        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("deposit failed - null account")
    void deposit_failed_null_acc() {
        // String accid = "accid01";

        assertThrows(WalletException.class, () -> {
            walletService.deposit(null, new BigDecimal("100.00"));
        });

        assertThrows(WalletException.class, () -> {
            walletService.deposit("", new BigDecimal("100.00"));
        });

        verify(walletRepository, never()).findByAccountId(any());

    }

    @Test
    @DisplayName("deposit failed - null or empty account")
    void deposit_failed_null_or_empty_acc() {
        WalletException exNull = assertThrows(WalletException.class, () -> {
            walletService.deposit(null, new BigDecimal("100.00"));
        });
        assertEquals("เลข บช ต้องห้ามเป็น null หรือ empty string", exNull.getMessage());

        WalletException exEmpty = assertThrows(WalletException.class, () -> {
            walletService.deposit("", new BigDecimal("100.00"));
        });
        assertEquals("เลข บช ต้องห้ามเป็น null หรือ empty string", exEmpty.getMessage());

        verify(walletRepository, never()).findByAccountId(any());
    }

    @Test
    @DisplayName("withdraw success")
    void withdraw_success() {
        String accid = "accid01";
        BigDecimal balance = new BigDecimal("40.00");
        BigDecimal withdrawalAmount = new BigDecimal("20.00");

        WalletEntity wallet = new WalletEntity();
        wallet.setAccountId(accid);
        wallet.setBalance(balance);
        
        when(walletRepository.findByAccountId(accid)).thenReturn(Optional.of(wallet));
        
        walletService.withdraw(accid, withdrawalAmount);

        assertEquals(new BigDecimal("20.00"), wallet.getBalance());
        verify(walletRepository).save(wallet);
    }
    
    @Test
    @DisplayName("withdraw failed - withdraw more than balance")
    void withdraw_failed_withdraw_more_than_balance() {
        String accid = "verysleepy001";
        BigDecimal balance = new BigDecimal("99.00");
        BigDecimal withdrawalAmount = new BigDecimal("100.00");
        String expectedMsg = "ตังไม่พอ เหลืออยู่:99.00 แต่ส่งมาถอน:100.00";

        WalletEntity wallet = new WalletEntity();
        wallet.setAccountId(accid);
        wallet.setBalance(balance);

        when(walletRepository.findByAccountId(accid)).thenReturn(Optional.of(wallet));

        // walletService.withdraw(accid, withdrawalAmount);

        WalletException ex = assertThrows(WalletException.class, () -> {
            walletService.withdraw(accid, withdrawalAmount);
        });

        assertEquals(expectedMsg, ex.getMessage());
        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("withdraw faile - negative amount")
    void withdraw_failed_negative_amount() {
        WalletException ex = assertThrows(WalletException.class, () -> 
            walletService.deposit("acc01", new BigDecimal("-1.00")));
        assertEquals("เงินที่ฝากต้องมากกว่า 0", ex.getMessage());
    }

    // @Test
    // @DisplayName("withdraw_failed - invalid account")
    // void withdraw_failed_invalid_account() {
    // }

    @Test
    @DisplayName("withdraw_failed - null or empty account")
    void withdraw_failed_null_account() {
        WalletException ex = assertThrows(WalletException.class, () -> 
            walletService.deposit("", new BigDecimal("100.00")));
        assertEquals("เลข บช ต้องห้ามเป็น null หรือ empty string", ex.getMessage());
    }

    @Test
    @DisplayName("transfer success")
    void trnsfer_success() {
        String from = "sender";
        String to = "receiver";
        BigDecimal amount = new BigDecimal("200.00");

        WalletEntity fromWallet = new WalletEntity();
        fromWallet.setBalance(new BigDecimal("1000.00"));

        WalletEntity toWallet = new WalletEntity();
        toWallet.setBalance(new BigDecimal("0.00"));

        when(walletRepository.findByAccountId(from)).thenReturn(Optional.of(fromWallet));
        when(walletRepository.findByAccountId(to)).thenReturn(Optional.of(toWallet));
        when(walletTransactionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        WalletTransactionEntity mockTx = new WalletTransactionEntity();
        mockTx.setId(java.util.UUID.randomUUID()); // mock rand uuid
        when(walletTransactionRepository.save(any())).thenReturn(mockTx);

        walletService.transfer(from, to, amount, "remarkmessage: ง่วงนอนขี้เกียจdeclareตัวแปร");

        assertEquals(0, new BigDecimal("800.00").compareTo(fromWallet.getBalance()));
        assertEquals(0, new BigDecimal("200.00").compareTo(toWallet.getBalance()));
        verify(walletRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("transfer faield - sernder insufficient")
    void transfer_failed_insufficient() {
        String from = "sender";
        WalletEntity fromWallet = new WalletEntity();
        fromWallet.setBalance(new BigDecimal("10.00"));

        when(walletRepository.findByAccountId(from)).thenReturn(Optional.of(fromWallet));
        when(walletRepository.findByAccountId("receiver")).thenReturn(Optional.of(new WalletEntity()));

        WalletException ex = assertThrows(WalletException.class, () -> 
            walletService.transfer(from, "receiver", new BigDecimal("100.00"), "test"));
        
        assertEquals("เงินต้นทางไม่พอ", ex.getMessage());
    }

    @Test
    @DisplayName("transfer failed - receiver not found")
    void transfer_failed_receiver_notfound() {
        String senderId = "sender01";
        String receiverId = "ghost_acc";
        BigDecimal amount = new BigDecimal("100.00");

        WalletEntity sender = new WalletEntity();
        sender.setAccountId(senderId);
        sender.setBalance(new BigDecimal("1000.00"));

        when(walletRepository.findByAccountId(senderId)).thenReturn(Optional.of(sender));
        when(walletRepository.findByAccountId(receiverId)).thenReturn(Optional.empty());

        WalletException ex = assertThrows(WalletException.class, () -> {
            walletService.transfer(senderId, receiverId, amount, "โอนให้ ghost");
        });

        assertEquals("ไม่เจอบัญชีปลายทาง", ex.getMessage());
        
        assertEquals(0, new BigDecimal("1000.00").compareTo(sender.getBalance()));
        
        verify(walletRepository, never()).save(any());
        verify(walletTransactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("transfer failed - receiver null")
    void transfer_failed_receiver_null() {
        String senderId = "sender01";
        BigDecimal amount = new BigDecimal("100.00");

        assertThrows(Exception.class, () -> {
            walletService.transfer(senderId, null, amount, "โอนให้ null");
        });

        verify(walletRepository, never()).save(any());
    }

    @Test
    @DisplayName("transfer failed - sender ans reciever same account")
    void transfer_failed_same_acc() {
        WalletException ex = assertThrows(WalletException.class, () -> 
            walletService.transfer("acc01", "acc01", new BigDecimal("100.00"), "test"));
        
        assertEquals("ไม่ให้โอนหาตัวเอง", ex.getMessage());
    }

}
