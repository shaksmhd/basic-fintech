package com.shaka.finte.controler;

import com.shaka.finte.DTO.AirtimeRequest;
import com.shaka.finte.DTO.TransactionResponse;
import com.shaka.finte.DTO.TransferRequest;
import com.shaka.finte.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        log.info("Transfer request received: {}", request);
        try {
            TransactionResponse response = transactionService.transfer(request);
            log.info("Transfer successful: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            TransactionResponse errorResponse = new TransactionResponse();
            errorResponse.setMessage("Transfer failed: " + e.getMessage());
            log.error("Transfer failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/airtime")
    public ResponseEntity<TransactionResponse> airtime(@Valid @RequestBody AirtimeRequest request) {
        log.info("Airtime request received: {}", request);
        try {
            TransactionResponse response = transactionService.airtime(request);
            log.info("Airtime successful: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            TransactionResponse errorResponse = new TransactionResponse();
            errorResponse.setMessage("Airtime failed: " + e.getMessage());
            log.error("Airtime failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@PathVariable String accountNumber) {
        try {
            List<TransactionResponse> history = transactionService.getTransactionHistory(accountNumber);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
