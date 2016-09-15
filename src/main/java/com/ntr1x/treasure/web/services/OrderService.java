package com.ntr1x.treasure.web.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p3.Order;
import com.ntr1x.treasure.web.model.p3.Order.Status;
import com.ntr1x.treasure.web.model.p5.OrderEntry;
import com.ntr1x.treasure.web.repository.OrderRepository;

@Service
public class OrderService implements IOrderService {

    @Inject
    private OrderRepository orders;
    
    @Override
    public OrdersResponse select(int page, int size, Status status, Long user, Long purchase) {
        
        Page<Order> p = orders.findByStatusAndUserIdAndPurchaseId(status, user, purchase, new PageRequest(page, size));
        
        List<OrdersResponse.Details> details = new ArrayList<>(); {
            
            for (Order o : p.getContent()) {
                
                OrdersResponse.Details d = new OrdersResponse.Details(); {
                    d.order = o;
                    
                    for (OrderEntry entry : o.getEntries()){
                        d.total += entry.getQuantity() * entry.getModification().getPrice();
                    }
                    
                    d.deliveryPrice = o.getDepot().getDeliveryPrice();
                    d.totalToPay = d.total + d.deliveryPrice;
                }
                
                details.add(d);
            }
        }
        
        return new OrdersResponse(
            p.getTotalElements(),
            page,
            size,
            details
        );
    }
}
