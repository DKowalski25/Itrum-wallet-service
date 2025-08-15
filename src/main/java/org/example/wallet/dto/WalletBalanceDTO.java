package org.example.wallet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record WalletBalanceDTO(
        UUID walletId,
        BigDecimal balance,
        LocalDateTime lastUpdated
) {}
