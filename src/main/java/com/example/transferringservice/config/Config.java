package com.example.transferringservice.config;

import com.example.transferringservice.repository.CardRepository;
import com.example.transferringservice.repository.DefaultCardRepository;
import com.example.transferringservice.repository.DefaultOperation;
import com.example.transferringservice.repository.OperationRepository;
import com.example.transferringservice.service.DefaultTransferService;
import com.example.transferringservice.service.TransferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public TransferService defaultTransferService() {
        return new DefaultTransferService();
    }

    @Bean
    public OperationRepository defaultOperation() {
        return new DefaultOperation();
    }

    @Bean
    public CardRepository defaultCardRepository() {
        return new DefaultCardRepository();
    }
}
