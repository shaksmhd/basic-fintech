package com.shaka.finte.util;

import com.shaka.finte.model.Account;
import com.shaka.finte.model.CustomerType;
import com.shaka.finte.model.TransactionType;
import com.shaka.finte.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class DiscountCalculator {


    private final TransactionRepository transactionRepository;

    public BigDecimal calculateDiscount(Account account, BigDecimal amount, TransactionType transactionType) {
        // No discount for airtime transactions
        if (transactionType == TransactionType.AIRTIME) {
            return BigDecimal.ZERO;
        }

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);

        long transactionCount = transactionRepository.countTransactionsByAccountAndTypeInDateRange(
                account.getAccountNumber(),
                transactionType,
                startOfMonth,
                endOfMonth
        );

        BigDecimal discount = BigDecimal.ZERO;

        // Rule 1 & 2: Business/Retail user discounts after 3 transactions
        if (transactionCount >= 3) {
            if (account.getCustomer().getType() == CustomerType.BUSINESS &&
                    amount.compareTo(BigDecimal.valueOf(150000)) > 0) {
                discount = discount.max(BigDecimal.valueOf(27));
            } else if (account.getCustomer().getType() == CustomerType.RETAIL &&
                    amount.compareTo(BigDecimal.valueOf(50000)) > 0) {
                discount = discount.max(BigDecimal.valueOf(18));
            }
        }

        // Rule 3: Loyalty discount for customers over 4 years
        long yearsAsCustomer = ChronoUnit.YEARS.between(
                account.getCustomer().getRegistrationDate(),
                LocalDate.now()
        );

        if (yearsAsCustomer > 4) {
            var customerTransactionsThisMonth = transactionRepository.findCustomerTransactionsInMonth(
                    account.getCustomer().getId(),
                    transactionType,
                    startOfMonth,
                    endOfMonth
            );

            // Check if this is among the first 3 transactions for the customer this month
            if (customerTransactionsThisMonth.size() < 3) {
                discount = discount.max(BigDecimal.valueOf(10));
            }
        }

        return discount;
    }

    public BigDecimal applyDiscount(BigDecimal amount, BigDecimal discountPercentage) {
        if (discountPercentage.compareTo(BigDecimal.ZERO) == 0) {
            return amount;
        }

        BigDecimal discountAmount = amount.multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return amount.subtract(discountAmount);
    }
}
