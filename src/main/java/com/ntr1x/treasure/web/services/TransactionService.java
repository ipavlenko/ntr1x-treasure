package com.ntr1x.treasure.web.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class TransactionService implements ITransactionService {
    
    @Override
    public void afterCommit(Runnable runnable) {
        
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            
            @Override
            public void afterCommit() {
                runnable.run();
            }
        });
    }
}
