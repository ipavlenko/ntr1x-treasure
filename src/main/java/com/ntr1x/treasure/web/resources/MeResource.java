package com.ntr1x.treasure.web.resources;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Cart;
import com.ntr1x.treasure.web.model.Order;
import com.ntr1x.treasure.web.model.OrderEntry;
import com.ntr1x.treasure.web.model.Purchase;
import com.ntr1x.treasure.web.model.Session;
import com.ntr1x.treasure.web.repository.OrderRepository;
import com.ntr1x.treasure.web.repository.PurchaseRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Api("Me")
@Component
@Path("me")
@PermitAll
public class MeResource {

    @Inject
    private Session session;
    
    @Inject
    private PurchaseRepository purchases;
    
    @Inject
    private OrderRepository orders;
    
    @GET
    @Path("/cart")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public Cart select() {
        return session.getUser().getCart();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public PurchasesResponse purchases(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size,
        @QueryParam("status") Purchase.Status status
    ){      
                        
        Page<Purchase> result = status == null
            ? purchases.findByUserId(session.getUser().getId(), new PageRequest(page, size))
            : purchases.findByUserIdAndStatus(session.getUser().getId(), status, new PageRequest(page, size))
        ;

        List<PurchasesResponse.Details> purchases = new ArrayList<>();

        for (Purchase p : result) {
            
            PurchasesResponse.Details details = new PurchasesResponse.Details(); {
                
                details.orders = orders.findByPurchaseId(p.getId());
                details.purchase = p;
            }

            for (Order order : details.orders) {
                
                for (OrderEntry entr : order.getEntries()){

                    details.goodsCnt += entr.getQuantity();
                    details.total += entr.getQuantity() * entr.getModification().getPrice();

                    if (order.getStatus() == Order.Status.PAID){
                        details.paidCnt += 1;
                    } else if (order.getStatus() == Order.Status.CONFIRMED){
                        details.confirmedCnt += 1;
                    } else if (order.getStatus() == Order.Status.CANCELED){
                        details.canceledCnt += 1;
                    }
                }
            }

            purchases.add(details);
        }

        return new PurchasesResponse(
            result.getTotalElements(),
            page,
            size,
            purchases
        );
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchasesResponse {
        
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
}
