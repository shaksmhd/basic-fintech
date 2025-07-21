package com.shaka.finte.DTO;

import com.shaka.finte.model.CustomerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Customer type is required")
    private CustomerType type;

    private BigDecimal initialBalance = BigDecimal.ZERO;
}
