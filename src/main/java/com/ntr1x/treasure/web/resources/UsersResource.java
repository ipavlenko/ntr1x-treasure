package com.ntr1x.treasure.web.resources;

import java.sql.Timestamp;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.assets.DeliveryPlace;
import com.ntr1x.treasure.web.model.assets.PaymentMethod;
import com.ntr1x.treasure.web.model.purchase.CartEntity;
import com.ntr1x.treasure.web.model.purchase.ResourceType;
import com.ntr1x.treasure.web.model.purchase.StoreAction;
import com.ntr1x.treasure.web.model.security.SecurityUser;
import com.ntr1x.treasure.web.model.security.SecurityUser.Role;
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
        @QueryParam("role") String role
    ){
        Page<SecurityUser> result = role == null
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
    public SecurityUser lock(@PathParam("id") long id) {

        SecurityUser user = em.find(SecurityUser.class, id);
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
    public SecurityUser unlock(@PathParam("id") long id) {

        SecurityUser user = em.find(SecurityUser.class, id);
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
    public SecurityUser select(@PathParam("id") long id) {
        
        return em.find(SecurityUser.class, id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public SecurityUser create(CreateUser user) {

        switch (user.type) {
        case ROOT_MODERATOR:
        case ROOT_SELLER:
        case ROOT_DELIVERY:
            // continue
            break;
        default:
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        SecurityUser u = new SecurityUser(); {
            
            int random = security.randomInt();
            
            u.setEmail(user.email);
            u.setPwdhash(security.hashPassword(random, user.email));
            u.setRandom(random);
            u.setConfirmed(user.confirmed);
            u.setDate(Timestamp.valueOf(LocalDateTime.now()));
            u.setPhone(user.phone);
            u.setUserName(user.userName);
            u.setSurname(user.surname);
            u.setMiddleName(user.middleName);
            u.setRole(user.role);
            u.setResType(ResourceType.EXTENDED);
            
            em.persist(u);
            em.flush();
            
            u.setAlias(ResourceUtils.alias(null, "users/i", u));
            
            em.merge(u);
            em.flush();
            
            params.createParams(u, user.params);
            
            em.flush();
            
            for (CreateUser.Place place : user.places) {
                
                DeliveryPlace d = new DeliveryPlace();
                
                d.setResType(ResourceType.EXTENDED);
                d.setOwner(u);
                
                em.persist(d);
                em.flush();
                
                d.setAlias(ResourceUtils.alias(u, "deliveryplaces", d));
                
                em.merge(d);
                em.flush();
                
                params.createParams(d, place.params);
                
                em.flush();
            }
            
            for (CreateUser.Method method : user.methods) {
                
                PaymentMethod m = new PaymentMethod();
                
                m.setResType(ResourceType.EXTENDED);
                m.setOwner(u);
                
                em.persist(m);
                em.flush();
                
                m.setAlias(ResourceUtils.alias(u, "paymentmethods", m));
                
                em.merge(m);
                em.flush();
                
                params.createParams(m, method.params);
                
                em.flush();
            }
            
            em.refresh(u);
        }
        
        CartEntity c = new CartEntity(); {
            
            c.setUser(u);
            c.setResType(ResourceType.EXTENDED);
            
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
	public SecurityUser update(@PathParam("id") long id, UpdateUser user) {
	    
	    SecurityUser u = em.find(SecurityUser.class, id);
	    
	    int random = security.randomInt();
        
        u.setEmail(user.email);
        u.setPwdhash(security.hashPassword(random, user.email));
        u.setRandom(random);
        u.setConfirmed(user.confirmed);
        u.setPhone(user.phone);
        u.setUserName(user.userName);
        u.setSurname(user.surname);
        u.setMiddleName(user.middleName);
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
                
                case ADD: {
                    
                    DeliveryPlace d = new DeliveryPlace();
                    
                    d.setResType(ResourceType.EXTENDED);
                    d.setOwner(u);
                    
                    em.persist(d);
                    em.flush();
                    
                    d.setAlias(ResourceUtils.alias(u, "deliveryplaces", d));
                    
                    em.merge(d);
                    em.flush();
                    
                    params.createParams(d, Arrays.stream(place.params).map(p -> new CreateParam(p.attribute, p.value)).toArray(CreateParam[]::new));
                    
                    em.flush();
                }
                case UPDATE: {
                    
                    DeliveryPlace d = em.find(DeliveryPlace.class, place.id);
                    
                    params.updateParams(d, place.params);
                    
                    em.flush();
                }
                case REMOVE: {
                    
                    DeliveryPlace d = em.find(DeliveryPlace.class, place.id);
                    
                    em.remove(d);
                    em.flush();
                }
            }
        }
        
        for (UpdateUser.Method method : user.methods) {
            
            switch (method.action) {
            
            case ADD: {
                    
                    PaymentMethod m = new PaymentMethod();
                    
                    m.setResType(ResourceType.EXTENDED);
                    m.setOwner(u);
                    
                    em.persist(m);
                    em.flush();
                    
                    m.setAlias(ResourceUtils.alias(u, "paymentmethods", m));
                    
                    em.merge(m);
                    em.flush();
                    
                    params.createParams(m, Arrays.stream(method.params).map(p -> new CreateParam(p.attribute, p.value)).toArray(CreateParam[]::new));
                    
                    em.flush();
                }
                case UPDATE: {
                    
                    PaymentMethod m = em.find(PaymentMethod.class, method.id);
                    
                    params.updateParams(m, method.params);
                    
                    em.flush();
                }
                case REMOVE: {
                    
                    PaymentMethod m = em.find(PaymentMethod.class, method.id);
                    
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
    public SecurityUser delete(@PathParam("id") long id) {
        
        SecurityUser u = em.find(SecurityUser.class, id);
        
        switch(u.getResType()) {
        case ROOT_SELLER:
        case ROOT_DELIVERY:
        case ROOT_MODERATOR:
        case ROOT_USER:
            throw new IllegalArgumentException();
        default:
            // contiue
        }
        
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
        public List<SecurityUser> users;
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
        public ResourceType type;
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
        public ResourceType type;
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
            public StoreAction action;
            
            @XmlElement
            public UpdateParam[] params;
        }
        
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Method {
            
            public Long id;
            public StoreAction action;
            
            @XmlElement
            public UpdateParam[] params;
        }
    }
    
}
