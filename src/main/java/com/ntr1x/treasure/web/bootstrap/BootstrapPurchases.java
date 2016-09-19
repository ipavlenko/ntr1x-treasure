package com.ntr1x.treasure.web.bootstrap;

import java.time.LocalDate;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p2.Purchase;
import com.ntr1x.treasure.web.services.IPurchaseService;

@Service
public class BootstrapPurchases {

    @Inject
    private BootstrapHolder holder;
    
    public BootstrapState.Purchases createPurchases(WebTarget target) {
     
        BootstrapState state = holder.get();
        
        BootstrapState.Purchases purchases = new BootstrapState.Purchases();
        
        {
            IPurchaseService.PurchaseCreate s = new IPurchaseService.PurchaseCreate(); {
                
                s.user = state.users.seller1.getId();
                s.method = state.methods.seller1MethodCash.getId();
                s.provider = state.providers.seller1China.getId();
                
                s.title = "Шапки и шарфы";
                s.promo = "Новая поставка шапок и шарфов";
                
                s.open = LocalDate.now().minusWeeks(1);
                s.stop = LocalDate.now().plusWeeks(1);
                s.delivery = LocalDate.now().plusWeeks(2);
                s.nextDelivery = LocalDate.now().plusWeeks(3);
            }
            
            purchases.seller1Purchase1 = target
                .path("/me/purchases")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller1)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Purchase.class)
            ;
        }
        
        {
            IPurchaseService.PurchaseCreate s = new IPurchaseService.PurchaseCreate(); {
                
                s.user = state.users.seller1.getId();
                s.method = state.methods.seller1MethodCard.getId();
                s.provider = state.providers.seller1India.getId();
                
                s.title = "Первая поставка всякой ерунды из Индии";
                s.open = LocalDate.now().minusWeeks(1);
                s.stop = LocalDate.now().plusWeeks(1);
                s.delivery = LocalDate.now().plusWeeks(2);
                s.nextDelivery = LocalDate.now().plusWeeks(3);
            }
            
            purchases.seller1Purchase2 = target
                .path("/me/purchases")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller1)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Purchase.class)
            ;
        }
        
        {
            IPurchaseService.PurchaseCreate s = new IPurchaseService.PurchaseCreate(); {
                
                s.user = state.users.seller1.getId();
                s.method = state.methods.seller1MethodCash.getId();
                s.provider = state.providers.seller1China.getId();
                
                s.title = "Вторая поставка всякой ерунды из Китая";
                s.open = LocalDate.now().minusWeeks(1);
                s.stop = LocalDate.now().plusWeeks(1);
                s.delivery = LocalDate.now().plusWeeks(2);
                s.nextDelivery = LocalDate.now().plusWeeks(3);
            }
            
            purchases.seller1Purchase3 = target
                .path("/me/purchases")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller1)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Purchase.class)
            ;
        }
        
        {
            IPurchaseService.PurchaseCreate s = new IPurchaseService.PurchaseCreate(); {
                
                s.user = state.users.seller2.getId();
                s.method = state.methods.seller2MethodCash.getId();
                s.provider = state.providers.seller2China.getId();
                
                s.title = "Третья поставка всякой ерунды из Китая";
                s.open = LocalDate.now().minusWeeks(1);
                s.stop = LocalDate.now().plusWeeks(1);
                s.delivery = LocalDate.now().plusWeeks(2);
                s.nextDelivery = LocalDate.now().plusWeeks(3);
            }
            
            purchases.seller2Purchase1 = target
                .path("/me/purchases")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller2)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Purchase.class)
            ;
        }
        
        {
            IPurchaseService.PurchaseCreate s = new IPurchaseService.PurchaseCreate(); {
                
                s.user = state.users.seller2.getId();
                s.method = state.methods.seller2MethodCard.getId();
                s.provider = state.providers.seller2Japan.getId();
                
                s.title = "Первая поставка всякой ерунды из Японии";
                s.open = LocalDate.now().minusWeeks(1);
                s.stop = LocalDate.now().plusWeeks(1);
                s.delivery = LocalDate.now().plusWeeks(2);
                s.nextDelivery = LocalDate.now().plusWeeks(3);
            }
            
            purchases.seller2Purchase2 = target
                .path("/me/purchases")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller2)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Purchase.class)
            ;
        }
        
        {
            IPurchaseService.PurchaseCreate s = new IPurchaseService.PurchaseCreate(); {
                
                s.user = state.users.seller2.getId();
                s.method = state.methods.seller2MethodCard.getId();
                s.provider = state.providers.seller2Japan.getId();
                
                s.title = "Вторая поставка всякой ерунды из Японии";
                s.open = LocalDate.now().minusWeeks(1);
                s.stop = LocalDate.now().plusWeeks(1);
                s.delivery = LocalDate.now().plusWeeks(2);
                s.nextDelivery = LocalDate.now().plusWeeks(3);
            }
            
            purchases.seller2Purchase3 = target
                .path("/me/purchases")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller2)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Purchase.class)
            ;
        }
        
        return purchases;
    }
}
