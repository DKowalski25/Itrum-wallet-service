package org.example.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletOperationDTO(
        @NotNull UUID walletId,
        @NotNull OperationType operationType,
        @Positive @DecimalMin("0.01") BigDecimal amount
) {}
