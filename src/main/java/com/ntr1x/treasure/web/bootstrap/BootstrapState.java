package com.ntr1x.treasure.web.bootstrap;

import com.ntr1x.treasure.web.model.p1.Category;
import com.ntr1x.treasure.web.model.p1.User;

public class BootstrapState {
    
    public Users users;
    public Directories directories;
    public Purchases purchases;
    
    public static class Directories {
        
        Category adult;
        
        Category adultUnderwearMale;
        Category adultUnderwearFemale;
        Category adultOuterwearMale;
        Category adultOuterwearFemale;
        Category adultFootwear;
        Category adultAccessories;
        Category adultSocks;
        Category adultHats;
        
        Category children;
    }
    
    public static class Users {
        
        public User admin;
        public String adminPassword;
        
        public User user;
        public String userPassword;
    }
    
    public static class Purchases {
        
    }
}
