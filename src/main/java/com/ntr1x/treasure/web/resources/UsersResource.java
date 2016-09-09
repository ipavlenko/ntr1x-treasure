package com.ntr1x.treasure.web.resources;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.Aspect;
import com.ntr1x.treasure.web.model.Cart;
import com.ntr1x.treasure.web.model.Depot;
import com.ntr1x.treasure.web.model.Method;
import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.model.User.Role;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.UserRepository;
import com.ntr1x.treasure.web.services.IParamService;
import com.ntr1x.treasure.web.services.IParamService.CreateParam;
import com.ntr1x.treasure.web.services.IParamService.UpdateParam;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Api("Users")
@Component
@Path("/ws/users")
@PermitAll
public class UsersResource {

	@Inject
	private ISecurityService security;
	
	@Inject
	private UserRepository users;
	
	@Inject
    private IParamService params;
	
//	@Inject
//	private PaymentMethodRepository payments;
//
//	@Inject
//	private DeliveryPlaceRepository stores;

    @PersistenceContext
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///users:admin" })
    public UsersResponse list(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size,
        @QueryParam("role") Role role
    ){
        Page<User> result = role == null
            ? users.findAll(new PageRequest(page, size))
            : users.findUsersByRole(role, new PageRequest(page, size))
        ;
        
        return new UsersResponse(
            result.getTotalElements(),
            page,
            size,
            result.getContent()
        );
    }

    @GET
    @Path("/i/{id}/lock")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///users/i/{id}:admin" })
    public User lock(@PathParam("id") long id) {

        User user = em.find(User.class, id);
        user.setLocked(true);
        
        em.persist(user);
        em.flush();

        return user;
    }

    @GET
    @Path("/i/{id}/unlock")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed( { "res:///users/i/{id}:admin" })
    public User unlock(@PathParam("id") long id) {

        User user = em.find(User.class, id);
        user.setLocked(false);
        
        em.persist(user);
        em.flush();

        return user;
    }

    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///users/i/{id}:admin" })
    public User select(@PathParam("id") long id) {
        
        return em.find(User.class, id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User create(CreateUser user) {
        
        User u = new User(); {
            
            int random = security.randomInt();
            
            u.setEmail(user.email);
            u.setPwdhash(security.hashPassword(random, user.email));
            u.setRandom(random);
            u.setConfirmed(user.confirmed);
            u.setRegistered(LocalDateTime.now());
            u.setPhone(user.phone);
            u.setName(user.userName);
            u.setSurname(user.surname);
            u.setMiddlename(user.middleName);
            u.setRole(user.role);
            
            em.persist(u);
            em.flush();
            
            u.setAlias(ResourceUtils.alias(null, "users/i", u));
            
            em.merge(u);
            em.flush();
            
            params.createParams(u, user.params);
            
            em.flush();
            
            if (user.places != null) {
                
                for (CreateUser.Place place : user.places) {
                    
                    Depot d = new Depot();
                    
                    d.setUser(u);
                    
                    em.persist(d);
                    em.flush();
                    
                    d.setAlias(ResourceUtils.alias(u, "deliveryplaces", d));
                    
                    em.merge(d);
                    em.flush();
                    
                    params.createParams(d, place.params);
                    
                    em.flush();
                }
            }
            
            if (user.methods != null) {
                
                for (CreateUser.Method method : user.methods) {
                    
                    Method m = new Method();
                    
                    m.setUser(u);
                    
                    em.persist(m);
                    em.flush();
                    
                    m.setAlias(ResourceUtils.alias(u, "paymentmethods", m));
                    
                    em.merge(m);
                    em.flush();
                    
                    params.createParams(m, method.params);
                    
                    em.flush();
                }
            }
            
            em.refresh(u);
        }
        
        Cart c = new Cart(); {
            
            c.setUser(u);
            
            em.persist(c);
            em.flush();
            
            c.setAlias(ResourceUtils.alias(u, "cart", c));
            
            em.merge(c);
            em.flush();
        }
        
        em.refresh(u);

        return u;
	}

	@PUT
	@Path("/i/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public User update(@PathParam("id") long id, UpdateUser user) {
	    
	    User u = em.find(User.class, id);
	    
	    int random = security.randomInt();
        
        u.setEmail(user.email);
        u.setPwdhash(security.hashPassword(random, user.email));
        u.setRandom(random);
        u.setConfirmed(user.confirmed);
        u.setPhone(user.phone);
        u.setName(user.userName);
        u.setSurname(user.surname);
        u.setMiddlename(user.middleName);
        u.setRole(user.role);
        
        em.persist(u);
        em.flush();
        
        u.setAlias(ResourceUtils.alias(null, "users", u));
        
        em.merge(u);
        em.flush();
        
        params.updateParams(u, user.params);
        
        em.flush();
        
        for (UpdateUser.Place place : user.places) {
            
            switch (place.action) {
                
                case CREATE: {
                    
                    Depot d = new Depot();
                    
                    d.setUser(u);
                    
                    em.persist(d);
                    em.flush();
                    
                    d.setAlias(ResourceUtils.alias(u, "deliveryplaces", d));
                    
                    em.merge(d);
                    em.flush();
                    
                    params.createParams(d, Arrays.stream(place.params).map(p -> new CreateParam(p.attribute, p.value)).toArray(CreateParam[]::new));
                    
                    em.flush();
                }
                case UPDATE: {
                    
                    Depot d = em.find(Depot.class, place.id);
                    
                    params.updateParams(d, place.params);
                    
                    em.flush();
                }
                case REMOVE: {
                    
                    Depot d = em.find(Depot.class, place.id);
                    
                    em.remove(d);
                    em.flush();
                }
            }
        }
        
        for (UpdateUser.Method method : user.methods) {
            
            switch (method.action) {
            
            case CREATE: {
                    
                    Method m = new Method();
                    
                    m.setUser(u);
                    
                    em.persist(m);
                    em.flush();
                    
                    m.setAlias(ResourceUtils.alias(u, "paymentmethods", m));
                    
                    em.merge(m);
                    em.flush();
                    
                    params.createParams(m, Arrays.stream(method.params).map(p -> new CreateParam(p.attribute, p.value)).toArray(CreateParam[]::new));
                    
                    em.flush();
                }
                case UPDATE: {
                    
                    Method m = em.find(Method.class, method.id);
                    
                    params.updateParams(m, method.params);
                    
                    em.flush();
                }
                case REMOVE: {
                    
                    Method m = em.find(Method.class, method.id);
                    
                    em.remove(m);
                    em.flush();
                }
                
            }
        }
        
        em.refresh(u);
	    
	    return u;
	}
	
	@DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///users/i/{id}:admin" })
    public User delete(@PathParam("id") long id) {
        
        User u = em.find(User.class, id);
        em.remove(u);
        
        return u;
    }

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
        public String middleName;
        public String surname;
        public String userName;
        public String phone;
        public String email;
        public String password;
        public Aspect type;
        public boolean confirmed;
        
        @XmlElement
        private CreateParam[] params;
        
        @XmlElement
        private Place[] places;
        
        @XmlElement
        private Method[] methods;
        
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Place {
            
            @XmlElement
            public CreateParam[] params;
        }
        
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Method {
            
            @XmlElement
            public CreateParam[] params;
        }
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUser {
        
        public Role role;
        public String middleName;
        public String surname;
        public String userName;
        public String phone;
        public String email;
        public String password;
        public Aspect type;
        public boolean confirmed;
        
        @XmlElement
        private UpdateParam[] params;
        
        @XmlElement
        private Place[] places;
        
        @XmlElement
        private Method[] methods;
        
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Place {
            
            public Long id;
            public Action action;
            
            @XmlElement
            public UpdateParam[] params;
        }
        
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Method {
            
            public Long id;
            public Action action;
            
            @XmlElement
            public UpdateParam[] params;
        }
    }
    
}
