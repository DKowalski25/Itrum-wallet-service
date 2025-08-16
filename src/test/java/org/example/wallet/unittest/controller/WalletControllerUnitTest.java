package org.example.wallet.unittest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.wallet.config.TestSecurityConfig;
import org.example.wallet.config.TestWalletConfig;
import org.example.wallet.controller.wallet.WalletControllerImpl;
import org.example.wallet.dto.OperationType;
import org.example.wallet.dto.WalletBalanceDTO;
import org.example.wallet.dto.WalletOperationDTO;
import org.example.wallet.service.wallet.WalletService;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletControllerImpl.class)
@Import(value = {TestSecurityConfig.class, TestWalletConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
class WalletControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletService walletService;

    @Test
    @WithMockUser
    void testProcessOperation_success() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletOperationDTO dto = new WalletOperationDTO(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").exists())
                .andExpect(jsonPath("$.balance").value(100));
    }

    @Test
    @WithMockUser
    void testGetWalletBalance_success() throws Exception {
        // 1. Подготовка тестовых данных
        UUID walletId = UUID.randomUUID();
        WalletBalanceDTO mockResponse = new WalletBalanceDTO(
                walletId,
                BigDecimal.valueOf(100),
                LocalDateTime.now()
        );

        when(walletService.getWalletBalance(walletId))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(100));
    }
}