package com.ntr1x.treasure.web.bootstrap;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import com.ntr1x.treasure.web.converter.ImageSettingsProvider.ImageSettings;
import com.ntr1x.treasure.web.model.p1.Image;
import com.ntr1x.treasure.web.model.p3.Publication;
import com.ntr1x.treasure.web.services.ICategoryService;
import com.ntr1x.treasure.web.services.IImageService;
import com.ntr1x.treasure.web.services.IPublicationService;
import com.ntr1x.treasure.web.services.IScaleImageService;

public class BootstrapPublications {
    
    private Bootstrap bootstrap;

    public BootstrapPublications(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }
    
    public List<Publication> createPublications(WebTarget target, String token) {
        
        List<Publication> publications = new ArrayList<>();
        List<IPublicationService.PublicationCreate> create = new ArrayList<>();
        
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/01.jpg");
            Image image1 = bootstrapImage(target, token, "bootstrap/publications/10.jpg");
            Image image2 = bootstrapImage(target, token, "bootstrap/publications/11.jpg");
            Image image3 = bootstrapImage(target, token, "bootstrap/publications/12.jpg");
            Image image4 = bootstrapImage(target, token, "bootstrap/publications/13.jpg");
            Image image5 = bootstrapImage(target, token, "bootstrap/publications/14.jpg");
            Image image6 = bootstrapImage(target, token, "bootstrap/publications/15.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {

                p.title = "\"Нацимбио\" заключила госконтракт на поставку вакцины против гепатита B";
                p.promo = "Сумма контракта составила 297 млн рублей.";
                p.content = "Национальная иммунобиологическая компания («Нацимбио») заключила с Минздравом госконтракт о поставках в регионы лекарственных препаратов для профилактики вирусного гепатита В. \n" +
                        "Сумма контракта составила 297 млн рублей. Поставка вакцины против гепатита В осуществляется в рамках обеспечения Национального календаря профилактических прививок. Поставки, согласно контракту, должны быть организованы менее чем за три недели, сообщили в Нацимбио.\n" +
                        "«Всего по договору лекарственными препаратами для профилактики вирусного гепатита В будут обеспечены 162 учреждения здравоохранения в 83 регионах РФ», пояснил генеральный директор «Нацимбио» Николай Семенов.\n" +
                        "До 1 февраля 2016 года, согласно поручению президента Владимира Путина, должен быть определен федеральный поставщик препаратов от туберкулеза, ВИЧ-инфекции и вирусных гепатитов. Ожидается, что им станет дочерняя структура госкорпорации «Ростех» – «Нацимбио» (подробнее о расширении монопольных владений – в материале «Большая заразница», Vademecum #25-26 (92-93) от 27 июля 2015 года). \n" +
                        "Суммарный объем потребности государства в препаратах от гепатитов оценивается в 2,1 млрд рублей ежегодно.";
                p.published = LocalDateTime.now().minusDays(30);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.globe.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null),
                        new IImageService.RelatedImage(null, image1.getId(), new String[] { "thumbnail" }, null),
                        new IImageService.RelatedImage(null, image2.getId(), new String[] { "thumbnail" }, null),
                        new IImageService.RelatedImage(null, image3.getId(), new String[] { "thumbnail" }, null),
                        new IImageService.RelatedImage(null, image4.getId(), new String[] { "thumbnail" }, null),
                        new IImageService.RelatedImage(null, image5.getId(), new String[] { "thumbnail" }, null),
                        new IImageService.RelatedImage(null, image6.getId(), new String[] { "thumbnail" }, null),
                };
            }
    
            create.add(p);
        }
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/02.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "Большинство сокращаемых медработников не получают альтернативных предложений о работе.";
                p.promo = "Около 80% сокращаемых медицинских работников не получают альтернативных предложений при увольнении. Об этом свидетельствуют результаты опроса, проведенного Фондом \"Здоровье\".";
                p.content = "Около 80% сокращаемых медицинских работников не получают альтернативных предложений при увольнении. Об этом свидетельствуют результаты опроса, проведенного Фондом «Здоровье» при помощи мобильного приложения «Справочник врача», говорится в официальном пресс-релизе.\n" +
                        "В материал Фонда говорится, что чаще всего о состоявшейся или планируемой кадровой оптимизации сообщали работники медицинских стационаров (58%). Ответы сотрудников поликлиник и скорой помощи практически сопоставимы: 48% и 49% соответственно заявили о сокращениях.\n" +
                        "«По свидетельству респондентов, почти в 80% случаев сокращаемым медикам не предлагается трудоустроиться на равноценную должность в другой медицинской организации, либо пройти переобучение по другой медицинской специальности. Минздрав России громко заявлял в конце 2014 года, что совместный проект с крупным рекрутинговым агентством поможет сокращаемым в поиске работы, однако опрашиваемые этой поддержки не чувствуют», - отметил директор Фонда независимого мониторинга «Здоровье» Эдуард Гаврилов.\n" +
                        "О том, что руководство учреждений не предлагает альтернативную работу или переобучение, заявили более 85% медработников в Красноярском крае, Чувашии, Ленинградской, Новгородской и Саратовской областях.\n" +
                        "В случае сокращения большинство респондентов собираются устроиться в другую государственную медицинскую организацию (39%) либо уйти на работу в коммерческую клинику (28,5%). Оставить профессию и сменить сферу деятельности готовы 16% опрошенных, причем принять такое кардинальное решение в большей степени готовы работники скорой помощи – они сообщали о подобных намерениях в 24% случаев. Готовность реализоваться вне медицинской профессии выразили примерно 16% врачей-специалистов. Еще 10% медработников готовы ради профессии ехать в другой регион.";
                p.published = LocalDateTime.now().minusDays(29);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.medicine.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.endoprosthesis.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/03.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "России предсказали позднюю и умеренную эпидению гриппа";
                p.promo = "Об этом свидетельствует данные наблюдений за распространением гриппа в южном полушарии.";
                p.content = "В текущем сезоне эпидемия гриппа начнется в декабре-январе и будет умеренной. Об этом заявила ТАСС заместитель директора НИИ Грипа Людмила Цыбалова. Об этом, по словам Цыбаловой, свидетельствуют данные наблюдений за распространением гриппа в южном полушарии. \n" +
                        "Цыбалова добавила, что на данный момент у циркулирующих вирусов гриппа не обнаружено новых мутаций, повышающих их агрессивность. Основными эпидемическими штаммами, по прогнозу эксперта, будут H3N2 и H1N1, возможно более широкое, чем обычно, распространение гриппа B. Также продолжится циркуляция пандемического штамма H1N1, однако его влияние на эпидемиологическую ситуацию будут незначительным, поскольку значительная часть населения уже имеет иммунитет к этому вирусу. \n" +
                        "Отсутствие новых мутаций циркулирующих в настоящее время штаммов гриппа позволяет надеяться на то, что заранее подготовленные вакцины против смогу обеспечить надежную защиту от этой инфекции. В прошлом году эффективность прививочной кампании была ниже, чем обычно, поскольку в вакцинах отсутствовал мутировавший штамм H3N2, пояснила замдиректора НИИ гриппа.";
                p.published = LocalDateTime.now().minusDays(28);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.research.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.specialization.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/04.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "России предсказали позднюю и умеренную эпидению гриппа";
                p.promo = "Об этом свидетельствует данные наблюдений за распространением гриппа в южном полушарии.";
                p.content = "В текущем сезоне эпидемия гриппа начнется в декабре-январе и будет умеренной. Об этом заявила ТАСС заместитель директора НИИ Грипа Людмила Цыбалова. Об этом, по словам Цыбаловой, свидетельствуют данные наблюдений за распространением гриппа в южном полушарии. \n" +
                        "Цыбалова добавила, что на данный момент у циркулирующих вирусов гриппа не обнаружено новых мутаций, повышающих их агрессивность. Основными эпидемическими штаммами, по прогнозу эксперта, будут H3N2 и H1N1, возможно более широкое, чем обычно, распространение гриппа B. Также продолжится циркуляция пандемического штамма H1N1, однако его влияние на эпидемиологическую ситуацию будут незначительным, поскольку значительная часть населения уже имеет иммунитет к этому вирусу. \n" +
                        "Отсутствие новых мутаций циркулирующих в настоящее время штаммов гриппа позволяет надеяться на то, что заранее подготовленные вакцины против смогу обеспечить надежную защиту от этой инфекции. В прошлом году эффективность прививочной кампании была ниже, чем обычно, поскольку в вакцинах отсутствовал мутировавший штамм H3N2, пояснила замдиректора НИИ гриппа.";
                p.published = LocalDateTime.now().minusDays(27);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.research.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.trauma.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/05.jpg");
        
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "Японских хирургов будут обучать с помощью российских роботов";
                p.promo = "В японском Университете Джунтендо состоялась торжественная церемония открытия симуляционного центра, оснащенног роботами-симуляторами.";
                p.content = "7 октября в японском Университете Джунтендо состоялась торжественная церемония открытия симуляционного центра, оснащенного роботами-симуляторами, созданными казанской компанией «Эйдос-Медицина». По информации пресс-службы Минпромторга, российские разработки служат тренажерами для имитации реальных ситуаций, возникающих в операционных. С их помощью можно отрабатывать подготовку пациента к наркозу, проведение хирургических вмешательств, выведение больного из наркоза и послеоперационные реанимационные мероприятия. \n" +
                        "Качество и уникальные характеристики российских стимуляторов подтверждены крупнейшей в мире компанией по производству хирургического оборудования Medtronic-Covidien.\n" +
                        "Согласно сообщению, казанские роботы в настоящее время также поставляются в США, Турцию, страны Евросоюза и СНГ. В мире подобное оборудование производят пять компаний, однако стоимость их продукция значительно дороже и по многим параметрам проигрывает российским аналогам. \n" +
                        "Ранее «Эйдос-Медицина» обнародовала сумму контракта на поставку медицинских симуляторов в Японию: оборудование и его установка обойдутся японской стороне в 1,5 миллиона долларов США.\n" +
                        "«Эйдос-Медицина» была основана в 2012 году, в ее штат входят 130 свыше разработчиков. Годовой оборот компании оценивается в 16 млн долларов США. «Эйдос-Медицина» - резидент кластера биомедицинских технологий Фонда «Сколково».";
                p.published = LocalDateTime.now().minusDays(26);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.medicine.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.arthroscopy.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/06.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "В Канаде разрешили медицинское применение героина";
                p.promo = "Канадские пациенты с тяжелой формой наркотической зависимости смогут получить инъекцию фармацевтического героина прямо в кабинете врача: медицинским работникам на законодательном уровне разрешили прописывать героин пациентам, которым не помогают стандартные методы лечения, в том числе метадон.";
                p.content = "Канадские пациенты с тяжелой формой наркотической зависимости смогут получить инъекцию фармацевтического героина прямо в кабинете врача: медицинским работникам на законодательном уровне разрешили прописывать героин пациентам, которым не помогают стандартные методы лечения, в том числе метадон. Об этом информирует CNN.\n" +
                        "По словам сторонников лечения, введение фармацевтического героина под контролем врача, безусловно, не поможет в борьбе в зависимостью, однако снизит вероятность передозировки, заражения гемоконтактными вирусными инфекциями. Также доступ к медицинскому героину позволит сократить нелегальное использование наркотиков и связь с криминалом среди наркозависимых.\n" +
                        "Власти Канады отмечают, что данная практика уже одобрена правительственными некоторых европейских стран. ";
                p.published = LocalDateTime.now().minusDays(25);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.medicine.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.specialization.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/07.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "«Фармимэкс» завершила модернизацию завода в Скопине";
                p.promo = "Компания «Фармацевтический импорт, экспорт» («Фармимэкс») запустила новые производственные мощности на фармацевтическом заводе «Скопинфарм». В модернизацию площадки было вложено более 1 млрд рублей, сообщается в пресс-релизе, поступившем в распоряжение редакции Remedium.";
                p.content = "Компания «Фармацевтический импорт, экспорт» («Фармимэкс») запустила новые производственные мощности на фармацевтическом заводе «Скопинфарм». В модернизацию площадки было вложено более 1 млрд рублей, сообщается в пресс-релизе, поступившем в распоряжение редакции Remedium.\n" +
                        "В рамках проекта по обновлению производства был построен производственный корпус площадью более 1 000 м2 для производства четырех стадий препаратов крови по технологии швейцарской компании Octapharma AG, также была подготовлена контрольно-аналитическая лаборатория для проведения физико-химических и биохимических анализов факторов крови.\n" +
                        "По планам компании, модернизация инфраструктуры позволит увеличить объемы производства только препаратов крови до 346 тысяч флаконов в год, и, следовательно, полностью заместить импортные препараты для лечения гемофилии.";
                p.published = LocalDateTime.now().minusDays(24);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.conference.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.endoprosthesis.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/08.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "Ограничение импорта соли и морской воды может иметь негативные последствия для фармпроизводителей";
                p.promo = "Введение ограничений на поставки в Россию поваренной соли и морской воды могут пострадать как минимум три иностранных и один российский производитель, говорится в материале аналитической компании RNC Pharma, поступившем в распоряжение редакции Remedium.";
                p.content = "Введение ограничений на поставки в Россию поваренной соли и морской воды могут пострадать как минимум три иностранных и один российский производитель, говорится в материале аналитической компании RNC Pharma, поступившем в распоряжение редакции Remedium.\n" +
                        "С 1 ноября 2016 года в перечень продукции, на которую распространяются ограничению по поставкам из ряда стран, будет внесена товарная субпозиция 250100, в рамках которой в Россию импортируется в том числе морская вода и ряд продуктов на ее основе. Данное ограничение может негативно отразиться на ввозе некоторых импортных и производстве отечественных препаратов на основе морской воды, используемых для профилактики и лечения заболеваний носа и горла.\n" +
                        "В текущем году препараты на основе морской воды поставлялись по двум товарным субпозициям ТНВЭД, при этом по группе 250100 было ввезено 2 торговых марки: Физиомер («Омега-Биттнер») и Хьюмер («Лаборатории Урго»). По этой же товарной субпозиции в Россию ввозится морская вода для производства препарата Линаква, компании «Солофарм». Так же вплоть до ноября 2015 года по этой группе ввозились отдельные продукты в линейке Аква Марис («Ядран Галенский»).\n" +
                        "За первые 7 месяцев 2016 года в РФ было импортировано готовых препаратов на основе морской воды на сумму 0,8 млрд рублей, динамика поставок к аналогичному периоду прошлого года в серьёзном минусе (-59%). За год также серьезно снизился натуральный объем поставок: на 57%. ";
                p.published = LocalDateTime.now().minusDays(23);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.expose.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.arthroscopy.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/09.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "Takeda намерена укрепить позиции на американском рынке";
                p.promo = "Японский крупнейший фармпроизводитель Takeda планирует потратить около 15 млрд долларов на приобретение американских компаний для расширения своего присутствия на западных рынках. Об этом пишет Financial Times со ссылкой на информированные источники.";
                p.content = "Японский крупнейший фармпроизводитель Takeda планирует потратить около 15 млрд долларов на приобретение американских компаний для расширения своего присутствия на западных рынках. Об этом пишет Financial Times со ссылкой на информированные источники.\n" +
                        "По информации издания, Takeda рассматривает возможность покупки компаний, занимающихся разработкой лекарственных препаратов для лечения рака, заболеваний пищеварительной и центральной нервной системы. \n" +
                        "На данный момент неизвестно, планируется ли одна крупная покупка либо будет приобретено несколько небольших компаний. Согласно данным источников, Takeda в случае необходимости может собрать до 20 млрд долларов.\n" +
                        "Последняя крупная сделка с участием Takeda была проведена в 2011 году. Тогда японская компания выкупила за 14 млрд долларов ценные бумаги швейцарской фармацевтической группы Nycomed. ";
                p.published = LocalDateTime.now().minusDays(22);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.medicine.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.trauma.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.localizations.interphalangeal.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/10.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "Allergan заплатит за Vitae Pharmaceuticals 639 млн долларов";
                p.promo = "Allergan договорилась о приобретении биотехнологической компании Vitae Pharmaceuticals за 639 млн долларов, что в два раза превышает рыночную стоимость разработчика лекарственных препаратов, пишет The Wall Street Journal.";
                p.content = "Allergan договорилась о приобретении биотехнологической компании Vitae Pharmaceuticals за 639 млн долларов, что в два раза превышает рыночную стоимость разработчика лекарственных препаратов, пишет The Wall Street Journal.\n" +
                        "Сделка, в рамках которой Allergan выплатит по 21 доллару за каждую акцию Vitae (на момент закрытия биржи ценные бумаги оценивались в 8,10 доллара), позволит Allergan усилить дерматологическое направление. \n" +
                        "По словам исполнительного директора Allergan Брента Сондерса (Brent Saunders), покупка Vitae является стратегической инвестицией. Благодаря сделке, Allergan получит права на экспериментальные препараты для лечения псориаза и дерматита. ";
                p.published = LocalDateTime.now().minusDays(21);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.globe.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.endoprosthesis.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/11.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "ФАС готова дать возможность реальным аптекам торговать в интернете";
                p.promo = "Федеральная антимонопольная служба (ФАС) поддерживает интернет-торговлю лекарственными препаратами, но только если интернет-магазин привязан к конкретному аптечному учреждению. Об этом рассказал «Интерфаксу» начальник управления антимонопольного ведомства Тимофей Нижегородцев.";
                p.content = "Федеральная антимонопольная служба (ФАС) поддерживает интернет-торговлю лекарственными препаратами, но только если интернет-магазин привязан к конкретному аптечному учреждению. Об этом рассказал «Интерфаксу» начальник управления антимонопольного ведомства Тимофей Нижегородцев.\n" +
                        "«Мы поддерживаем онлайн-витрину аптечного учреждения, потому что оно осуществляет входной контроль, выполняет лицензионные требования, соблюдает режимы хранения и доставки, несет ответственность за продажу препаратов рецептурного отпуска», - сказал он.\n" +
                        "Он подчеркнул, что ведомство выступает только против интернет-торговли лекарствами в общепринятом смысле, когда сайт привязан к виртуальному ресурсу, который предлагает виртуальные лекарства, которые при доставке могут оказаться фальсифицированными или контрафактными.\n" +
                        "По словам Нижегородцева, законопроект об онлайн-торговле лекарственными препаратами уже почти готов. Сроки его принятия зависят от правительства. Данный законопроект разрешает дистанционную торговлю лекарствами только аптечным организациям (для лекарственных препаратов для медицинского применения) и ветеринарным аптечными организациям (для лекарственных препаратов для ветеринарного применения) в установленном порядке. ";
                p.published = LocalDateTime.now().minusDays(20);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.expose.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.specialization.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/12.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "Шесть российских лабораторий по диагностике полиомиелита получили сертификаты ВОЗ";
                p.promo = "Всемирная организация здравоохранения (ВОЗ) выдала сертификаты о соответствии международным стандартам шести национальным лабораториям РФ по диагностике полиомиелита. На следующий календарный год аккредитацию получили лаборатории в Москве, Санкт-Петербурге, Омске, Екатеринбурге, Ставрополе и Хабаровске, сообщается на сайте Минздрава РФ.";
                p.content = "Всемирная организация здравоохранения (ВОЗ) выдала сертификаты о соответствии международным стандартам шести национальным лабораториям РФ по диагностике полиомиелита. На следующий календарный год аккредитацию получили лаборатории в Москве, Санкт-Петербурге, Омске, Екатеринбурге, Ставрополе и Хабаровске, сообщается на сайте Минздрава РФ.\n" +
                        "Полученные документы подтверждают, что лаборатории обладают необходимыми возможностями выявлять и идентифицировать дикие и вакцинно-родственные полиовирусы, которые могут присутствовать в клинических образцах и образцах сточных вод.\n" +
                        "Аккредитация национальных лабораторий по полиомиелиту ежегодно проводится региональным офисом ВОЗ и основывается на оценке показателей работы лаборатории в течение 12 месяцев, предшествующих дате проведения оценки. Аккредитация выдается на последующий календарный год. ";
                p.published = LocalDateTime.now().minusDays(19);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.medicine.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.trauma.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/13.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "Для иммунизации детей против гриппа в этом эпидсезоне будут использоваться три российские вакцины";
                p.promo = "В 2016 году для вакцинации детей от гриппа в рамках Национального календаря прививок будет использовано три российские вакцины, в том числе две новые, рассказала глава Роспотребнадзора Анна Попова.";
                p.content = "В 2016 году для вакцинации детей от гриппа в рамках Национального календаря прививок будет использовано три российские вакцины, в том числе две новые, рассказала глава Роспотребнадзора Анна Попова.\n" +
                        "Попова отметила, что для иммунизации 18 млн детей (из них 5 млн – дошкольного возраста) будет использоваться Гриппол Плюс и новые вакцины Совигрипп и Ультрикс. По ее словам, более половины от планируемого объема вакцин от гриппа уже доставлено в регионы.\n" +
                        "«На сегодняшний день доставка вакцин от гриппа первым траншем осуществлена во все субъекты, и 52% от планируемого количества уже находится на территориях субъектов РФ и муниципалитетов. Иммунизация уже начата», - приводит ТАСС слова Поповой. ";
                p.published = LocalDateTime.now().minusDays(18);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.expose.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.endoprosthesis.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/14.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "Распространение электронных сигарет оказалось связано с ростом показателя успешного отказа от табака в Великобритании";
                p.promo = "Рост популярности электронных сигарет оказался связан с повышением показателя успешного отказа от табакокурения, свидетельствуют результаты исследования, проведенного в Великобритании. Результаты работы были опубликованы в British Medical Journal.";
                p.content = "Рост популярности электронных сигарет оказался связан с повышением показателя успешного отказа от табакокурения, свидетельствуют результаты исследования, проведенного в Великобритании. Результаты работы были опубликованы в British Medical Journal.\n" +
                        "По данным исследователей, приверженность к электронным сигаретам позволила в 2015 году отказаться от табака 18 тыс. человек в Англии. Ученые подчеркивают, что хотя сама по себе эта цифра не велика, она клинически значима из-за огромного позитивного влияния самого факта прекращения курения: при отказе от курения в возрасте 40 лет, ожидаемая продолжительность жизни увеличивается на 9 лет (по сравнению с человеком, продолжающим курить).\n" +
                        "В рамках работы ученые проанализировали данные Smoking Toolkit Study, в котором были собраны данные о 43 тыс. курильщиках в возрасте от 16 лет за 2006-2015 год. Также была изучена база данных национальной системы здравоохранения об успешных случаях отказа от курения. Согласно результатам исследования, использование электронных сигарет негативно сказывается на применении рецептурных методов борьбы с табакокурения, однако в данной области необходимо проведение дополнительных исследований.\n" +
                        "В прошлом году департамент здравоохранения Великобритании выступил в поддержку электронных сигарет, заявив, что они на 95% безопаснее обычных табакосодержащих сигарет и могут использоваться как вспомогательные средства при отказе от курения. Стоит отметить, что мнение британского минздрава идет вразрез с позицией Всемирной организации здравоохранения (ВОЗ), призывающейограничить рекламу и продажу электронных сигарет. По мнению части экспертного сообщества, содержащие никотин электронные сигареты являются табачными изделиями, следовательно, их оборот должен регулироваться соответствующим законом. ";
                p.published = LocalDateTime.now().minusDays(17);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.medicine.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.specialization.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/15.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "Roche успешно испытала лекарственное средство против рассеянного склероза";
                p.promo = "Экспериментальное лекарственное средство окрелизумаб (ocrelizumab), разработанное компанией Roche для лечения рассеянного склероза (РС), превзошло по эффективности препарат интерферона бета-1a, применяющегося в настоящее время в терапии данного заболевания. Об этом свидетельствуют предварительные результаты КИ, опубликованные на сайте компании.";
                p.content = "Экспериментальное лекарственное средство окрелизумаб (ocrelizumab), разработанное компанией Roche для лечения рассеянного склероза (РС), превзошло по эффективности препарат интерферона бета-1a, применяющегося в настоящее время в терапии данного заболевания. Об этом свидетельствуют предварительные результаты КИ, опубликованные на сайте компании.\n" +
                        "Согласно представленным данным, среди пациентов с релапсирующим-ремиттирующим РС, принимавших окрелизумаб, частота достижения состояния «без признаков заболевания» на 75% была выше, чем в группе пациентов, принимавших интерферон бета-1a. В группе пациентов с первичным прогрессирующим РС, в окрелизумаб-группе показатель прекращения развития заболевания был на 47% выше, чем в группе сравнения.\n" +
                        "Ранее в этом году окрелизумаб получил статус принципиально нового лекарственного средства (Breakthrough Therapy) от Администрации по контролю за продуктами и лекарствами США (FDA), данный препарат стал первым в своем классе, получившем данный статус. По прогнозам аналитиков из Thomson Reuters, в случае регистрации окрелизумаба, продажи лекарственного средства к 2022 году достигнут 4,09 млрд долларов.\n" +
                        "Окрелизумаб является моноклональным антителом, специфичным к CD20 позитивным В-клеткам иммунной системы. ";
                p.published = LocalDateTime.now().minusDays(16);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.research.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.arthroscopy.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/16.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "В Москве снизилось количество пациентов с инфарктом";
                p.promo = "С 2013 года в Москве на 20% сократилось количество случаев инфарктов, рассказала главный кардиолог столицы Елена Васильева. Она подчеркнула, что добиться таких успехов позволило создание системы оперативной помощи пациентам.";
                p.content = "С 2013 года в Москве на 20% сократилось количество случаев инфарктов, рассказала главный кардиолог столицы Елена Васильева. Она подчеркнула, что добиться таких успехов позволило создание системы оперативной помощи пациентам.\n" +
                        "«Результативность инфарктной сети подтверждает статистика за последние три года. Не только уменьшилась больничная летальность от инфаркта миокарда, но и стала снижаться сама заболеваемость. С 2013 года количество случаев инфаркта миокарда снизилось более чем на 20%», - приводит слова Васильевой ТАСС.\n" +
                        "Она также сообщила, что в Москве стали открывать первые кабинеты вторичной профилактики инфарктов и инсультов. «В тестовом режиме они работают уже в нескольких поликлиниках Москвы. Однако к февралю 2017 года кабинет вторичной профилактики будет работать в центральной поликлинике каждого округа столицы», - отметила Васильева. ";
                p.published = LocalDateTime.now().minusDays(15);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.expose.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.specialization.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/17.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "В Госдуму внесен законопроект о введении системы видеофиксации операций";
                p.promo = "Депутаты Государственной Думы подготовили законопроект о внесении изменений в федеральный закон «Об основах охраны здоровья граждан в РФ» с целью внедрения практики видеосъемки оперативных вмешательств с применением общей анестезии. ";
                p.content = "Депутаты Государственной Думы подготовили законопроект о внесении изменений в федеральный закон «Об основах охраны здоровья граждан в РФ» с целью внедрения практики видеосъемки оперативных вмешательств с применением общей анестезии. \n" +
                        "Как отметили в пояснительной записке разработчики документа, в последнее время увеличилось число случаев неправомерных действий медицинского персонала лечебных учреждений при проведении операций. Так, согласно статистическим данным, ежегодно обнаруживается около 3 тыс. забытых в пациентах предметов. \n" +
                        "Депутаты подчеркивают, что внедрение системы видеозаписи оперативных вмешательств позволит повысить ответственность медицинского персонала, а также сделает возможным использование данных видео фиксации при обнаружении фактов халатности медработников. Законопроектом предусматривается, что видеозапись может быть выдана по запросу правоохранительных органов, пациента, родственников пациента и вышестоящих медицинских организаций. \n" +
                        "Стоит отметить, что законопроект не был поддержан Правительством РФ в частности из-за того, что не был определен источник финансового обеспечения затрат, связанных с проведением медицинской организацией видеозаписи. ";
                p.published = LocalDateTime.now().minusDays(14);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.conference.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.endoprosthesis.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/18.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "Foamix успешно испытала топическое средство против розацеа";
                p.promo = "Компания Foamix Pharmaceuticals сообщила о получении убедительных данных клинических исследований II фазы топического средства для лечения папуло-пустулезной розацеа";
                p.content = "Компания Foamix Pharmaceuticals сообщила о получении убедительных данных клинических исследований II фазы топического средства для лечения папуло-пустулезной розацеа. Экспериментальное лекарственное средство FMX103 продемонстрировало свое превосходство над плацебо, сообщается в официальном пресс-релизе разработчика ЛС.\n" +
                        "В исследованиях приняли участие 233 пациента со средней и тяжелой формами розацеа. В зависимости от группы пациенты прошли терапию FMX103 (пена, содержащая миноциклин 3% или 1,5%) или пеной с инертным веществом. Препарат наносился на пораженные участки один раз в день на протяжении 12 недель, наблюдение за участниками КИ продолжалось еще 4 недели после завершения лечения.\n" +
                        "Эффективность FMX103 оценивалось по количеству очагов воспаления, а также по шкале оценки тяжести заболевания (IGA). Помимо этого оценивалась безопасность и переносимость препарата.\n" +
                        "По данным компании, в группе терапии активным веществом очаги воспаления сократились на 21,1% и 19,9 % (для 1,5 и 3% концентраций), тогда как в плацебо-группе этот показатель составил 7,8%.";
                p.published = LocalDateTime.now().minusDays(13);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.medicine.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.trauma.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/19.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "Гормональная внутриматочная система экономически эффективна в предупреждении развития рака эндометрия у женщин с ожирением";
                p.promo = "Установка левоноргестрел-содержащей внутриматочной системы оказалась эффективным и экономически целесообразным методом защиты женщин, страдающих ожирением, от летального исхода в результате злокачественной опухоли эндометрия.";
                p.content = "Установка левоноргестрел-содержащей внутриматочной системы оказалась эффективным и экономически целесообразным методом защиты женщин, страдающих ожирением, от летального исхода в результате злокачественной опухоли эндометрия. Об этом свидетельствуют результаты исследования, опубликованные в Obstetrics & Gynecology.\n" +
                        "Ученые из медицинского центра Университета Дьюка использовали модифицированную модель Маркова для оценки эффективности и экономической целесообразности установки внутриматочной системы с левоноргестрелом по сравнению со стандартными профилактическими мерами для женщин старше 50 лет с индексом массы тела (ИМТ) более 40, входящих в группу риска по развитию рака эндометрия. При подсчете экономической целесообразности авторы работы учитывали стоимость установки внутриматочной системы и противоопухолевого лечения.\n" +
                        "Согласно полученным результатам, установка гормональной внутриматочной рилизинг-системы на 68% снижает вероятность развития рака эндометрия у женщин с ожирением. Авторы подсчитали, что коэффициент эффективности затрат на рилизинг-систему у женщин с ИМП более 40 составляет 74 707 долларов США на каждый год жизни (по состоянию на 2015 год). Исходя из того, что защитный эффект рилизинг-системы сохраняется в течение 10 лет, инкрементный коэффициент эффективности затрат снижается на 37 858 долларов на каждый год сохраненной жизни. \n" +
                        "При этом, в случае женщин с ИМТ более 30, коэффициент эффективности рилизинг-системы составил уже 137 223 тыс. долларов. ";
                p.published = LocalDateTime.now().minusDays(12);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.globe.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.specialization.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
    
        {
            Image image = bootstrapImage(target, token, "bootstrap/publications/20.jpg");
            
            IPublicationService.PublicationCreate p = new IPublicationService.PublicationCreate(); {
    
                p.title = "«Катрен» вложит около 1 млрд рублей в строительство новой штаб-квартиры";
                p.promo = "Российская научно-производственная компания «Катрен» в 2019 году планирует завершить строительство нового головного офиса в наукограде Кольцово. ";
                p.content = "Российская научно-производственная компания «Катрен» в 2019 году планирует завершить строительство нового головного офиса в наукограде Кольцово. Инвестиции в объект площадью 16 тыс. квадратных метров оцениваются примерно в 1 млрд рублей, сообщает «Интерфакс-Недвижимость».\n" +
                        "На новой площадке разместится АО НПК «Катрен» и его дочерние структуры. Как рассказал основной акционер группы «Катрен» Леонид Конобеев, действующего офиса в Нижней Ельцовке (в черте Новосибирска) для нужд компании уже недостаточно.\n" +
                        "«В прошлом году мы приняли решение о строительстве собственного здания, которое объединило бы офис и новый складской комплекс. Был выбран участок на территории биотехнопарка наукограда Кольцово. К сожалению, выяснилось, что геологические условия на площадке не подходят для строительства склада. Сейчас мы рассматриваем другие участки для логокомплекса, в том числе в районе наукограда», — отметил он.\n" +
                        "Будущий логистический комплекс будет обслуживать юг Западной Сибири. Его площадь составит 8-10 тыс. кв. м, месячный оборот — 1,5 млрд рублей. Сейчас заказы в регионе обрабатывает склад в Бердске. Однако его возможности по расширению ограничены, что может вызвать проблемы в период пиковых нагрузок.\n" +
                        "Помимо этого в текущем году «Катрен» намерена запустить новый складской комплекс в Челябинске. На текущий момент общая площадь складских комплексов компании составляет 121,5 тыс. кв.м. ";
                p.published = LocalDateTime.now().minusDays(11);
                p.categories = new ICategoryService.RelatedCategory[] {
                        new ICategoryService.RelatedCategory(null, bootstrap.directories.medicine.getId(), null),
                        new ICategoryService.RelatedCategory(null, bootstrap.specializations.arthroscopy.getId(), null)
                };
                p.images = new IImageService.RelatedImage[] {
                        new IImageService.RelatedImage(null, image.getId(), new String[] { "primary" }, null)
                };
            }
    
            create.add(p);
        }
        
        for (IPublicationService.PublicationCreate p : create) {
            
            Publication r = target
                .path("/publications")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(p, MediaType.APPLICATION_JSON_TYPE), Publication.class)
            ;
            
            publications.add(r);
        }
        
        return publications;
    }
    
    private Image bootstrapImage(WebTarget target, String token, String path) {
        
        URL imageUrl = getClass().getClassLoader().getResource(path);
        
        ImageSettings settings = new ImageSettings(); {
            settings.items = new ImageSettings.Item[] {
                new ImageSettings.Item("image", "jpg", 800, 800, IScaleImageService.Type.CONTAIN),
                new ImageSettings.Item("gallery", "png", 200, 120, IScaleImageService.Type.COVER),
                new ImageSettings.Item("news-avatar", "png", 110, 110, IScaleImageService.Type.COVER),
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
        
        return target
            .path("/images/m2m")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .header(HttpHeaders.AUTHORIZATION, token)
            .post(Entity.entity(mp, mp.getMediaType()), Image.class)
        ;
    }
}
