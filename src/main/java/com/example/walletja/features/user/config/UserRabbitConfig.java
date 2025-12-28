package com.example.walletja.features.user.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRabbitConfig {

    public static final String EXCHANGE = "walletja.exchange";
    
    public static final String QUEUE_USER_REGISTER = "user.registration.queue";
    public static final String KEY_USER_REGISTRATION = "user.registration.key";


    @Bean
    public DirectExchange walletExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue userRegistrationQueue() {
        return new Queue(QUEUE_USER_REGISTER, true);
    }

    @Bean
    public Binding bindingUserRegistration(Queue userRegistrationQueue, DirectExchange walletExchange) {
        return BindingBuilder
                .bind(userRegistrationQueue)
                .to(walletExchange)
                .with(KEY_USER_REGISTRATION);
    }

}
