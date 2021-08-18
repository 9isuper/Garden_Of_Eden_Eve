package com.isuper.eden.eve.boot;

import com.isuper.eden.adam.EdenAdamBootstrap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/** @author admin */
@EnableAsync
@EnableRetry
@ComponentScan(basePackages = {"com.isuper"})
@SpringBootApplication
@EnableConfigurationProperties
public class EdenEveBootApplication {

  public static String[] args;
  public static ConfigurableApplicationContext context;

  public static void start(String[] args) {
    EdenEveBootApplication.args = args;
    EdenAdamBootstrap.run(args);
    EdenEveBootApplication.context = SpringApplication.run(EdenEveBootApplication.class, args);
    EdenAdamBootstrap.markStartupAndWait();
  }

  public static void main(String[] args) {
    EdenEveBootApplication.start(args);
  }
}
