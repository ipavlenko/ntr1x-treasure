package com.ntr1x.treasure.web.services;

public interface ITransactionService {

    void afterCommit(Runnable runnable);
}
