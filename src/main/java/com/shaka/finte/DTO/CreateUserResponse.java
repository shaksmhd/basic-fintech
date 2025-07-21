package com.shaka.finte.DTO;

import com.shaka.finte.model.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse {
    private String customerId;
    private String name;
    private CustomerType type;
    private LocalDate registrationDate;
    private String accountNumber;
    private BigDecimal initialBalance;
    private String message;
}
