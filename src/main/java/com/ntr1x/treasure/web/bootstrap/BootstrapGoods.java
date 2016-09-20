package com.ntr1x.treasure.web.bootstrap;

import java.io.File;
import java.net.URL;

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
        
        // Seller 1 Purchase 1
        
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
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
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
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
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
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
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
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
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
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
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
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearMale.getId(),
            },
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
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "11(белый)/22(св.серый)",
                "11(белый)/40(св.сирень(т.розовый))",
                "11(белый)/03(св.сине-зелёный)",
                "28(коралл)/39(св.розовый)",
                "39(св.розовый)/22(св.серый)",
                "11(белый)/60(пудра)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/8.png",
            "Альмера 41989L",
            "Шапка Состав:35% Ангора 50% Шерсть 15% Полиамид",
            "Ferz",
            1400f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "22(св.серый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/9.png",
            "Альпина 211950D (30)",
            "Шарф-снуд Состав:70% Шерсть 30% Акрил",
            "Ferz",
            1560f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "35(т.беж)/61(св.бежевый)",
                "18(черный)/15(малиновый(фуксия))",
                "18(черный)/35(т.беж)",
                "18(черный)/25(красный)",
                "18(черный)/33(ср.серый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/10.png",
            "Альф 51875B",
            "Шапка Состав:70% Шерсть 30% Акрил",
            "Ferz",
            700f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearMale.getId(),
            },
            new String[] {
                "44(т.серый)",
                "18(черный)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/11.png",
            "Амина 21/61819D",
            "Компл. Состав:70% Шерсть 30% Акрил",
            "Ferz",
            2300f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "11(белый)/19(бирюза)",
                "11(белый)/38(розовый)",
                "11(белый)/20(электрик)",
                "11(белый)/57(т.голубой)",
                "12(жемчужный)/35(т.беж)",
                "11(белый)/12(жемчужный)",
                "11(белый)/33(ср.серый)",
                "11(белый)/28(коралл)",
                "38(розовый)/33(ср.серый)",
                "73(серый)/05(терракот)",
                
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/12.png",
            "Анкара 41903V (50)",
            "Шапка Состав:20% Альпака 30% Шерсть 50% Акрил",
            "Ferz",
            1000f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "50(фиолетовый)",
                "90(т.бирюза)",
                "19(бирюза)",
                "54(ср.коричневый)",
                "85(шоколад)",
                "26(бордо)",
                "61(св.бежевый)",
                "64(липа)",
                "22(св.серый)",
                "35(т.беж)",
                "14(оранжевый)",
                "11(белый)",
                "18(черный)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/13.png",
            "Антарктида 31/21/61911D",
            "Компл. Состав:70% Шерсть 30% Акрил",
            "Ferz",
            2400f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "11(белый)/53(голубой)",
                "25(красный)/18(черный)",
                "33(ср.серый)/11(белый)",
                "35(т.беж)/11(белый)",
                "11(белый)/33(ср.серый)",
                "11(белый)/84(зеленый)",
                "05(терракот)/18(черный)",
                "11(белый)/28(коралл)",
                "18(черный)/35(т.беж)",
                "11(белый)/35(т.беж)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/14.png",
            "Арабика 61951L (100)",
            "Шапка Состав:35% Ангора 50% Шерсть 15% Полиамид",
            "Ferz",
            1100f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "51(сливовый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/15.png",
            "Аргентина 41902C (100)",
            "Шапка Состав:70% Шерсть 30% Акрил",
            "Ferz",
            700f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "66(св.фиолетовый)",
                "41(св.фиолетовый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/16.png",
            "Ариша 71884V",
            "Шапка Состав:20% Альпака 30% Шерсть 50% Акрил",
            "Ferz",
            1300f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "22(св.серый)",
                "28(коралл)",
                "35(т.беж)",
                "39(св.розовый)",
                "15(малиновый(фуксия))",
                "98(т.синий)",
                "11(белый)",
                "61(св.бежевый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/17.png",
            "Астра 41988B",
            "Шапка Состав:70% Шерсть 30% Акрил",
            "Ferz",
            1200f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "40(св.сирень(т.розовый))",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/18.png",
            "Атлантика 21/61908D",
            "Компл. Состав:70% Шерсть 30% Акрил",
            "Ferz",
            1800f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "35(т.беж)/61(св.бежевый)",
                "11(белый)/53(голубой)",
                "33(ср.серый)/11(белый)",
                "11(белый)/33(ср.серый)",
                "18(черный)/11(белый)",
                "11(белый)/98(т.синий)",
                "98(т.синий)/25(красный)",
                "33(ср.серый)/38(розовый)",
                "22(св.серый)/11(белый)",
                "42(охра)/05(терракот)",
                "11(белый)/35(т.беж)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/19.png",
            "Барабас 71939B",
            "Шапка Состав:70% Шерсть 30% Акрил",
            "Ferz",
            1800f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "22(св.серый)",
                "44(т.серый)",
                "96(джинс)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/20.png",
            "Барри 51845C (100)",
            "Шапка Состав:70% Шерсть 30% Акрил",
            "Ferz",
            760f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "30(салатовый)",
                "50(фиолетовый)",
                "66(св.фиолетовый)",
                "25(красный)",
                "55(т.фиолетовый/баклажан)",
                "90(т.бирюза)",
                "38(розовый)",
                "61(св.бежевый)",
                "06(св.сиреневый)",
                "22(св.серый)",
                "15(малиновый(фуксия))",
                "75(фисташка)",
                "11(белый)",
                "18(черный)",
                "41(св.фиолетовый)",
                "89(голубой)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/21.png",
            "Барса 51029B",
            "Шапка Состав:70% Шерсть 30% Акрил",
            "Ferz",
            700f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearMale.getId(),
            },
            new String[] {
                "18(черный)/44(т.серый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/22.png",
            "Бёрд 71518N",
            "Шапка Состав:80% Шерсть 20% Полиамид",
            "Ferz",
            500f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "11(белый)/22(св.серый)",
                "39(св.розовый)/28(коралл)",
                "11(белый)/47(св.сиреневый)",
                "40(св.сирень(т.розовый))/11(белый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/23.png",
            "Беркут 51681N",
            "Шапка Состав:80% Шерсть 20% Полиамид",
            "Ferz",
            640f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearMale.getId(),
            },
            new String[] {
                "22(св.серый)",
                "44(т.серый)",
                "33(ср.серый)",
                "96(джинс)",
                "98(т.синий)",
                "18(черный)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/24.png",
            "Берри 71879V",
            "Шапка Состав:20% Альпака 30% Шерсть 50% Акрил",
            "Ferz",
            800f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "22(св.серый)",
                "15(малиновый(фуксия))",
                "19(бирюза)",
                "38(розовый)",
                "11(белый)",
                "61(св.бежевый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/25.png",
            "Бирюза 41952E",
            "Шапка Состав:50% Шерсть 50% Акрил",
            "Ferz",
            1400f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "54(ср.коричневый)",
                "18(черный)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/26.png",
            "Божоле 61934V (60)",
            "Шапка Состав:20% Альпака 30% Шерсть 50% Акрил",
            "Ferz",
            1400f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "11(белый)",
                "18(черный)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/27.png",
            "Бонд 51028C",
            "Шапка Состав:70% Шерсть 30% Акрил",
            "Ferz",
            700f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearMale.getId(),
            },
            new String[] {
                "22(св.серый)",
                "27(т.бордо)",
                "11(белый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/28.png",
            "Бонжур 311957V",
            "Митенки Состав:20% Альпака 30% Шерсть 50% Акрил",
            "Ferz",
            700f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "11(белый)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/29.png",
            "Брабус 61946D (50)",
            "Шапка Состав:70% Шерсть 30% Акрил",
            "Ferz",
            1900f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "18(черный)/19(бирюза)",
                "35(т.беж)/61(св.бежевый)",
                "11(белый)/53(голубой)",
                "18(черный)/15(малиновый(фуксия))",
                "18(черный)/35(т.беж)",
                "18(черный)/25(красный)",
                "33(ср.серый)/28(коралл)",
            }
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase1,
            "bootstrap/purchase1/30.png",
            "Брусника 71911B",
            "Шапка Состав:70% Шерсть 30% Акрил",
            "Ferz",
            900f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultOuterwearFemale.getId(),
            },
            new String[] {
                "39(св.розовый)",
                "11(белый)",
            }
        );
        
        // Seller 1 Purchase 2
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase2,
            "bootstrap/purchase2/1.png",
            "INVU Classic  B1410B",
            "Пол:  Мужские Оправа:  Металл Цвет оправы:  Золотистая Цвет линзы:  Поляризационная зелёная",
            "INVU",
            2288f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultAccessories.getId(),
            },
            new String[] {}
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase2,
            "bootstrap/purchase2/2.png",
            "INVU Classic  B1410D",
            "Пол:  Мужские Оправа:  Металл Цвет оправы:  Тёмно-Серебристая Цвет линзы:  Поляризационная серая",
            "INVU",
            2288f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultAccessories.getId(),
            },
            new String[] {}
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase2,
            "bootstrap/purchase2/3.png",
            "INVU Classic  B1410E",
            "Пол:  Мужские Оправа:  Металл Цвет оправы:  Серебристая Цвет линзы:  Поляризационная серая с синим зеркальным покрытием",
            "INVU",
            2288f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultAccessories.getId(),
            },
            new String[] {}
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase2,
            "bootstrap/purchase2/3.png",
            "INVU Classic  B1411B",
            "Пол:  Мужские Оправа:  Металл Цвет оправы:  Золотистая Цвет линзы:  Поляризационная зелёная",
            "INVU",
            2288f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultAccessories.getId(),
            },
            new String[] {}
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase2,
            "bootstrap/purchase2/5.png",
            "INVU Classic  B1411C",
            "Пол:  Мужские Оправа:  Металл Цвет оправы:  Серебристая Цвет линзы:  Поляризационная серая",
            "INVU",
            2288f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultAccessories.getId(),
            },
            new String[] {}
        );
        
        bootstrapGood(
            target,
            state,
            state.sessions.seller1,
            state.purchases.seller1Purchase2,
            "bootstrap/purchase2/6.png",
            "INVU Classic  B1411D",
            "Пол:  Мужские Оправа:  Металл Цвет оправы:  Серебристая Цвет линзы:  Поляризационная серая с зелёным зеркальным покрытием",
            "INVU",
            2288f,
            new long[] {
                state.directories.adult.getId(),
                state.directories.adultAccessories.getId(),
            },
            new String[] {}
        );
    }
    
    private Good bootstrapGood(WebTarget target, BootstrapState state, String token, Purchase purchase, String imagePath, String title, String promo, String brand, float price, long[] categories, String[] colors) {
        
        URL imageUrl = getClass().getClassLoader().getResource(imagePath);
        
        Image image = null; {
        
            if (imageUrl != null) {
                
            
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
                        new File(imageUrl.getFile()),
                        MediaType.APPLICATION_OCTET_STREAM_TYPE
                    ))
                ;
                
                image = target
                    .path("/images/m2m")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .post(Entity.entity(mp, mp.getMediaType()), Image.class)
                ;
            }
        }
        
        IGoodService.CreateRequest s = new IGoodService.CreateRequest(); {
            
            s.purchase = purchase.getId();
            s.title = title;
            s.promo = promo;
            
            s.attributes = new IAttributeService.RelatedAttribute[] {
                new IAttributeService.RelatedAttribute(null, state.attributes.goodBrand.getId(), brand, null),
            };
            
            if (image != null) {
                
                s.images = new IImageService.RelatedImage[] {
                    new IImageService.RelatedImage(null, image.getId(), null),
                };
            }
            
            s.categories = new ICategoryService.RelatedCategory[categories.length]; {
                
                for (int i = 0; i < categories.length; i++) {
                    
                    s.categories[i] = new ICategoryService.RelatedCategory(null, categories[i], null);
                }
            }
            
            if (colors.length > 0) {
                
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
            } else {
                
                s.modifications = new IModificationService.RelatedModification[1];
                
                IModificationService.RelatedModification m = new IModificationService.RelatedModification(); {
                    m.price = price;
                    m.sizeRange = 10f;
                }
                
                s.modifications[0] = m;
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
