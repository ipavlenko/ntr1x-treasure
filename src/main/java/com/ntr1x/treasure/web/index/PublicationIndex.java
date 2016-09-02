package com.ntr1x.treasure.web.index;

import org.apache.solr.client.solrj.beans.Field;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PublicationIndex {
    
    @Field("id")
    public String id;
    
    @Field("resource_l")
    public Long resource;
    
    @Field("type_s")
    public String dtype;
    
    @Field("title_txt_ru")
    private String title;
    
    @Field("promo_txt_ru")
    private String promo;
    
    @Field("content_txt_ru")
    private String content;
    
    @Field("tags_txts_ru")
    private String[] tags;
    
    @Field("category_ls")
    public Long[] categories;
}