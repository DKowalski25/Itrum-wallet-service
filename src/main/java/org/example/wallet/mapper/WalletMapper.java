package org.example.wallet.mapper;

import org.example.wallet.dto.OperationType;
import org.example.wallet.dto.WalletBalanceDTO;
import org.example.wallet.dto.WalletOperationDTO;
import org.example.wallet.model.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletMapper {

    public WalletBalanceDTO toWalletBalanceDTO(Wallet wallet) {
        return new WalletBalanceDTO(
                wallet.getId(),
                wallet.getBalance(),
                wallet.getUpdatedAt()
        );
    }

    public Wallet toEntity(WalletOperationDTO dto) {
        return Wallet.builder()
                .balance(BigDecimal.ZERO)
                .build();
    }
}
