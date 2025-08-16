package org.example.wallet.unittest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.wallet.controller.wallet.WalletControllerImpl;
import org.example.wallet.dto.OperationType;
import org.example.wallet.dto.WalletBalanceDTO;
import org.example.wallet.dto.WalletOperationDTO;
import org.example.wallet.service.wallet.WalletService;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletControllerImpl.class)
class WalletControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletService walletService;

    @TestConfiguration
    static class WalletControllerTestConfig {
        @Bean
        public WalletService walletService() {
            return Mockito.mock(WalletService.class);
        }
    }

    @Test
    void testProcessOperation_success() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletOperationDTO dto = new WalletOperationDTO(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(100));
        WalletBalanceDTO responseDTO = new WalletBalanceDTO(walletId, BigDecimal.valueOf(100), LocalDateTime.now());

        Mockito.when(walletService.processOperation(any(WalletOperationDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(100));
    }

    @Test
    void testGetWalletBalance_success() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletBalanceDTO responseDTO = new WalletBalanceDTO(walletId, BigDecimal.valueOf(500), LocalDateTime.now());

        Mockito.when(walletService.getWalletBalance(walletId))
                .thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(500));
    }
}
