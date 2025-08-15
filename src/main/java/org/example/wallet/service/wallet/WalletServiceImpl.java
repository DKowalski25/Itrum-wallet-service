package org.example.wallet.service.wallet;

import lombok.RequiredArgsConstructor;
import org.example.wallet.dto.WalletBalanceDTO;
import org.example.wallet.dto.WalletOperationDTO;
import org.example.wallet.exception.InsufficientFundsException;
import org.example.wallet.exception.WalletNotFoundException;
import org.example.wallet.mapper.WalletMapper;
import org.example.wallet.model.Wallet;
import org.example.wallet.repository.wallet.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Override
    @Transactional
    @Retryable(maxAttempts = 3)
    public WalletBalanceDTO processOperation(WalletOperationDTO operationDTO) {
        Wallet wallet = walletRepository.findWithLockById(operationDTO.walletId())
                .orElseGet(() -> walletRepository.save(
                        walletMapper.toEntity(operationDTO)
                ));

        updateWalletBalance(wallet, operationDTO);
        walletRepository.save(wallet);

        return walletMapper.toWalletBalanceDTO(wallet);

    }

    @Override
    @Transactional(readOnly = true)
    public WalletBalanceDTO getWalletBalance(UUID walletId) {
        Wallet wallet = walletRepository.findWithLockById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        return walletMapper.toWalletBalanceDTO(wallet);
    }

    private void updateWalletBalance(Wallet wallet, WalletOperationDTO operationDTO) {
        BigDecimal balance = operationDTO.amount();
        switch (operationDTO.operationType()) {
            case DEPOSIT -> wallet.setBalance(wallet.getBalance().add(balance));
            case WITHDRAW -> {
                if (wallet.getBalance().compareTo(balance) < 0) {
                    throw new InsufficientFundsException(wallet.getId());
                }
                wallet.setBalance(wallet.getBalance().subtract(balance));
            }
        }
    }
}
