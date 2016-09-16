package com.ntr1x.treasure.web.bootstrap;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.p1.Attribute;
import com.ntr1x.treasure.web.model.p1.Category;
import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.model.p2.Method;
import com.ntr1x.treasure.web.model.p2.Provider;
import com.ntr1x.treasure.web.model.p2.Purchase;

public class BootstrapState {
    
    public Users users;
    public Directories directories;
    public Attributes attributes;
    public Purchases purchases;
    public Sessions sessions;
    public Methods methods;
    public Providers providers;
    
    @XmlRootElement
    public static class Directories {
        
        public Category adult;
        
        public Category adultUnderwearMale;
        public Category adultUnderwearFemale;
        public Category adultOuterwearMale;
        public Category adultOuterwearFemale;
        public Category adultFootwear;
        public Category adultAccessories;
        public Category adultSocks;
        public Category adultHats;
        
        public Category children;
    }
    
    @XmlRootElement
    public static class Attributes {
        public Attribute goodBrand;
        public Attribute modificationSize;
        public Attribute modificationColor;
    }
    
    @XmlRootElement
    public static class Users {
        
        public User admin;
        public String adminPassword;
        
        public User user1;
        public String user1Password;
        
        public User user2;
        public String user2Password;
        
        public User seller1;
        public String seller1Password;
        
        public User seller2;
        public String seller2Password;
    }
    
    @XmlRootElement
    public static class Sessions {
        
        public String admin;
        public String user1;
        public String user2;
        public String seller1;
        public String seller2;
    }
    
    @XmlRootElement
    public static class Methods {
        
        public Method seller1MethodCash;
        public Method seller1MethodCard;
        
        public Method seller2MethodCash;
        public Method seller2MethodCard;
    }
    
    @XmlRootElement
    public static class Providers {
        
        public Provider seller1China;
        public Provider seller1India;
        
        public Provider seller2China;
        public Provider seller2Japan;
    }
    
    @XmlRootElement
    public static class Purchases {

        public Purchase seller1Purchase1;
        public Purchase seller1Purchase2;
        public Purchase seller1Purchase3;
        public Purchase seller2Purchase1;
        public Purchase seller2Purchase2;
        public Purchase seller2Purchase3;
    }
}
