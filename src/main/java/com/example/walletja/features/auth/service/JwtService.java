package com.example.walletja.features.auth.service;

import org.springframework.beans.factory.annotation.Value;

public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;
}
