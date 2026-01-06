package com.example.walletja.features.user.service;

import com.example.walletja.common.util.PasswordUtil;
import com.example.walletja.features.user.config.UserRabbitConfig;
import com.example.walletja.features.user.entity.UserEntity;
import com.example.walletja.features.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.ArgumentCaptor;

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
    @DisplayName("Test Case 1 : ลองปิง Msg To MQ")
    public void ShouldSendMsgToRabbitMQ() {
        // เพิ่ม arrange
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        UserEntity user = new UserEntity();
        user.setUsername("plukpingMQ");

        // Act
        userService.sendUserRegistrationMessage(user);

        // Assert
        verify(rabbitTemplate).convertAndSend(
            eq(UserRabbitConfig.EXCHANGE),
            eq(UserRabbitConfig.KEY_USER_REGISTRATION),
            // any(UserEntity.class)
            userCaptor.capture()
        );

        // Inspect data ที่ส่งมา
        UserEntity capturedUser = userCaptor.getValue();
        assertEquals("plukpingMQ", capturedUser.getUsername());
    }

    @Test
    @DisplayName("Test Case 2 : ควร Insert Register Data เข้า Database และ Hash PW แล้ว")
    public void ShouldInsertRegisterDataToDB() {
        UserEntity inputUser = new UserEntity();
        
        inputUser.setUsername("plukusername");
        inputUser.setPassword("123456!");

        when(passwordUtil.hashPassword("123456!")).thenReturn("hashed-password");
        
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity result = userService.registerUser(inputUser);

        assertEquals("plukusername", result.getUsername());
        assertEquals("hashed-password", result.getPassword());
        assertNotNull(result.getAccountId());
        
        verify(userRepository, times(1)).save(any(UserEntity.class));

    }

    @Test
    @DisplayName("Test Case 3 : ควรโยน Duplicate User ออกมาถ้าหากว่า Username already exists")
    public void ShouldReturnDupUsernameWhenUsernameAlreadyExists() {

        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("plukusername");

        when(userRepository.findUserByUsername("plukusername"))
            .thenReturn(java.util.Optional.of(mockUser));

        com.example.walletja.common.exception.UserAlreadyExistsException exception = 
            org.junit.jupiter.api.Assertions.assertThrows(
                com.example.walletja.common.exception.UserAlreadyExistsException.class, 
                () -> userService.registerUser(mockUser)
            );

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Test CAse 4: Username ต้องเป็น string number เท่านั้น")
    public void ShouldSuccessWhenUsernameIsAlphanumeric() {
        UserEntity user = new UserEntity();
        user.setUsername("test4nosymbol");
        user.setPassword("123456!");

        when(passwordUtil.hashPassword("123456!")).thenReturn("hashed");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserEntity result = userService.registerUser(user);

        assertEquals("test4nosymbol", result.getUsername());
    }

    @Test
    @DisplayName("Test Case 5 : Username มี Symbol ต้อง Test Failed")
    public void ShouldFailedWhenUsernameHasSymbol() {
        UserEntity user = new UserEntity();
        user.setUsername("test5symbol_");
        user.setPassword("123456!");

        IllegalArgumentException exception = assertThrows (
            IllegalArgumentException.class,
            () -> userService.registerUser(user)
        );

        assertEquals("Username must be alphanumeric only", exception.getMessage());
    }
    
}
