package com.ntr1x.treasure.web.index;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Indexed;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ModificationIndex {
    
    @Field("id")
    public String id;
    
    @Field("resource_l")
    public Long resource;
    
    @Field("good_l")
    public Long good;
    
    @Field("purchase_l")
    public Long purchase;
    
    @Field("type_s")
    public String dtype;
    
    @Field("title_txt_ru")
    @Indexed(boost = 4.0f)
    private String title;
    
    @Field("promo_txt_ru")
    @Indexed(boost = 2.0f)
    private String promo;
    
    @Field("content_txt_ru")
    @Indexed(boost = 1.0f)
    private String content;
    
    @Field("tags_txts_ru")
    @Indexed(boost = 2.0f)
    private String[] tags;
    
    @Field("category_ls")
    public Long[] categories;
    
    @Field("attribute_ss")
    public String[] attributes;
}