package com.ntr1x.treasure.web.services;

import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.ntr1x.treasure.web.converter.AppConverterProvider.LocalDateConverter;
import com.ntr1x.treasure.web.model.Order;
import com.ntr1x.treasure.web.model.Purchase;
import com.ntr1x.treasure.web.model.Purchase.Status;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IPurchaseService {
    
    Purchase create(PurchaseCreate create);
    Purchase update(long id, PurchaseUpdate update);
    Purchase remove(long id);
    
    PurchasesResponse list(int page, int size, Long user, Status status);
    
    Purchase select(long id);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseCreate {
        
        public String title;
        public long provider;
        public long method;
        public long user;
        
        @XmlJavaTypeAdapter(LocalDateConverter.class)
        @ApiModelProperty(example="1970-01-01")
        public LocalDate open;

        @XmlJavaTypeAdapter(LocalDateConverter.class)
        @ApiModelProperty(example="1970-01-01")
        public LocalDate stop;

        @XmlJavaTypeAdapter(LocalDateConverter.class)
        @ApiModelProperty(example="1970-01-01")
        public LocalDate delivery;
        
        @XmlJavaTypeAdapter(LocalDateConverter.class)
        @ApiModelProperty(example="1970-01-01")
        public LocalDate nextDelivery;
        
        @XmlElement
        public IAttributeService.CreateAttribute[] attributes;
        
        @XmlElement
        public IImageService.CreateImage[] images;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseUpdate {
        
        public String title;
        public long provider;
        public long method;
        public long user;
        
        @XmlJavaTypeAdapter(LocalDateConverter.class)
        @ApiModelProperty(example="1970-01-01")
        public LocalDate open;

        @XmlJavaTypeAdapter(LocalDateConverter.class)
        @ApiModelProperty(example="1970-01-01")
        public LocalDate stop;

        @XmlJavaTypeAdapter(LocalDateConverter.class)
        @ApiModelProperty(example="1970-01-01")
        public LocalDate delivery;
        
        @XmlJavaTypeAdapter(LocalDateConverter.class)
        @ApiModelProperty(example="1970-01-01")
        public LocalDate nextDelivery;
        
        @XmlElement
        public IAttributeService.UpdateAttribute[] attributes;
        
        @XmlElement
        public IImageService.UpdateImage[] images;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailsResponse {
        
        public long count;
        public int page;
        public int size;
        public List<Details> purchases;
        
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Details {
              
            public Purchase purchase;
            public List<Order> orders;
              
            public float total;
            public int paidCnt;
            public int confirmedCnt;
            public int canceledCnt;
            public float goodsCnt;
        }
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchasesResponse {
        
        public long count;
        public int page;
        public int size;
        public List<Purchase> purchases;
    }
}
