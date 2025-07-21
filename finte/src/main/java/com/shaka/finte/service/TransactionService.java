package com.shaka.finte.service;

import com.shaka.finte.DTO.AirtimeRequest;
import com.shaka.finte.DTO.TransactionResponse;
import com.shaka.finte.DTO.TransferRequest;

import java.util.List;

public interface TransactionService {
    TransactionResponse transfer(TransferRequest request);
    TransactionResponse airtime(AirtimeRequest request);
    List<TransactionResponse> getTransactionHistory(String accountNumber);
}
