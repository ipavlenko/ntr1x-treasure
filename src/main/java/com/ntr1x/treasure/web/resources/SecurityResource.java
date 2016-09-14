package com.ntr1x.treasure.web.resources;

import java.net.URI;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Cart;
import com.ntr1x.treasure.web.model.Session;
import com.ntr1x.treasure.web.model.Token;
import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.oauth.IOAuthService;
import com.ntr1x.treasure.web.oauth.IOAuthService.UserInfo;
import com.ntr1x.treasure.web.oauth.OAuth;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.UserRepository;
import com.ntr1x.treasure.web.services.ISecurityService;
import com.ntr1x.treasure.web.services.IStoreMailService;
import com.ntr1x.treasure.web.utils.ConversionUtils;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Api("Security")
@Component
@Path("/security")
public class SecurityResource {

	@PersistenceContext
	private EntityManager em;
	
	@Inject
    private ISecurityService security;

	@Inject
    private UserRepository users;
	
//	@Inject
//    private IParamService params;
	
    @Inject
    private IStoreMailService mail;

    @Inject
    private OAuth oauth;

//    @Inject
//    private ISMSService sms;

    @Inject
    private Session session;
    
//    @Inject
//    private SecurityPhoneCodeRepository phones;
	
//	@POST
//    @Path("/phone/code")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public Response code(GetCode code) throws Exception {
//
//        if (code.phone > 0){
//
//            SecurityPhoneCode phoneCode;
//
//            List<SecurityPhoneCode> result = em.createNamedQuery("SecurityPhoneCode.accessibleOfPhone", SecurityPhoneCode.class)
//                    .setParameter("phone", code.phone)
//                    .getResultList();
//
//            if (result == null || result.size() == 0) {
//                phoneCode = new SecurityPhoneCode();
//                phoneCode.setPhone(code.phone);
//                phoneCode.setCode((10000 + (int)(Math.random() * (89999))));
//
//                em.persist(phoneCode);
//                em.flush();
//            } else {
//                phoneCode = result.get(0);
//            }
//
//            List<String> phones = new LinkedList<>();
//            phones.add(String.valueOf(code.phone));
//
//            return Response.ok(
//                    sms.sendSMS(
//                            String.valueOf(phoneCode.getCode()),
//                            phones
//                    )
//            ).build();
//        }
//        
//        return Response.status(Response.Status.BAD_REQUEST).build();
//    }

    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OAuthResponse {
        
        public URI uri;
    }

    @GET
    @Path("/signup/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public OAuthResponse signupSocial(@PathParam("name") String name) throws Exception {

        IOAuthService service = oauth.service(name);

        return new OAuthResponse(
            service.auth(
                service.token().secret()
            )
        );
    }

    @GET
    @Path("/signup/{name}/confirm")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserInfo signupSocialConfirm(
        @PathParam("name") String name,
        @QueryParam("code") String code,
        @QueryParam("state") String state
    ) throws Exception {

        IOAuthService service = oauth.service(name);

        return service.userinfo(
            service.token(state, code)
        );
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User signup(Signup signup) throws Exception {
        
//        SecurityPhoneCode code = phones.findByPhoneAndCode(signup.phone, signup.code);
//
//        if (code == null) {
//            throw new WebApplicationException(Response.Status.BAD_REQUEST);
//        }

        User u = new User(); {
            
            int random = security.randomInt();
            
            u.setEmail(signup.email);
            u.setPwdhash(security.hashPassword(random, signup.password));
            u.setRandom(random);
            u.setConfirmed(false);
            u.setRegistered(LocalDateTime.now());
            u.setPhone(String.valueOf(signup.phone));
            u.setName(signup.name);
            u.setSurname(signup.surname);
            u.setMiddlename(signup.middleName);
            u.setRole(User.Role.USER);
            
            em.persist(u);
            em.flush();
            
            u.setAlias(ResourceUtils.alias(null, "users/i", u));
            
            em.merge(u);
            em.flush();
            
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

        Token token =  new Token();
        token.setUser(u);
        token.setType(Token.SIGNUP);
        token.setToken(security.randomInt());

        em.persist(token);
        em.flush();

        String key = ConversionUtils.BASE62.encode(
            security.encrypt(
                security.toByteArray(
                    new ISecurityService.SecurityToken(
                        token.getId(),
                        token.getType(),
                        token.getToken()
                    )
                )
            )
        );

        Map<String, Object> map = new HashMap<>(); {

            map.put("key", key);
            if (signup.token != null) {
                map.put("token", signup.token);
            }
        }

        mail.sendSignupConfirmation(
            u.getEmail(),
            map
        );

        return u;
    }

    @GET
    @Path("/signup/confirm/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public SigninResponse doSignupConfirm(@PathParam("key") String key) {
            
        ISecurityService.SecurityToken st = security.parseToken(
            security.decrypt(
                ConversionUtils.BASE62.decode(key)
            )
        );
        
        Token token = em.find(Token.class, st.id);
        
        User user = token != null && token.getToken() == st.getSignature()
            ? token.getUser()
            : null
        ;
            
        if (user != null){
            user.setConfirmed(true);
            em.merge(user);
        }

        em.remove(token);

        Validate.isTrue(!user.isLocked());

        Session session = new Session(); {
            session.setUser(user);
            session.setSignature(security.randomInt());
        }
        
        em.persist(session);
        em.flush();
        
        String value = ConversionUtils.BASE62.encode(
            security.encrypt(
                security.toByteArray(
                    new ISecurityService.SecuritySession(
                        session.getId(),
                        session.getSignature()
                    )
                )
            )
        );

        return new SigninResponse(user, value);
    }

    @POST
    @Path("/signout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public SignoutResponse doSignout(Signout signout) {
        
        Session s = em.find(Session.class, session.getId());
        em.remove(s);
        em.flush();
        
        return new SignoutResponse();
    }
    
    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public SigninResponse doSignin(Signin signin) {
            
        if (!EmailValidator.getInstance().isValid(signin.email)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        User user = users.findByEmail(signin.email);

        if (user == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        try {
            
            Validate.isTrue(user.getPwdhash().equals(security.hashPassword(user.getRandom(), signin.password)));
            Validate.isTrue(!user.isLocked());
            Validate.isTrue(user.isConfirmed());

            Session session = new Session();
            session.setUser(user);
            session.setSignature(security.randomInt());

            em.persist(session);
            em.flush();


            String value = ConversionUtils.BASE62.encode(
                security.encrypt(
                    security.toByteArray(
                        new ISecurityService.SecuritySession(
                            session.getId(),
                            session.getSignature()
                        )
                    )
                )
            );
            
            return new SigninResponse(user, value);
            
        } catch (Exception e){
            
            throw new WebApplicationException(e, Response.Status.UNAUTHORIZED);
        }
    }

    @POST
    @Path("/current")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public CurrentResponse current(){

        return session == null
            ? null
            : new CurrentResponse(session.getUser())
        ;
    }
    
    @XmlRootElement
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SigninResponse {
        
        @XmlElement
        public User user;
        
        public String token;
    }
    
    @XmlRootElement
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignoutResponse {
        
        @XmlElement
        public User user;
        
        public String token;
    }

    @XmlRootElement
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurrentResponse {
        
        @XmlElement
        public User user;
    }

    @XmlRootElement
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetCode {
        
        public long phone;
    }

    @XmlRootElement
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Signup {
        
        public Date date;
        public String email;
        public String password;
        public String confirmation;
        public int code;
        public String name;
        public String surname;
        public String middleName;
        public long phone;
        public String token;
    }

    @XmlRootElement
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Signin {
        
        public String email;
        public String password;
    }
    
    @XmlRootElement
    public static class Signout {
    }
}
