package org.example.wallet.config;

import org.example.wallet.dto.WalletBalanceDTO;
import org.example.wallet.service.wallet.WalletService;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@TestConfiguration
public class TestWalletConfig {
    @Bean
    @Primary
    public WalletService walletService() {
        WalletService mock = Mockito.mock(WalletService.class);

        UUID walletId = UUID.randomUUID();
        WalletBalanceDTO balanceDTO = new WalletBalanceDTO(walletId, BigDecimal.valueOf(100), LocalDateTime.now());

        Mockito.when(mock.processOperation(ArgumentMatchers.any()))
                .thenReturn(balanceDTO);

        Mockito.when(mock.getWalletBalance(walletId))
                .thenReturn(balanceDTO);

        return mock;
    }
}
