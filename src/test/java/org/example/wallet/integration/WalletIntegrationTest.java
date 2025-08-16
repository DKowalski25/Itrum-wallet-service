package org.example.wallet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.wallet.dto.OperationType;
import org.example.wallet.dto.WalletOperationDTO;
import org.example.wallet.model.Wallet;
import org.example.wallet.repository.wallet.WalletRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WalletIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void cleanup() {
        walletRepository.deleteAll();
    }

    @Test
    void testCreateWalletAndDeposit() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletOperationDTO dto = new WalletOperationDTO(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(200));

        String response = mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(200))
                .andExpect(jsonPath("$.walletId").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UUID createdWalletId = UUID.fromString(objectMapper.readTree(response).get("walletId").asText());

        mockMvc.perform(get("/api/v1/wallets/{walletId}", createdWalletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(200))
                .andExpect(jsonPath("$.walletId").value(createdWalletId.toString()));
    }

    @Test
    void testWithdrawInsufficientFunds() throws Exception {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = Wallet.builder()
                .balance(BigDecimal.valueOf(50))
                .build();
        walletRepository.save(wallet);

        WalletOperationDTO dto = new WalletOperationDTO(wallet.getId(), OperationType.WITHDRAW, BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
