package org.example.wallet.controller.wallet;

import jakarta.validation.Valid;
import org.example.wallet.dto.WalletBalanceDTO;
import org.example.wallet.dto.WalletOperationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

public interface WalletController {
    @PostMapping
    ResponseEntity<WalletBalanceDTO> processOperation(@RequestBody @Valid WalletOperationDTO operationDTO);

    @GetMapping("/{walletId}")
    ResponseEntity<WalletBalanceDTO> getWalletBalance(@PathVariable UUID walletId);
}
