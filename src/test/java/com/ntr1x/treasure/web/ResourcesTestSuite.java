package com.ntr1x.treasure.web;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ntr1x.treasure.web.endpoints.ResourceEndpointTest;
import com.ntr1x.treasure.web.resources.UserResourceTest;
import com.ntr1x.treasure.web.resources.CategoryResourceTest;
import com.ntr1x.treasure.web.resources.PublicationResourceTest;

@RunWith(Suite.class)
@SuiteClasses({
    UserResourceTest.class,
    CategoryResourceTest.class,
	PublicationResourceTest.class,
	ResourceEndpointTest.class,
})
public class ResourcesTestSuite {
    
}