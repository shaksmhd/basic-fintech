package com.shaka.finte.service.impl;

import com.shaka.finte.DTO.CreateUserRequest;
import com.shaka.finte.DTO.CreateUserResponse;
import com.shaka.finte.model.Account;
import com.shaka.finte.model.Customer;
import com.shaka.finte.repository.AccountRepository;
import com.shaka.finte.repository.CustomerRepository;
import com.shaka.finte.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CustomerRepository customerRepository;

    private final AccountRepository accountRepository;

    private final Random random = new Random();

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with request: {}", request);
        // Create customer
        Customer customer = new Customer();
        customer.setCustomerId(generateCustomerId());
        customer.setName(request.getName());
        customer.setType(request.getType());
        customer.setRegistrationDate(LocalDate.now());

        Customer savedCustomer = customerRepository.save(customer);

        // Create account with 10-digit account number
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(request.getInitialBalance());
        account.setCustomer(savedCustomer);

        Account savedAccount = accountRepository.save(account);

        log.info("User created successfully: {}", savedCustomer.getCustomerId());
        // Create response
        CreateUserResponse response = new CreateUserResponse();
        response.setCustomerId(savedCustomer.getCustomerId());
        response.setName(savedCustomer.getName());
        response.setType(savedCustomer.getType());
        response.setRegistrationDate(savedCustomer.getRegistrationDate());
        response.setAccountNumber(savedAccount.getAccountNumber());
        response.setInitialBalance(savedAccount.getBalance());
        response.setMessage("User and account created successfully");
        log.info("User creation response created: {}", response);

        return response;
    }

    private String generateCustomerId() {
        return "CUST" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private String generateAccountNumber() {
        StringBuilder accountNumber;

        do {
            accountNumber = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                accountNumber.append(random.nextInt(10));
            }
        } while (accountRepository.findByAccountNumber(accountNumber.toString()).isPresent());

        log.info("Generated account number: {}", accountNumber);
        return accountNumber.toString();
    }
}
