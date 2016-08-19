package com.ntr1x.treasure.web.records;

import org.springframework.beans.BeanUtils;

import com.ntr1x.treasure.web.model.Account;
import com.ntr1x.treasure.web.services.ResourceService;

public class AccountRecord extends ResourceRecord<Account> {

    public AccountRecord(ResourceService service) {
        super(service, Account.class);
    }
    
    @Override
    protected void handleAllProperties(Account source, Account dest) {
        
        BeanUtils.copyProperties(source, dest,
            "id",
            "alias",
            "relate",
            "password",
            "action",
            "comments",
            "goods",
            "attachments",
            "subcategories",
            "likes",
            "categories",
            "tags",
            "sessions"
        );
    }
}
