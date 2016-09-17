package com.ntr1x.treasure.web.index;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface ModificationIndexRepositoryCustom {
    
    SearchResult search(SearchRequest request);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        
        public String query;
        public Long purchase;
        public Long good;
        public List<Long> categories;
        public List<String> attributes;
        public int page;
        public int size;
        public GroupBy groupBy;
        
        public static enum GroupBy {
            PURCHASE,
            GOOD
        }
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResult {
        
        public long count;
        public int page;
        public int size;
        public Collection<Long> items;
    }
}
