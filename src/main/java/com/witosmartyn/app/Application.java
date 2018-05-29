package com.witosmartyn.app;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication()

        @ComponentScan({"com.witosmartyn.app",
                        "com.witosmartyn.app.controllers",
                        "com.witosmartyn.app.services",
                        "com.witosmartyn.app.config"})
           @EntityScan({"com.witosmartyn.app.entities"})
@EnableJpaRepositories({"com.witosmartyn.app.repositories"})
@EnableTransactionManagement
@WebAppConfiguration
@Log4j
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);

    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);

        log.info("\n_App Started_\n");


    }


}
