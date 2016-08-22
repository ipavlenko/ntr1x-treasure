//package com.ntr1x.treasure.web.records;
//
//import java.util.List;
//import java.util.function.Function;
//import java.util.function.Predicate;
//
//import javax.persistence.EntityManager;
//
//import org.springframework.beans.BeanUtils;
//import org.springframework.util.Assert;
//
//import com.ntr1x.treasure.web.model.Managed;
//import com.ntr1x.treasure.web.model.Resource;
//import com.ntr1x.treasure.web.services.ResourceService;
//
//public class ResourceRecord<T extends Resource> extends ManagedRecord<T> {
//
//    public ResourceRecord(ResourceService service, Class<T> clazz) {
//        super(service, clazz);
//    }
//
//    @Override
//    public T create(T managed, Function<T, String> alias, Predicate<T> validate) {
//
//        Assert.isTrue(validate.test(managed));
//
//        EntityManager em = service.getEntityManager();
//
//        T m = BeanUtils.instantiate(clazz);
//        
//        handleAllProperties(managed, m);
//
//        em.persist(m);
//        em.flush();
//
//        m.setAlias(alias.apply(m));
//
//        handleAllChildren(managed, m);
//
//        em.persist(m);
//        em.flush();
//
//        return m;
//    }
//
//    @Override
//    public T update(T managed, Predicate<T> validate) {
//
//        Assert.isTrue(validate.test(managed));
//
//        EntityManager em = service.getEntityManager();
//
//        T m = em.find(clazz, managed.getId());
//        
//        handleAllProperties(managed, m);
//
//        m = em.merge(m);
//        em.flush();
//
//        handleAllChildren(managed, m);
//
//        m = em.merge(m);
//        em.flush();
//
//        return m;
//    }
//
//    @Override
//    public T remove(T managed, Predicate<T> validate) {
//
//        Assert.isTrue(validate.test(managed));
//
//        EntityManager em = service.getEntityManager();
//
//        T m = em.find(clazz, managed.getId());
//
//        em.remove(m);
//        em.flush();
//
//        return m;
//    }
//
//    
//}
