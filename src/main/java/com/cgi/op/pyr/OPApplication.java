package com.cgi.op.pyr;

import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

/** SpringBoot application startup class */
@SpringBootApplication
public class OPApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(OPApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(OPApplication.class, args);
  }

  /**
   * Initialize H2 embedded DB TODO : Remove this initializer when use a persistent DB
   *
   * @param connectionFactory
   * @return
   */
  @Bean
  ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
    LOGGER.info("ConnectionFactoryInitializer : {}", connectionFactory.getMetadata());
    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);
    initializer.setDatabasePopulator(
        new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
    return initializer;
  }
}
