package com.example.walletja.features.wallet.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

@Getter @Setter
public class WithdrawRequest {
    @NotBlank(message = "AccountId ห้ามเป็น null")
    private String accountId;

    @NotBlank(message = "ห้ามใส่จำนวนเงินติดลบ")
    @DecimalMin(value = "0.01", message = "ต้องใส่จำนวนเงิน > 0")
    @Digits(integer=10, fraction=2)
    private BigDecimal amount;
}
