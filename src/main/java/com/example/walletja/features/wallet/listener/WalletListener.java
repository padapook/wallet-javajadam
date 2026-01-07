package com.example.walletja.features.wallet.listener;

import com.example.walletja.features.user.config.UserRabbitConfig;
import com.example.walletja.features.wallet.service.WalletService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WalletListener {
    private final WalletService walletService;
    private final ObjectMapper objectMapper;

    // debug ว่าทำไมมันไม่เข้ามาทำ func handler
    // ปิด anno @req arg cont แล้วสร้าง construct เอง
    // public WalletListener(WalletService walletService, ObjectMapper objectMapper) {
    //     this.walletService = walletService;
    //     this.objectMapper = objectMapper;
    //     log.info("WalletListener Bean Init");
    // }

    @RabbitListener(queues = UserRabbitConfig.QUEUE_USER_REGISTER)
    public void handleUserRegistration(String message) {
        try {
            log.info("เข้า func นี้มั้ย");
            JsonNode jsonNode = objectMapper.readTree(message);

            String username = jsonNode.get("username").asText();

            walletService.createWallet(username);

        } catch (Exception e) {
            log.error("Failed to create wallet: {}", e.getMessage());
        }
    }
}
