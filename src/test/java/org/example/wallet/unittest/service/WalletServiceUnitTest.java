package org.example.wallet.unittest.service;

import org.example.wallet.dto.OperationType;
import org.example.wallet.dto.WalletBalanceDTO;
import org.example.wallet.dto.WalletOperationDTO;
import org.example.wallet.exception.InsufficientFundsException;
import org.example.wallet.exception.WalletNotFoundException;
import org.example.wallet.mapper.WalletMapper;
import org.example.wallet.model.Wallet;
import org.example.wallet.repository.wallet.WalletRepository;
import org.example.wallet.service.wallet.WalletService;
import org.example.wallet.service.wallet.WalletServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class WalletServiceUnitTest {

    private WalletRepository walletRepository;
    private WalletMapper walletMapper;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletRepository = Mockito.mock(WalletRepository.class);
        walletMapper = new WalletMapper();
        walletService = new WalletServiceImpl(walletRepository, walletMapper);
    }

    @Test
    void testDeposit_createsWalletAndAddsBalance() {
        UUID walletId = UUID.randomUUID();
        WalletOperationDTO dto = new WalletOperationDTO(walletId, OperationType.DEPOSIT, BigDecimal.valueOf(100));

        Mockito.when(walletRepository.findWithLockById(walletId))
                .thenReturn(Optional.empty());
        Mockito.when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> {
                    Wallet wallet = invocation.getArgument(0);
                    wallet.setId(UUID.randomUUID());
                    return wallet;
                });

        WalletBalanceDTO result = walletService.processOperation(dto);

        assertEquals(BigDecimal.valueOf(100), result.balance());
        assertNotNull(result.walletId());
    }

    @Test
    void testWithdraw_decreasesBalance() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(200));

        WalletOperationDTO dto = new WalletOperationDTO(walletId, OperationType.WITHDRAW, BigDecimal.valueOf(50));

        Mockito.when(walletRepository.findWithLockById(walletId))
                .thenReturn(Optional.of(wallet));
        Mockito.when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        WalletBalanceDTO result = walletService.processOperation(dto);

        assertEquals(BigDecimal.valueOf(150), result.balance());
        assertEquals(walletId, result.walletId());
    }

    @Test
    void testWithdraw_insufficientFunds_throwsException() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(30));

        WalletOperationDTO dto = new WalletOperationDTO(walletId, OperationType.WITHDRAW, BigDecimal.valueOf(50));

        Mockito.when(walletRepository.findWithLockById(walletId))
                .thenReturn(Optional.of(wallet));

        assertThrows(InsufficientFundsException.class,
                () -> walletService.processOperation(dto));
    }

    @Test
    void testGetWalletBalance_walletNotFound_throwsException() {
        UUID walletId = UUID.randomUUID();

        Mockito.when(walletRepository.findWithLockById(walletId))
                .thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class,
                () -> walletService.getWalletBalance(walletId));
    }

    @Test
    void testGetWalletBalance_success() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(500));

        Mockito.when(walletRepository.findWithLockById(walletId))
                .thenReturn(Optional.of(wallet));

        WalletBalanceDTO result = walletService.getWalletBalance(walletId);

        assertEquals(walletId, result.walletId());
        assertEquals(BigDecimal.valueOf(500), result.balance());
    }
}
