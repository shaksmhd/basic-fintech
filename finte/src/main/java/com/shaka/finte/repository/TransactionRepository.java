package com.shaka.finte.repository;

import com.shaka.finte.model.Transaction;
import com.shaka.finte.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySourceAccountAccountNumberOrderByTransactionDateDesc(String accountNumber);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.sourceAccount.accountNumber = ?1 AND t.type = ?2 AND t.transactionDate >= ?3 AND t.transactionDate <= ?4")
    long countTransactionsByAccountAndTypeInDateRange(String accountNumber, TransactionType type, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount.customer.id = ?1 AND t.type = ?2 AND t.transactionDate >= ?3 AND t.transactionDate <= ?4 ORDER BY t.transactionDate ASC")
    List<Transaction> findCustomerTransactionsInMonth(Long customerId, TransactionType type, LocalDateTime startDate, LocalDateTime endDate);
}
