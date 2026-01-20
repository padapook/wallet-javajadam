package com.example.walletja.common.dto.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransferResponse {
    private String transactionId;
    private String fromAccountId;
    private String toAccountId;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String remark;
}
