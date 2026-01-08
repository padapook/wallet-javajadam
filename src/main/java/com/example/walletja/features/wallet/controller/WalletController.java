package com.example.walletja.features.wallet.controller;

import com.example.walletja.features.wallet.dto.DepositRequest;
import com.example.walletja.features.wallet.dto.WithdrawRequest;
import com.example.walletja.features.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

        private final WalletService walletService;

        //เขียนแบบรับ payload ตรงๆ ยังไม่ได้ผ่าน token
        @PostMapping("/deposit")
        public ResponseEntity<String> deposit(@RequestBody DepositRequest request) {
            walletService.deposit(request.getAccountId(), request.getAmount());

            return ResponseEntity.ok("Deposit Success");
        }

        @PostMapping("/withdraw")
        public ResponseEntity<String> withdraw(@RequestBody WithdrawRequest request) {
            walletService.withdraw(request.getAccountId(), request.getAmount());

            return ResponseEntity.ok("withdraw Success");
        }
}
