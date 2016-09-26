package com.ntr1x.treasure.web.services;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.model.p2.ResourceImage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IUserService {
    
    User create(CreateUser user);
    User update(long id, UpdateUser user);
    User remove(long id);

    UsersResponse list(int page, int size);
    
    User select(@PathParam("id") long id);
    UserItem selectItem(long id);
    UserItem selectItemByEmail(String email);

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
    public static class UserItem {

        public User user;
        public List<ResourceImage> images;
    }

    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUser {
        
        public String name;
        public String email;
        public String password;
        public boolean confirmed;
        
        @XmlElement
        public IGrantService.CreateGrant[] grants;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUser {
        
        public String name;
        public String email;
        public String password;
        public boolean confirmed;
        
        @XmlElement
        public IGrantService.UpdateGrant[] grants;
    }
}
