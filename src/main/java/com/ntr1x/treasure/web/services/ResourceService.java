package com.ntr1x.treasure.web.services;

import java.util.function.Function;
import java.util.function.Predicate;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.Account;
import com.ntr1x.treasure.web.model.Managed;
import com.ntr1x.treasure.web.model.Resource;
import com.ntr1x.treasure.web.records.AccountRecord;
import com.ntr1x.treasure.web.records.ManagedRecord;
import com.ntr1x.treasure.web.records.Record;
import com.ntr1x.treasure.web.records.ResourceRecord;

@Service
public class ResourceService {

    @Inject
    private EntityManager em;

    public EntityManager getEntityManager() {
        return this.em;
    }

    public <T extends Managed> T create(T managed, Function<T, String> alias, Predicate<T> validate) {

        Record<T> record = getRecord(managed);
        return record.create(managed, alias, validate);
    }

    public <T extends Managed> T update(T managed, Predicate<T> validate) {

        Record<T> record = getRecord(managed);
        return record.update(managed, validate);
    }

    public final <T extends Managed> T remove(T managed, Predicate<T> validate) {

        Record<T> record = getRecord(managed);
        return record.remove(managed, validate);
    }

    @SuppressWarnings("unchecked")
    private <T extends Managed> Record<T> getRecord(T managed) {
        
        if (Account.class.isAssignableFrom(managed.getClass())) {
            return (Record<T>) new AccountRecord(this);
        }
        
        if (Resource.class.isAssignableFrom(managed.getClass())) {
            return (Record<T>) new ResourceRecord<>(this, (Class<Resource>) managed.getClass());
        }    
        
        return (Record<T>) new ManagedRecord<>(this, managed.getClass());
    }
}
