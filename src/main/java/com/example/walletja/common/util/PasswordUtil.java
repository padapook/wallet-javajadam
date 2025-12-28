package com.example.walletja.common.util;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
        private final Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(16, 32, 1, (1024*64), 3);

        public String hashPassword(String rawPassword) {
            return encoder.encode(rawPassword);
        }

        public Boolean verifyPassword(String rawPassword, String encodedPassword) {
            return encoder.matches(rawPassword, encodedPassword);
        }
}
