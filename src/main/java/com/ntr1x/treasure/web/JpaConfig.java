package com.ntr1x.treasure.web;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.eclipse.persistence.config.BatchWriting;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
public class JpaConfig extends JpaBaseConfiguration {
	
	@Value("${eclipselink.ddl-generation.output-mode}")
	private String generationMode;
	
	protected JpaConfig(
		DataSource dataSource,
		JpaProperties properties,
		ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider
	) {
		super(dataSource, properties, jtaTransactionManagerProvider);
	}

	@Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		
        EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
        return adapter;
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
        
    	HashMap<String, Object> map = new HashMap<String, Object>();
        
    	map.put(PersistenceUnitProperties.WEAVING, "static");
    	map.put(PersistenceUnitProperties.DEPLOY_ON_STARTUP, "true");
    	map.put(PersistenceUnitProperties.BATCH_WRITING, BatchWriting.JDBC);
    	map.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.DROP_AND_CREATE);
    	map.put(PersistenceUnitProperties.CREATE_JDBC_DDL_FILE, "storage-create.sql");
    	map.put(PersistenceUnitProperties.DROP_JDBC_DDL_FILE, "storage-drop.sql");
    	map.put(PersistenceUnitProperties.DDL_GENERATION_MODE, generationMode);
    	map.put(PersistenceUnitProperties.CACHE_SHARED_DEFAULT, "false");
    	map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_COMMIT_WITHOUT_PERSIST_RULES, "false");
    	
        return map;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
