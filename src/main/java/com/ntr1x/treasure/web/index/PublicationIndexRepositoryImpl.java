package com.ntr1x.treasure.web.index;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;

public class PublicationIndexRepositoryImpl implements PublicationIndexRepositoryCustom {

    @Inject
    private SolrTemplate solr;
    
    @Override
    public List<PublicationIndex> search(String query, List<Long> categories) {
        
        FilterQuery fq = new SimpleFilterQuery(new SimpleStringCriteria("type_s:publication"));
        for (Long category : categories) {
            fq.addCriteria(new SimpleStringCriteria(String.format("category_ls:%d", category)));
        }
        
        String s = query == null ? "" : query.trim();
        
        Query q = new SimpleQuery()
            .addFilterQuery(fq)
            .addCriteria(new SimpleStringCriteria(
                s.isEmpty() || "*".equals(s)
                    ? "*:*"
                    : String.format("title_txt_ru:\"%s\" promo_txt_ru:\"%s\" content_txt_ru:\"%s\"", s, s, s)
            ))
        ;
        
        return solr.queryForPage(q, PublicationIndex.class).getContent();
    }

}
