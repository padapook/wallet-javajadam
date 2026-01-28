package com.example.walletja.features.wallet.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Getter @Setter
public class DepositRequest {
    @NotBlank(message = "ต้องมี accid")
    @NotNull
    private String accountId;

    @NotNull(message = "กรุณาระบุจำนวนเงิน")
    @DecimalMin(value = "0.01", message = "ยอดเงินต้องอย่างน้อย 0.01")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal amount;
}
