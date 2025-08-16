package org.example.wallet;

import org.example.wallet.util.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletApplication {
    public static void main(String[] args) {
        if (System.getenv("IS_DOCKER") == null) {
            EnvLoader.loadEnv();
        }
        SpringApplication.run(WalletApplication.class, args);
    }
}