package br.com.alurafood.order.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
