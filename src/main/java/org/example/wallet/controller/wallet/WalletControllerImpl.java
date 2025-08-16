package org.example.wallet.controller.wallet;

import lombok.RequiredArgsConstructor;
import org.example.wallet.dto.WalletBalanceDTO;
import org.example.wallet.dto.WalletOperationDTO;
import org.example.wallet.service.wallet.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletControllerImpl implements WalletController {

    private final WalletService walletService;

    @Override
    public ResponseEntity<WalletBalanceDTO> processOperation(WalletOperationDTO operationDTO) {
        return ResponseEntity.ok(walletService.processOperation(operationDTO));
    }

    @Override
    public ResponseEntity<WalletBalanceDTO> getWalletBalance(UUID walletId) {
        return ResponseEntity.ok(walletService.getWalletBalance(walletId));
    }
}
