package com.ntr1x.treasure.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ntr1x.treasure.web.socket.ResourceSocketTest;

@RunWith(Suite.class)
@SuiteClasses({
    ResourceSocketTest.class,
//	AccountResourceTest.class,
//	PublicationResourceTest.class,
//	GoodResourceTest.class,
})
public class ResourcesTestSuite {
    
}