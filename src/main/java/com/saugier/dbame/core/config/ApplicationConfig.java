package com.saugier.dbame.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class ApplicationConfig {

    @Bean
    public Logger logger(){
        return LoggerFactory.getLogger(Logger.class);
    }

    @Bean(name = "privateDB")
    @Primary
    @ConfigurationProperties(prefix = "spring.private-db")
    public DataSource privateDB() {
        return DataSourceBuilder.create().build();
    }

//    @Bean(name = "sharedDB")
//    @ConfigurationProperties(prefix = "spring.shared-db")
//    public DataSource sharedDB() {
//        return DataSourceBuilder.create().build();
//    }
}
