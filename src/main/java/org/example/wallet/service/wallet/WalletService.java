package org.example.wallet.service.wallet;

import org.example.wallet.dto.WalletBalanceDTO;
import org.example.wallet.dto.WalletOperationDTO;
import org.example.wallet.exception.InsufficientFundsException;
import org.example.wallet.exception.WalletNotFoundException;

import java.util.UUID;

public interface WalletService {
    WalletBalanceDTO processOperation(WalletOperationDTO operationDTO)
            throws WalletNotFoundException, InsufficientFundsException;

    WalletBalanceDTO getWalletBalance(UUID walletId) throws WalletNotFoundException;

}
