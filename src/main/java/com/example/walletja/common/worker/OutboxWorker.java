package com.example.walletja.common.worker;

import com.example.walletja.common.entity.OutboxEntity;
import com.example.walletja.common.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j //มันคือ logger
@RequiredArgsConstructor
public class OutboxWorker {
    
    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbittemplate;

    @Scheduled(fixedDelay = 3000) // 3s
    @Transactional
    public void processOutbox() {
        List<OutboxEntity> pendingMessages = outboxRepository.findByProcessedFalse();

        if(pendingMessages.isEmpty()) {
            return;
        }

        for (OutboxEntity message : pendingMessages) {
            try {
                rabbittemplate.convertAndSend(
                    message.getExchange(),
                    message.getRoutingKey(),
                    message.getPayload()
                );

                // set process ที่ส่งไป MQ แล้วเป็น true
                message.setProcessed(true);
                outboxRepository.save(message);

            } catch (Exception e) {
                log.error("Failed to send message ID: {}. Error: {}", message.getId(), e.getMessage());
            }
        }

    }
}
