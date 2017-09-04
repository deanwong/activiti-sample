package com.foo.spring.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by wangding on 19/03/2017.
 */
@Configuration
public class UpgradeConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradeConfiguration.class);

    public static final String FLYWAY = "flyway";

    @Bean(name = FLYWAY)
    public UpgradeService upgrade(DataSource dataSource) {
        UpgradeService upgradeService = new FlywayUpgradeService(dataSource);
        try {
            upgradeService.upgrade();
        } catch (Exception ex) {
            LOGGER.error("Flyway failed!", ex);
        }
        return upgradeService;
    }

}
