package com.ntr1x.treasure.web;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(
    basePackages = { "com.ntr1x.treasure.web" },
    multicoreSupport = true
)
public class SolrConfig {

    @Value("${solr.url}")
    private String url;
    
    @Bean
    public SolrClient solrClient() {
      return new HttpSolrClient(url);
    }
    
    @Bean
    public SolrTemplate solrTemplate(SolrClient client) {
        return new SolrTemplate(client);
    }
}
