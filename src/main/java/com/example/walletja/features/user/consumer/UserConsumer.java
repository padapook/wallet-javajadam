package com.example.walletja.features.user.consumer;

import com.example.walletja.features.user.config.UserRabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class UserConsumer {

    /**
     * @param message
     */
    @RabbitListener(queues = UserRabbitConfig.QUEUE_USER_REGISTER)
    public void handleUserRegistration(String message) {
        System.out.println("Handle Queue Success: " + message);
    }
}
