package com.ntr1x.treasure.web.index;

import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface PublicationIndexRepositoryCustom {
    
    SearchResult search(SearchRequest request);

    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        
        public int page;
        public int size;
        public String query;
        public LocalDateTime since;
        public LocalDateTime until;
        public Long[][] categories;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResult {
        
        public long count;
        public int page;
        public int size;
        public List<Long> items;
    }
}
