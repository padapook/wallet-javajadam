package com.example.walletja.features.user.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class UserRabbitConfig {

    public static final String EXCHANGE = "user.registration.exchange";
    public static final String QUEUE_USER_REGISTER = "user.registration.queue";
    public static final String KEY_USER_REGISTRATION = "user.registration.key";

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue userRegistrationQueue() {
        return new Queue(QUEUE_USER_REGISTER, true);
    }

    @Bean
    public Binding bindingUserRegistration(Queue userRegistrationQueue, DirectExchange userExchange) {
        return BindingBuilder
                .bind(userRegistrationQueue)
                .to(userExchange)
                .with(KEY_USER_REGISTRATION);
    }

    @Bean
    // ทุกอย่างที่ส่งเข้ามาใน MQ ไม่ว่าจะเป็น text, json, ... จะถูกนับเป็น message
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory ConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(ConnectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
