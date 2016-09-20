package com.ntr1x.treasure.web.index;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleField;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.GroupEntry;

import com.ntr1x.treasure.web.index.ModificationIndexRepositoryCustom.SearchRequest.GroupBy;

public class ModificationIndexRepositoryImpl implements ModificationIndexRepositoryCustom {

    @Inject
    private SolrTemplate solr;
    
    @Override
    public SearchResult search(SearchRequest request) {
            
        FilterQuery fq = new SimpleFilterQuery(new SimpleStringCriteria("type_s:modification")); {
            
            if (request.purchase != null) {
                fq.addCriteria(new SimpleStringCriteria(String.format("purchase_l:%d", request.purchase)));
            }
            
            if (request.good != null) {
                fq.addCriteria(new SimpleStringCriteria(String.format("good_l:%d", request.good)));
            }
            
            if (request.categories != null) {
                
                for (Long category : request.categories) {
                    fq.addCriteria(new SimpleStringCriteria(String.format("category_ls:%d", category)));
                }
            }
            
            if (request.attributes != null) {
                
                for (String attribute : request.attributes) {
                    fq.addCriteria(new SimpleStringCriteria(String.format("attribute_ss:%s", attribute)));
                }
            }
        }
        
        String s = request.query == null ? "" : request.query.trim();
        
        GroupOptions groupOptions = new GroupOptions()
            .addGroupByField(new SimpleField(request.groupBy == GroupBy.GOOD ? "good_l" : "purchase_l"))
        ;
        
        Query q = new SimpleQuery()
            .setGroupOptions(groupOptions)
            .setRows(1000000)
            .addFilterQuery(fq)
            .addCriteria(new SimpleStringCriteria(
                s.isEmpty() || "*".equals(s)
                    ? "*:*"
                    : String.format("title_txt_ru:\"%s\" promo_txt_ru:\"%s\" content_txt_ru:\"%s\"", s, s, s)
            ))
        ;
        
        Page<GroupEntry<ModificationIndex>> p = solr
            .queryForGroupPage(q, ModificationIndex.class)
            .getGroupResult(request.groupBy == GroupBy.GOOD ? "good_l" : "purchase_l")
            .getGroupEntries()
        ;
        
        List<Long> list = p.getContent()
            .stream()
            .map((g) -> g.getResult().getContent().get(0).resource)
            .skip(request.page * request.size)
            .limit(request.size)
            .collect(Collectors.toList())
        ;
        
        return new SearchResult(
            p.getTotalElements(),
            request.page,
            request.size,
            list
        );
    }
}
