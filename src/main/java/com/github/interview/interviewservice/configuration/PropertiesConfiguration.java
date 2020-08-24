package com.github.interview.interviewservice.configuration;

import com.github.interview.interviewservice.configuration.properties.ExceptionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        ExceptionProperties.class
})
public class PropertiesConfiguration {
}
