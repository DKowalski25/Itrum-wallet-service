package org.example.wallet.util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    public static void loadEnv() {
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> {
            System.getProperty(entry.getKey(), entry.getValue());
            System.out.println("Loaded env: " + entry.getKey() + "=" + entry.getValue());
        });
    }
}
