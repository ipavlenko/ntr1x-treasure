package com.ntr1x.treasure.web.index;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;

public class ModificationIndexRepositoryImpl implements ModificationIndexRepositoryCustom {

    @Inject
    private SolrTemplate solr;
    
    @Override
    public List<ModificationIndex> search(String query, Long purchase, Long good, List<Long> categories, List<String> attributes) {
            
        FilterQuery fq = new SimpleFilterQuery(new SimpleStringCriteria("type_s:modification")); {
            
            if (purchase != null) {
                fq.addCriteria(new SimpleStringCriteria(String.format("purchase_l:%d", purchase)));
            }
            
            if (good != null) {
                fq.addCriteria(new SimpleStringCriteria(String.format("good_l:%d", good)));
            }
            
            if (categories != null) {
                
                for (Long category : categories) {
                    fq.addCriteria(new SimpleStringCriteria(String.format("category_ls:%d", category)));
                }
            }
            
            if (attributes != null) {
                
                for (String attribute : attributes) {
                    fq.addCriteria(new SimpleStringCriteria(String.format("attribute_ss:%s", attribute)));
                }
            }
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
        
        return solr.queryForPage(q, ModificationIndex.class).getContent();
    }
}
