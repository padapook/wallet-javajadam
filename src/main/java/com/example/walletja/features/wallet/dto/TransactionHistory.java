package com.example.walletja.features.wallet.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class TransactionHistory {
    private String displayTitle;
    private BigDecimal amount;
    private String status;
    private String datetime;
    private String remark;
}
