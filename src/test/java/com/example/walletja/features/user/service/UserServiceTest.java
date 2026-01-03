package com.example.walletja.features.user.service;

import com.example.walletja.common.util.PasswordUtil;
import com.example.walletja.features.user.config.UserRabbitConfig;
import com.example.walletja.features.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // private final UserRepository userRepository;
    // private final PasswordUtil passwordUtil;
    // private final RabbitTemplate rabbitTemplate;
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordUtil passwordUtil;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Send Msg To MQ")
    public void ShouldSendMsgToRabbitMQ() {
        String testMessage = "Unit Test Inject Message";

        userService.registerUser(testMessage);

        verify(rabbitTemplate).convertAndSend(
            eq(UserRabbitConfig.EXCHANGE),
            eq(UserRabbitConfig.KEY_USER_REGISTRATION),
            eq(testMessage)
        );
    }
}
