package com.ntr1x.treasure.web;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;

@Component
public class FlywayConfig implements FlywayMigrationStrategy {
    
	@Override
    public void migrate(Flyway flyway) {
        flyway.setBaselineOnMigrate(true);
        flyway.migrate();
    }
}