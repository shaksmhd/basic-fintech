package com.shaka.finte.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private String reference;
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal originalAmount;
    private BigDecimal finalAmount;
    private BigDecimal discountPercentage;
    private String type;
    private LocalDateTime transactionDate;
    private String message;
}
