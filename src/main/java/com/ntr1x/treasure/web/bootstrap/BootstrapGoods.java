package com.ntr1x.treasure.web.bootstrap;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p3.Good;
import com.ntr1x.treasure.web.services.IAttributeService;
import com.ntr1x.treasure.web.services.ICategoryService;
import com.ntr1x.treasure.web.services.IGoodService;
import com.ntr1x.treasure.web.services.IModificationService;

@Service
public class BootstrapGoods {

    @Inject
    private BootstrapHolder holder;
    
    public void createGoods(WebTarget target) {
        
        BootstrapState state = holder.get();
        
        {
            IGoodService.CreateRequest s = new IGoodService.CreateRequest(); {
                
                s.purchase = state.purchases.seller1Purchase1.getId();
                s.title = "Китайские штаны";
                s.promo = "Одноразовые китайские штаны";
                
                s.attributes = new IAttributeService.RelatedAttribute[] {
                    new IAttributeService.RelatedAttribute(null, state.attributes.goodBrand.getId(), "Adibas", null),
                };
                
                s.categories = new ICategoryService.RelatedCategory[] {
                    new ICategoryService.RelatedCategory(null, state.directories.adult.getId(), null),
                    new ICategoryService.RelatedCategory(null, state.directories.adultOuterwearMale.getId(), null),
                };
                
                IModificationService.RelatedModification m1 = new IModificationService.RelatedModification(); {
                    m1.price = 100f;
                    m1.sizeRange = 10f;
                    m1.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#FF0000", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "S", null),
                    };
                }
                
                IModificationService.RelatedModification m2 = new IModificationService.RelatedModification(); {
                    m2.price = 120f;
                    m2.sizeRange = 10f;
                    m2.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#FF0000", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "M", null),
                    };
                }
                
                IModificationService.RelatedModification m3 = new IModificationService.RelatedModification(); {
                    m3.price = 150f;
                    m3.sizeRange = 10f;
                    m3.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#FF0000", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "L", null),
                    };
                }
                
                IModificationService.RelatedModification m4 = new IModificationService.RelatedModification(); {
                    m4.price = 100f;
                    m4.sizeRange = 10f;
                    m4.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#00FF00", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "S", null),
                    };
                }
                
                IModificationService.RelatedModification m5 = new IModificationService.RelatedModification(); {
                    m5.price = 120f;
                    m5.sizeRange = 10f;
                    m5.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#00FF00", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "M", null),
                    };
                }
                
                IModificationService.RelatedModification m6 = new IModificationService.RelatedModification(); {
                    m6.price = 150f;
                    m6.sizeRange = 10f;
                    m6.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#00FF00", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "L", null),
                    };
                }
                
                s.modifications = new IModificationService.RelatedModification[] {
                    m1, m2, m3, m4, m5, m6
                };
            }
            
            target
                .path(String.format("/purchases/i/%d/goods", state.purchases.seller1Purchase1.getId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller1)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Good.class)
            ;
        }
        
        {
            IGoodService.CreateRequest s = new IGoodService.CreateRequest(); {
                
                s.purchase = state.purchases.seller1Purchase2.getId();
                s.title = "Индийские шапочки";
                s.promo = "Индийские вязаные шапочки";
                
                s.attributes = new IAttributeService.RelatedAttribute[] {
                    new IAttributeService.RelatedAttribute(null, state.attributes.goodBrand.getId(), "IndianHats", null),
                };
                
                s.categories = new ICategoryService.RelatedCategory[] {
                    new ICategoryService.RelatedCategory(null, state.directories.adult.getId(), null),
                    new ICategoryService.RelatedCategory(null, state.directories.adultOuterwearFemale.getId(), null),
                };
                
                IModificationService.RelatedModification m1 = new IModificationService.RelatedModification(); {
                    m1.price = 200f;
                    m1.sizeRange = 10f;
                    m1.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#FF0000", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "S", null),
                    };
                }
                
                IModificationService.RelatedModification m2 = new IModificationService.RelatedModification(); {
                    m2.price = 220f;
                    m2.sizeRange = 10f;
                    m2.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#FF0000", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "M", null),
                    };
                }
                
                IModificationService.RelatedModification m3 = new IModificationService.RelatedModification(); {
                    m3.price = 250f;
                    m3.sizeRange = 10f;
                    m3.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#FF0000", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "L", null),
                    };
                }
                
                IModificationService.RelatedModification m4 = new IModificationService.RelatedModification(); {
                    m4.price = 200f;
                    m4.sizeRange = 10f;
                    m4.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#00FF00", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "S", null),
                    };
                }
                
                IModificationService.RelatedModification m5 = new IModificationService.RelatedModification(); {
                    m5.price = 220f;
                    m5.sizeRange = 10f;
                    m5.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#00FF00", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "M", null),
                    };
                }
                
                IModificationService.RelatedModification m6 = new IModificationService.RelatedModification(); {
                    m6.price = 250f;
                    m6.sizeRange = 10f;
                    m6.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), "#00FF00", null),
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationSize.getId(), "L", null),
                    };
                }
                
                s.modifications = new IModificationService.RelatedModification[] {
                    m1, m2, m3, m4, m5, m6
                };
            }
            
            target
                .path(String.format("/purchases/i/%d/goods", state.purchases.seller1Purchase2.getId()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.seller1)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Good.class)
            ;
        }
    }
}
