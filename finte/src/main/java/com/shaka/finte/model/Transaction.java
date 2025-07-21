package com.shaka.finte.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    private String destinationAccount;

    private BigDecimal amount;

    private BigDecimal originalAmount;

    private BigDecimal discountPercentage;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String networkProvider;

    private String phoneNumber;

    private LocalDateTime transactionDate;

    private String reference;
}
