//package com.ntr1x.treasure.web.records;
//
//import java.util.function.Function;
//import java.util.function.Predicate;
//
//import javax.persistence.EntityManager;
//
//import org.springframework.beans.BeanUtils;
//import org.springframework.util.Assert;
//
//import com.ntr1x.treasure.web.model.Managed;
//import com.ntr1x.treasure.web.services.ResourceService;
//
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//public class ManagedRecord<T extends Managed> implements Record<T> {
//
//    protected final ResourceService service;
//    protected final Class<T> clazz;
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
//    protected void handleAllProperties(T source, T dest) {
//        BeanUtils.copyProperties(source, dest,
//            "id",
//            "relate",
//            "action"
//        );
//    }
//    
//    protected void handleAllChildren(T source, T dest) {
//    }
//}
