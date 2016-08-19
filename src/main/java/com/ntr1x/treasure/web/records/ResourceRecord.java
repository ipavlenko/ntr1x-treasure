package com.ntr1x.treasure.web.records;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.persistence.EntityManager;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.ntr1x.treasure.web.model.Managed;
import com.ntr1x.treasure.web.model.Resource;
import com.ntr1x.treasure.web.services.ResourceService;

public class ResourceRecord<T extends Resource> extends ManagedRecord<T> {

    public ResourceRecord(ResourceService service, Class<T> clazz) {
        super(service, clazz);
    }

    @Override
    public T create(T managed, Function<T, String> alias, Predicate<T> validate) {

        Assert.isTrue(validate.test(managed));

        EntityManager em = service.getEntityManager();

        T m = BeanUtils.instantiate(clazz);
        
        handleAllProperties(managed, m);

        em.persist(m);
        em.flush();

        m.setAlias(alias.apply(m));

        handleAllChildren(managed, m);

        em.persist(m);
        em.flush();

        return m;
    }

    @Override
    public T update(T managed, Predicate<T> validate) {

        Assert.isTrue(validate.test(managed));

        EntityManager em = service.getEntityManager();

        T m = em.find(clazz, managed.getId());
        
        handleAllProperties(managed, m);

        m = em.merge(m);
        em.flush();

        handleAllChildren(managed, m);

        m = em.merge(m);
        em.flush();

        return m;
    }

    @Override
    public T remove(T managed, Predicate<T> validate) {

        Assert.isTrue(validate.test(managed));

        EntityManager em = service.getEntityManager();

        T m = em.find(clazz, managed.getId());

        em.remove(m);
        em.flush();

        return m;
    }

    @Override
    protected void handleAllProperties(T source, T dest) {
        
        BeanUtils.copyProperties(source, dest,
            "id",
            "alias",
            "relate",
            "action",
            "comments",
            "goods",
            "attachments",
            "subcategories",
            "likes",
            "categories",
            "tags"
        );
    }
    
    @Override
    protected void handleAllChildren(T source, T dest) {

        handleRelatedChildren(service, source.getComments(),
            (r) -> String.format("%s/comments/%d", source == null ? "" : source.getAlias(), r.getId()),
            (r) -> r.getRelate() == source
        );

        handleRelatedChildren(service, source.getGoods(),
            (r) -> String.format("%s/goods/%d", source == null ? "" : source.getAlias(), r.getId()),
            (r) -> r.getRelate() == source
        );

        handleRelatedChildren(service, source.getAttachments(),
            (r) -> String.format("%s/attachments/%d", source == null ? "" : source.getAlias(), r.getId()),
            (r) -> r.getRelate() == source
        );

        handleRelatedChildren(service, source.getSubcategories(),
            (r) -> String.format("%s/subcategories/%d", source == null ? "" : source.getAlias(), r.getId()),
            (r) -> r.getRelate() == source
        );

        handleRelatedChildren(service, source.getLikes(),
            (r) -> String.format("%s/likes/%d", source == null ? "" : source.getAlias(), r.getId()),
            (r) -> r.getRelate() == source
        );

        handleRelatedChildren(service, source.getCategories(),
            (r) -> String.format("%s/categories/%d", source == null ? "" : source.getAlias(), r.getId()),
            (r) -> r.getRelate() == source
        );

        handleRelatedChildren(service, source.getTags(),
            (r) -> String.format("%s/tags/%d", source == null ? "" : source.getAlias(), r.getId()),
            (r) -> r.getRelate() == source
        );
    }
    
    protected <Q extends Managed> void handleRelatedChildren(ResourceService service, List<Q> children, Function<Q, String> alias, Predicate<Q> validate) {
        
        if (children != null) {
            for (Q child : children) {
                
                Assert.isTrue(validate.test(child));
                
                switch (child.getAction()) {
                case CREATE:
                    service.create(child, alias, (c) -> c.getId() == null);
                    break;
                case UPDATE:
                    service.update(child, (c) -> c.getId() != null);
                    break;
                case REMOVE:
                    service.remove(child, (c) -> c.getId() != null);
                    break;
                case IGNORE:
                    break;
                }
            }
        }
    }
}
