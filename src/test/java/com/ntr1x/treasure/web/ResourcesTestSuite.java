package com.ntr1x.treasure.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ntr1x.treasure.web.resources.AccountResourceTest;
import com.ntr1x.treasure.web.resources.GoodResourceTest;
import com.ntr1x.treasure.web.resources.PublicationResourceTest;

@RunWith(Suite.class)
@SuiteClasses({
	AccountResourceTest.class,
//	PublicationResourceTest.class,
//	GoodResourceTest.class,
})
public class ResourcesTestSuite {
	
}