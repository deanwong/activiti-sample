package com.foo.spring.upgrade;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Created by wangding on 19/03/2017.
 */
public class FlywayUpgradeService implements UpgradeService {

    private static final Logger LOG = LoggerFactory.getLogger(FlywayUpgradeService.class);

    private DataSource dataSource;

    public FlywayUpgradeService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String BASE_LINE = "1.0.0";

    private Flyway buildFlyway() {
        Flyway flyway = new Flyway();
        flyway.setLocations("db/migration");
        flyway.setTable("flyway_schema_version");
        flyway.setValidateOnMigrate(true);
        flyway.setCleanOnValidationError(false);
        flyway.setDataSource(dataSource);
        return flyway;
    }

    @Override
    public void upgrade() {
        LOG.info("============================ Start clean & upgrade ============================");
        Flyway flyway = buildFlyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString(BASE_LINE);
//        flyway.clean();
        flyway.migrate();
        LOG.info("============================ Successfully clean & upgrade ============================");
    }

}
