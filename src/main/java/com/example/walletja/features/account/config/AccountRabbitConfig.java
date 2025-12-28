package com.example.walletja.features.account.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountRabbitConfig {

    public static String EXCHANGE = "walletja.exchange";

    public static final String QUEUE_ACCOUNT_CREATE = "account.create.queue";
    public static final String KEY_ACCOUNT_CREATE = "account.create.key";

    public static final String QUEUE_ACCOUNT_UPDATE = "account.update.queue";
    public static final String KEY_ACCOUNT_UPDATE = "account.update.key";

    public static final String QUEUE_ACCOUNT_DELETE = "account.delete.queue";
    public static final String KEY_ACCOUNT_DELETE = "account.delete.key";

    @Bean
    public Queue accountCreateQueue() {
        return new Queue(QUEUE_ACCOUNT_CREATE, true);
    }

    @Bean
    public Binding bindingAccountCreate(Queue accountCreateQueue, DirectExchange walletExchange) {
        return BindingBuilder
            .bind(accountCreateQueue)
            .to(walletExchange)
            .with(KEY_ACCOUNT_CREATE);
    }

    @Bean
    public Queue accountUpdateQueue() {
        return new Queue(QUEUE_ACCOUNT_UPDATE, true);
    }

    @Bean
    public Binding bindingAccountUpdate(Queue accountUpdateQueue, DirectExchange walletExchange) {
        return BindingBuilder
            .bind(accountUpdateQueue)
            .to(walletExchange)
            .with(KEY_ACCOUNT_UPDATE);
    }

    @Bean
    public Queue accountDeleteQueue() {
        return new Queue(QUEUE_ACCOUNT_DELETE, true);
    }

    @Bean
    public Binding bindingAccountDelete(Queue accountDeleteQueue, DirectExchange walletExchange) {
        return BindingBuilder
            .bind(accountDeleteQueue)
            .to(walletExchange)
            .with(KEY_ACCOUNT_DELETE);
    }
}
