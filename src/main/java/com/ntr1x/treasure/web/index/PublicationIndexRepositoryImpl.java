package com.ntr1x.treasure.web.index;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.ScoredPage;

public class PublicationIndexRepositoryImpl implements PublicationIndexRepositoryCustom {

    @Inject
    private SolrTemplate solr;
    
    @Override
    public SearchResult search(SearchRequest request) {
        
        FilterQuery fq = new SimpleFilterQuery(new SimpleStringCriteria("type_s:publication"));
        
        for (Long[] categories : request.categories) {
            
            if (categories.length > 0) {
                StringBuilder b = new StringBuilder();
                b.append('(');
                b.append(String.format("category_ls:%d", categories[0]));
                for (int i = 1; i < categories.length; i++) {
                    b.append(String.format(" OR category_ls:%d", categories[i]));
                }
                b.append(')');
                fq.addCriteria(new SimpleStringCriteria(b.toString()));
            }
        }
        
        if (request.since != null || request.until != null) {
            
            fq.addCriteria(
                new SimpleStringCriteria(
                    String.format(
                        "published_dt:[%s TO %s]",
                        request.since != null
                            ? request.since.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".000Z"
                            : '*',
                        request.until != null
                            ? request.until.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".000Z"
                            : '*'
                                
                    )
                )
            );
        }
        
        String s = request.query == null ? "" : request.query.trim();
        
        Query q = new SimpleQuery()
            .addFilterQuery(fq)
            .setPageRequest(new PageRequest(request.page, request.size))
            .addCriteria(new SimpleStringCriteria(
                s.isEmpty() || "*".equals(s)
                    ? "*:*"
                    : String.format("title_txt_ru:\"%s\" promo_txt_ru:\"%s\" content_txt_ru:\"%s\"", s, s, s)
            ))
        ;
        
        ScoredPage<PublicationIndex> p = solr.queryForPage(q, PublicationIndex.class);
        
        return new SearchResult(
            p.getTotalElements(),
            request.page,
            request.size,
            p.getContent().stream().map(d -> d.resource).collect(Collectors.toList())
        );
    }
}
