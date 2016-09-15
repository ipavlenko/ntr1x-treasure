package com.ntr1x.treasure.web.services;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Aspect;
import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.model.p1.User.Role;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IUserService {
    
    User create(CreateUser user);
    User update(long id, UpdateUser user);
    User remove(long id);
    
    UsersResponse list(int page, int size, Role role);
    
    User select(@PathParam("id") long id);
    
    @XmlRootElement
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UsersResponse {
        
        public long count;
        public int page;
        public int size;
        
        @XmlElement
        public List<User> users;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUser {
        
        public Role role;
        public String name;
        public String surname;
        public String middlename;
        public String phone;
        public String email;
        public String password;
        public Aspect type;
        public boolean confirmed;
        
        @XmlElement
        public IAttributeService.CreateAttribute[] attributes;
        
        @XmlElement
        public IGrantService.CreateGrant[] grants;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUser {
        
        public Role role;
        public String name;
        public String surname;
        public String middlename;
        public String phone;
        public String email;
        public String password;
        public Aspect type;
        public boolean confirmed;
        
        @XmlElement
        public IAttributeService.UpdateAttribute[] attributes;
        
        @XmlElement
        public IGrantService.UpdateGrant[] grants;
    }
}
