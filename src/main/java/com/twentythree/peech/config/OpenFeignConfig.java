package com.twentythree.peech.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.twentythree.peech")
class OpenFeignConfig {

}