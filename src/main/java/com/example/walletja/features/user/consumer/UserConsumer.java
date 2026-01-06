package com.example.walletja.features.user.consumer;

import com.example.walletja.features.user.config.UserRabbitConfig;
import com.example.walletja.features.user.entity.UserEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class UserConsumer {

    /**
     * @param message
     */
    @RabbitListener(queues = UserRabbitConfig.QUEUE_USER_REGISTER)
    public void handleUserRegistration(UserEntity user) {
        // ส่ง username มา
        System.out.println(" ====== Handle Queue Success ======");
        System.out.println("Username: " + user.getUsername());
    }
}
