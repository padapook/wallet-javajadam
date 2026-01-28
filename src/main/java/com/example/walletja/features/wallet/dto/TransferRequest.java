package com.example.walletja.features.wallet.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter @Setter
public class TransferRequest {
    @NotBlank(message = "ต้องระบุ accid ต้นทาง")
    private String fromAccountId;

    @NotBlank(message = "ต้องระบุ accid ปลายทาง")
    private String toAccountId;

    @NotNull(message = "ระบุจำนวนด้วย")
    @DecimalMin(value = "1.00", message = "ขั้นต่ำ 1")
    @DecimalMax(value = "9999.00", message = "ไม่เกิน 9999 ต่อ tx")
    @Digits(integer=10, fraction=2)
    private BigDecimal amount;

    private String remark;
}