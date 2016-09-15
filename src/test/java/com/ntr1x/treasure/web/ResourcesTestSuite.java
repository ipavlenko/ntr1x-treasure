package com.ntr1x.treasure.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ntr1x.treasure.web.resources.CategoryResourceTest;
import com.ntr1x.treasure.web.resources.MethodResourceTest;
import com.ntr1x.treasure.web.resources.ProviderResourceTest;
import com.ntr1x.treasure.web.resources.PurchaseResourceTest;
import com.ntr1x.treasure.web.resources.UserResourceTest;

@RunWith(Suite.class)
@SuiteClasses({
    UserResourceTest.class,
    CategoryResourceTest.class,
    ProviderResourceTest.class,
    MethodResourceTest.class,
    PurchaseResourceTest.class
})
public class ResourcesTestSuite {
    
}