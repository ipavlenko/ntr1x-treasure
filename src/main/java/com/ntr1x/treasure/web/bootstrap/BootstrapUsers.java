package com.ntr1x.treasure.web.bootstrap;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.bootstrap.BootstrapState.Users;
import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.model.p1.User.Role;
import com.ntr1x.treasure.web.services.IGrantService;
import com.ntr1x.treasure.web.services.IProfilerService;
import com.ntr1x.treasure.web.services.IUserService;

@Service
public class BootstrapUsers {
    
    @Inject
    private IProfilerService profiler;
    
    public Users createUsers(WebTarget target) {
        
        Users users = new Users();
        
        profiler.withDisabledSecurity(() -> {
            
            {    
                IUserService.CreateUser u = new IUserService.CreateUser(); {
                    
                    u.role = Role.ADMIN;
                    u.confirmed = true;
                    u.email = "admin@example.com";
                    u.password = "admin";
                    u.phone = "80001230001";
                    u.surname = "Эникеев";
                    u.name = "Валентин";
                    u.middlename = "Петрович";
                    u.confirmed = true;
                    u.grants = new IGrantService.CreateGrant[] {
                        new IGrantService.CreateGrant("/", "admin")
                    };
                }
                
                users.admin = target
                  .path("/users")
                  .request(MediaType.APPLICATION_JSON_TYPE)
                  .post(Entity.entity(u, MediaType.APPLICATION_JSON_TYPE), User.class)
                ;
                
                users.adminPassword = u.password;
            }
            
            {    
                IUserService.CreateUser u = new IUserService.CreateUser(); {
                    
                    u.role = Role.USER;
                    u.confirmed = true;
                    u.email = "user1@example.com";
                    u.password = "user1";
                    u.phone = "89991230001";
                    u.surname = "Простой";
                    u.name = "Андрей";
                    u.middlename = "Михайлович";
                    u.confirmed = true;
                }
                
                users.user1 = target
                  .path("/users")
                  .request(MediaType.APPLICATION_JSON_TYPE)
                  .post(Entity.entity(u, MediaType.APPLICATION_JSON_TYPE), User.class)
                ;
                
                users.user1Password = u.password;
            }
            
            {    
                IUserService.CreateUser u = new IUserService.CreateUser(); {
                    
                    u.role = Role.USER;
                    u.confirmed = true;
                    u.email = "user2@example.com";
                    u.password = "user2";
                    u.phone = "89991230002";
                    u.surname = "Людина";
                    u.name = "Вероника";
                    u.middlename = "Сергеевна";
                    u.confirmed = true;
                }
                
                users.user2 = target
                  .path("/users")
                  .request(MediaType.APPLICATION_JSON_TYPE)
                  .post(Entity.entity(u, MediaType.APPLICATION_JSON_TYPE), User.class)
                ;
                
                users.user2Password = u.password;
            }
            
            {    
                IUserService.CreateUser u = new IUserService.CreateUser(); {
                    
                    u.role = Role.SELLER;
                    u.confirmed = true;
                    u.email = "seller1@example.com";
                    u.password = "seller1";
                    u.phone = "81111230001";
                    u.surname = "Продавцов";
                    u.name = "Пётр";
                    u.middlename = "Валентинович";
                    u.confirmed = true;
                }
                
                users.seller1 = target
                  .path("/users")
                  .request(MediaType.APPLICATION_JSON_TYPE)
                  .post(Entity.entity(u, MediaType.APPLICATION_JSON_TYPE), User.class)
                ;
                
                users.seller1Password = u.password;
            }
            
            {    
                IUserService.CreateUser u = new IUserService.CreateUser(); {
                    
                    u.role = Role.SELLER;
                    u.confirmed = true;
                    u.email = "seller2@example.com";
                    u.password = "seller2";
                    u.phone = "81111230002";
                    u.surname = "Торгаш";
                    u.name = "Юлия";
                    u.middlename = "Михайловна";
                    u.confirmed = true;
                }
                
                users.seller2 = target
                  .path("/users")
                  .request(MediaType.APPLICATION_JSON_TYPE)
                  .post(Entity.entity(u, MediaType.APPLICATION_JSON_TYPE), User.class)
                ;
                
                users.seller2Password = u.password;
            }
            
        });
        
        return users;
    }
}
