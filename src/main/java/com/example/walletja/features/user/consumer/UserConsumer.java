package com.example.walletja.features.user.consumer;

import com.example.walletja.features.user.config.UserRabbitConfig;
// import com.example.walletja.features.user.entity.UserEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.example.walletja.features.user.dto.UserEventDto;


@Component
public class UserConsumer {

    /**
     * @param message
     */
    @RabbitListener(queues = UserRabbitConfig.QUEUE_USER_REGISTER)
    // แก้ไขจากรับ params user entity -> user event dto
    public void handleUserRegistration(UserEventDto event) {
        // ส่ง username มา
        System.out.println(" ====== Handle Queue DTO Success ======");
        // System.out.println("Username: " + user.getUsername());
    }
}
