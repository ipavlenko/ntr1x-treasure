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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Cart;
import com.ntr1x.treasure.web.model.Method;
import com.ntr1x.treasure.web.model.Order;
import com.ntr1x.treasure.web.model.OrderEntry;
import com.ntr1x.treasure.web.model.Provider;
import com.ntr1x.treasure.web.model.Purchase;
import com.ntr1x.treasure.web.model.Session;
import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.repository.OrderRepository;
import com.ntr1x.treasure.web.repository.PurchaseRepository;
import com.ntr1x.treasure.web.services.IMethodService;
import com.ntr1x.treasure.web.services.IMethodService.MethodCreate;
import com.ntr1x.treasure.web.services.IOrderService;
import com.ntr1x.treasure.web.services.IOrderService.OrdersResponse;
import com.ntr1x.treasure.web.services.IProviderService;
import com.ntr1x.treasure.web.services.IProviderService.ProviderCreate;
import com.ntr1x.treasure.web.services.IPurchaseService;
import com.ntr1x.treasure.web.services.IPurchaseService.PurchaseCreate;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("Me")
@Component
@Path("/me")
@PermitAll
public class MeResource {

    @Inject
    private Session session;
    
    @Inject
    private PurchaseRepository purchases;
    
    @Inject
    private OrderRepository orders;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private IOrderService orderService;
    
    @Inject
    private IPurchaseService purchaseService;
    
    @Inject
    private IProviderService providerService;
    
    @Inject
    private IMethodService methodService;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public User select() {
        return session.getUser();
    }
    
    @GET
    @Path("/cart")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public Cart selectCart() {
        return session.getUser().getCart();
    }
    
    @GET
    @Path("/orders")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public OrdersResponse orders(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size,
        @QueryParam("purchase") Long purchase,
        @QueryParam("status") Order.Status status
    ) {
        return orderService.select(page, size, status, session.getUser().getId(), purchase);
    }
    
//    @POST
//    @Path("/orders/i/{id}/status")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RolesAllowed({ "auth" })
//    @Transactional
//    public OrdersResponse ordersUpdateStatus(
//        @QueryParam("page") @ApiParam(example = "0") int page,
//        @QueryParam("size") @ApiParam(example = "10") int size,
//        @QueryParam("purchase") Long purchase,
//        @QueryParam("status") Order.Status status
//    ) {
//        return orderService.select(page, size, status, session.getUser().getId(), purchase);
//    }
    
    @GET
    @Path("/purchases")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public IPurchaseService.DetailsResponse purchases(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size,
        @QueryParam("status") Purchase.Status status
    ) {
                        
        Page<Purchase> result = purchases.findByUserIdAndStatus(session.getUser().getId(), status, new PageRequest(page, size));

        List<IPurchaseService.DetailsResponse.Details> purchases = new ArrayList<>();

        for (Purchase p : result) {
            
            IPurchaseService.DetailsResponse.Details details = new IPurchaseService.DetailsResponse.Details(); {
                
                details.orders = orders.findByStatusAndUserIdAndPurchaseId(null, null, p.getId(), null).getContent();
                details.purchase = p;
            }

            for (Order order : details.orders) {
                
                for (OrderEntry entr : order.getEntries()){

                    details.goodsCnt += entr.getQuantity();
                    details.total += entr.getQuantity() * entr.getModification().getPrice();

                    if (order.getStatus() == Order.Status.PAID){
                        details.paidCnt += 1;
                    } else if (order.getStatus() == Order.Status.CONFIRMED) {
                        details.confirmedCnt += 1;
                    } else if (order.getStatus() == Order.Status.CANCELED) {
                        details.canceledCnt += 1;
                    }
                }
            }

            purchases.add(details);
        }

        return new IPurchaseService.DetailsResponse(
            result.getTotalElements(),
            page,
            size,
            purchases
        );
    }
    
    @POST
    @Path("/purchases")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public Purchase purchasesCreate(PurchaseCreate create) {
        
        if (create.user != session.getUser().getId()) {
            throw new WebApplicationException("Cannot create purchase for another user", Response.Status.FORBIDDEN);
        }
        
        Purchase purchase = purchaseService.create(create);
        
        security.grant(session.getUser(), purchase.getAlias(), "admin");
        
        return purchase;
    }
    
    @GET
    @Path("/providers")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public List<Provider> providers() {
        
        return session.getUser().getProviders();
    }
    
    @POST
    @Path("/providers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public Provider providersCreate(ProviderCreate create) {
        
        if (create.user != session.getUser().getId()) {
            throw new WebApplicationException("Cannot create provider for another user", Response.Status.FORBIDDEN);
        }
        
        Provider provider = providerService.create(create);
        
        security.grant(session.getUser(), provider.getAlias(), "admin");
        
        return provider;
    }
    
    @GET
    @Path("/methods")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public List<Method> methods() {
        
        return session.getUser().getMethods();
    }
    
    @POST
    @Path("/methods")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public Method methodsCreate(MethodCreate create) {
        
        if (create.user != session.getUser().getId()) {
            throw new WebApplicationException("Cannot create payment method for another user", Response.Status.FORBIDDEN);
        }
        
        Method method = methodService.create(create);
        
        security.grant(session.getUser(), method.getAlias(), "admin");
        
        return method;
    }
}
