package com.shaka.finte.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirtimeRequest {
    @NotBlank(message = "Source account is required")
    private String sourceAccount;

    @NotBlank(message = "Network provider is required")
    private String networkProvider;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "100.0", message = "Minimum amount is N100.0 Naira")
    private BigDecimal amount;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

}
