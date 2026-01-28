package com.example.walletja.features.wallet.controller;

import com.example.walletja.common.dto.response.DetailedResponse;
import com.example.walletja.common.dto.response.SimpleResponse;
import com.example.walletja.common.dto.response.TransferResponse;
import com.example.walletja.features.wallet.dto.DepositRequest;
import com.example.walletja.features.wallet.dto.TransactionHistory;
import com.example.walletja.features.wallet.dto.TransferRequest;
import com.example.walletja.features.wallet.dto.WithdrawRequest;
import com.example.walletja.features.wallet.entity.WalletEntity;
import com.example.walletja.features.wallet.entity.WalletTransactionEntity;
import com.example.walletja.features.wallet.service.WalletService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.repository.support.Repositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

        private final WalletService walletService;

        //เขียนแบบรับ payload ตรงๆ ยังไม่ได้ผ่าน token
        @PostMapping("/deposit")
        public ResponseEntity<SimpleResponse> deposit(@RequestBody DepositRequest request) {
            walletService.deposit(request.getAccountId(), request.getAmount());

            return ResponseEntity.ok(new SimpleResponse(200, "Deposit Success"));
        }

        @PostMapping("/withdraw")
        public ResponseEntity<SimpleResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
            walletService.withdraw(request.getAccountId(), request.getAmount());

            return ResponseEntity.ok(new SimpleResponse(200, "Withdraw Success"));
        }

        @GetMapping("/tx/history/{accountId}")
        public ResponseEntity<DetailedResponse<List<TransactionHistory>>> getTransactionHistory(@PathVariable String accountId) {
            List<TransactionHistory> txhistory = walletService.getTransactionHistory(accountId);

            DetailedResponse<List<TransactionHistory>> response = new DetailedResponse<>(
                200,
                "Success",
                txhistory
            );

            return ResponseEntity.ok(response);
        }

        @PostMapping("/transfer")
        public ResponseEntity<DetailedResponse<TransferResponse>> transfer(@RequestBody TransferRequest request) {
            TransferResponse receipt = walletService.transfer(
                request.getFromAccountId(),
                request.getToAccountId(), 
                request.getAmount(),
                request.getRemark()
            );

            // DetailedResponse<String> response = new DetailedResponse<String>(
            //     200,
            //     "Transfer Success",
            //     "Success"
            // );

            // return ResponseEntity.ok(response);

            return ResponseEntity.ok(new DetailedResponse<>(200, "Transfer Success", receipt));
        }
}
