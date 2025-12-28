package com.example.walletja.features.wallet.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WalletRabbitConfig {

    public static String EXCHANGE = "walletja.exchange";

    public static String QUEUE_TRANSACTION_DEPOSIT = "wallet.deposit.queue";
    public static String KEY_TRANSACTION_DEPOSIT = "wallet.deposit.key";

    public static String QUEUE_TRANSACTION_WITHDRAW = "wallet.withdraw.queue";
    public static String KEY_TRANSACTION_WITHDRAW = "wallet.withdraw.key";


    @Bean
    public Queue walletDepositQueue() {
        return new Queue(QUEUE_TRANSACTION_DEPOSIT, true);
    }

    @Bean 
    public Binding bindingWalletDeposit(Queue walletDepositQueue, DirectExchange walletExchange) {
        return BindingBuilder
            .bind(walletDepositQueue) 
            .to(walletExchange)
            .with(KEY_TRANSACTION_DEPOSIT);
    }

    @Bean
    public Queue walletWithDrawQueue() {
        return new Queue(QUEUE_TRANSACTION_WITHDRAW, true);
    }

    @Bean
    public Binding bindingWalletWithDraw(Queue walletWithDrawQueue, DirectExchange walletExchange) {
        return BindingBuilder
            .bind(walletWithDrawQueue)
            .to(walletExchange)
            .with(KEY_TRANSACTION_WITHDRAW);
    }
}
