package com.ntr1x.treasure.web.bootstrap;

import java.io.File;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.converter.ImageSettingsProvider.ImageSettings;
import com.ntr1x.treasure.web.model.p1.Image;
import com.ntr1x.treasure.web.model.p2.Purchase;
import com.ntr1x.treasure.web.model.p3.Good;
import com.ntr1x.treasure.web.services.IAttributeService;
import com.ntr1x.treasure.web.services.ICategoryService;
import com.ntr1x.treasure.web.services.IGoodService;
import com.ntr1x.treasure.web.services.IImageService;
import com.ntr1x.treasure.web.services.IModificationService;
import com.ntr1x.treasure.web.services.IScaleImageService;

@Service
public class BootstrapGoods {

    @Inject
    private BootstrapHolder holder;
    
    public void createGoods(WebTarget target) {
        
        BootstrapState state = holder.get();
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/1.png",
            "Абба 41959B",
            "Шапка Состав:70% Шерсть 30% Акрил",
            "Ferz",
            1000f,
            new String[] {
                "61(св.бежевый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/2.png",
            "Адамас 21870E",
            "Шарф-снуд Состав:50% Шерсть 50% Акрил",
            "Ferz",
            1800f,
            new String[] {
                "22(св.серый)",
                "05(терракот)",
                "10(лавандовый)",
                "33(ср.серый)",
                "85(шоколад)",
                "26(бордо)",
                "98(т.синий)",
                "18(черный)",
                "61(св.бежевый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/3.png",
            "Акапелла 21/61777F",
            "Компл. Состав:70% Шерсть 30% Акрил",
            "Ferz",
            2300f,
            new String[] {
                "15(малиновый(фуксия))",
                "33(ср.серый)",
                "11(белый)",
                "18(черный)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/4.png",
            "Акварель 41978V",
            "Шапка Состав:20% Альпака 30% Шерсть 50% Акрил",
            "Ferz",
            1900f,
            new String[] {
                "05(терракот)",
                "55(т.фиолетовый/баклажан)",
                "19(бирюза)",
                "33(ср.серый)",
                "54(ср.коричневый)",
                "85(шоколад)",
                "26(бордо)",
                "61(св.бежевый)",
                "22(св.серый)",
                "28(коралл)",
                "35(т.беж)",
                "42(охра)",
                "15(малиновый(фуксия))",
                "98(т.синий)",
                "11(белый)",
                "18(черный)",
                "47(св.сиреневый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/5.png",
            "Акулина 61815N",
            "Шапка Состав:80% Шерсть 20% Полиамид",
            "Ferz",
            1640f,
            new String[] {
                "35(т.беж)",
                "33(ср.серый)",
                "26(бордо)",
                "04(песочный беж.",
                "11(белый)",
                "18(черный)",
                "61(св.бежевый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/6.png",
            "Алекс 51134C",
            "Шапка Состав:70% Шерсть 30% Акрил",
            "Ferz",
            600f,
            new String[] {
                "22(св.серый)",
                "27(т.бордо)",
                "44(т.серый)",
                "33(ср.серый)",
                "85(шоколад)",
                "98(т.синий)",
                "11(белый)",
                "18(черный)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/7.png",
            "Алика 71695N",
            "Шапка Состав:80% Шерсть 20% Полиамид",
            "Ferz",
            1000f,
            new String[] {
                "11(белый)/22(св.серый)",
                "11(белый)/40(св.сирень(т.розовый))",
                "11(белый)/03(св.сине-зелёный)",
                "28(коралл)/39(св.розовый)",
                "39(св.розовый)/22(св.серый)",
                "11(белый)/60(пудра)",
            }
        );
    }
    
    private Good bootstrapGood(WebTarget target, BootstrapState state, String token, Purchase purchase, String imagePath, String title, String promo, String brand, float price, String[] colors) {
        
        ImageSettings settings = new ImageSettings(); {
            settings.items = new ImageSettings.Item[] {
                new ImageSettings.Item("contain-550x550", "png", 550, 550, IScaleImageService.Type.CONTAIN),
                new ImageSettings.Item("contain-140x140", "png", 140, 140, IScaleImageService.Type.CONTAIN),
            };
        }
        
        @SuppressWarnings("resource")
        MultiPart mp = new FormDataMultiPart()
            .field("settings", settings, MediaType.APPLICATION_JSON_TYPE)
            .bodyPart(new FileDataBodyPart(
                "file",
                new File(getClass().getClassLoader().getResource(imagePath).getFile()),
                MediaType.APPLICATION_OCTET_STREAM_TYPE
            ))
        ;
        
        Image image = target
            .path("/images/m2m")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .header(HttpHeaders.AUTHORIZATION, token)
            .post(Entity.entity(mp, mp.getMediaType()), Image.class)
        ;
        
        IGoodService.CreateRequest s = new IGoodService.CreateRequest(); {
            
            s.purchase = purchase.getId();
            s.title = title;
            s.promo = promo;
            
            s.attributes = new IAttributeService.RelatedAttribute[] {
                new IAttributeService.RelatedAttribute(null, state.attributes.goodBrand.getId(), brand, null),
            };
            
            s.images = new IImageService.RelatedImage[] {
                new IImageService.RelatedImage(null, image.getId(), null),
            };
            
            s.categories = new ICategoryService.RelatedCategory[] {
                new ICategoryService.RelatedCategory(null, state.directories.adult.getId(), null),
                new ICategoryService.RelatedCategory(null, state.directories.adultOuterwearFemale.getId(), null),
            };
            
            s.modifications = new IModificationService.RelatedModification[colors.length];
            
            for (int i = 0; i < colors.length; i++) {
                
                IModificationService.RelatedModification m = new IModificationService.RelatedModification(); {
                    m.price = price;
                    m.sizeRange = 10f;
                    m.attributes = new IAttributeService.RelatedAttribute[] {
                        new IAttributeService.RelatedAttribute(null, state.attributes.modificationColor.getId(), colors[i], null),
                    };
                }
                
                s.modifications[i] = m;
            }
        }
        
        return target
            .path(String.format("/purchases/i/%d/goods", purchase.getId()))
            .request(MediaType.APPLICATION_JSON_TYPE)
            .header(HttpHeaders.AUTHORIZATION, state.sessions.seller1)
            .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Good.class)
        ;
    }
}
