package com.shaka.finte.service.impl;

import com.shaka.finte.DTO.AirtimeRequest;
import com.shaka.finte.DTO.TransactionResponse;
import com.shaka.finte.DTO.TransferRequest;
import com.shaka.finte.model.Account;
import com.shaka.finte.model.Transaction;
import com.shaka.finte.model.TransactionType;
import com.shaka.finte.repository.AccountRepository;
import com.shaka.finte.repository.TransactionRepository;
import com.shaka.finte.service.TransactionService;
import com.shaka.finte.util.DiscountCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final DiscountCalculator discountCalculator;


    @Override
    public TransactionResponse transfer(TransferRequest request) {
        log.info("Transfer request received: {}", request);
        Account sourceAccount = accountRepository.findByAccountNumber(request.getSourceAccount())
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        //Calculate discount if applicable
        BigDecimal discountedPercentage = discountCalculator.calculateDiscount(
                sourceAccount, request.getAmount(), TransactionType.TRANSFER
        );

        BigDecimal finalAmount = discountCalculator.applyDiscount(request.getAmount(), discountedPercentage);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(request.getDestinationAccount());
        transaction.setAmount(finalAmount);
        transaction.setOriginalAmount(request.getAmount());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setDiscountPercentage(discountedPercentage);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setReference("TRX-" + generateReference());

        // Save transaction
        transactionRepository.save(transaction);
        log.info("Transaction saved with reference: {}", transaction.getReference());

        // Create response
        TransactionResponse response = new TransactionResponse();
        response.setReference(transaction.getReference());
        response.setSourceAccount(request.getSourceAccount());
        response.setDestinationAccount(request.getDestinationAccount());
        response.setOriginalAmount(request.getAmount());
        response.setFinalAmount(finalAmount);
        response.setDiscountPercentage(discountedPercentage);
        response.setType("TRANSFER");
        response.setTransactionDate(transaction.getTransactionDate());
        response.setMessage("Transfer completed successfully");

        log.info("Transfer response created: {}", response);
        return response;
    }

    @Override
    public TransactionResponse airtime(AirtimeRequest request) {
        log.info("Airtime purchase request received: {}", request);
        Account sourceAccount = accountRepository.findByAccountNumber(request.getSourceAccount())
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        // No discount for airtime
        BigDecimal discountPercentage = BigDecimal.ZERO;
        BigDecimal finalAmount = request.getAmount();

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setOriginalAmount(request.getAmount());
        transaction.setAmount(finalAmount);
        transaction.setDiscountPercentage(discountPercentage);
        transaction.setType(TransactionType.AIRTIME);
        transaction.setNetworkProvider(request.getNetworkProvider());
        transaction.setPhoneNumber(request.getPhoneNumber());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setReference("AIR" +generateReference());

        transactionRepository.save(transaction);
        log.info("Airtime transaction saved with reference: {}", transaction.getReference());

        // Create response
        TransactionResponse response = new TransactionResponse();
        response.setReference(transaction.getReference());
        response.setSourceAccount(request.getSourceAccount());
        response.setDestinationAccount(request.getPhoneNumber());
        response.setOriginalAmount(request.getAmount());
        response.setFinalAmount(finalAmount);
        response.setDiscountPercentage(discountPercentage);
        response.setType("AIRTIME");
        response.setTransactionDate(transaction.getTransactionDate());
        response.setMessage("Airtime purchase completed successfully");

        log.info("Airtime purchase response created: {}", response);
        return response;
    }

    @Override
    public List<TransactionResponse> getTransactionHistory(String accountNumber) {
        List<Transaction> transactions = transactionRepository
                .findBySourceAccountAccountNumberOrderByTransactionDateDesc(accountNumber);

        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setReference(transaction.getReference());
        response.setSourceAccount(transaction.getSourceAccount().getAccountNumber());
        response.setDestinationAccount(
                transaction.getType() == TransactionType.AIRTIME ?
                        transaction.getPhoneNumber() : transaction.getDestinationAccount()
        );
        response.setOriginalAmount(transaction.getOriginalAmount());
        response.setFinalAmount(transaction.getAmount());
        response.setDiscountPercentage(transaction.getDiscountPercentage());
        response.setType(transaction.getType().toString());
        response.setTransactionDate(transaction.getTransactionDate());
        return response;
    }

    private String generateReference() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}
