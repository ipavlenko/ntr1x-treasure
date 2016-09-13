package com.ntr1x.treasure.web.services;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Order;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IOrderService {

    OrdersResponse select(
        int page,
        int size,
        Order.Status status,
        Long user,
        Long purchase
    );
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrdersResponse {
        
        public long count;
        public int page;
        public int size;
        public List<Details> orders;
        
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Details {
              
            public Order order;
            public float total;
            public float deliveryPrice;
            public float totalToPay;
        }
    }
}
