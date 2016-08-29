package com.ntr1x.treasure.web.conversation;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.ntr1x.treasure.web.converter.AppConverterProvider.LocalDateTimeConverter;

@XmlRootElement
public class Publication {
    
    public String title;
    public String promo;
    public String content;
    
    @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    public String published;
    
    @XmlElement
    public List<Tag> tags;
    
    public static class Tag {
        
        public String value;
    }
}