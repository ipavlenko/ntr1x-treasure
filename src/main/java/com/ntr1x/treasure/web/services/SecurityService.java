package com.ntr1x.treasure.web.services;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;
import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.model.p2.Grant;
import com.ntr1x.treasure.web.repository.GrantRepository;
import com.ntr1x.treasure.web.utils.ConversionUtils;
import com.ntr1x.treasure.web.utils.CryptoUtils;

@Service
public class SecurityService implements ISecurityService {
    
    @Inject
    private Config config;
    
    @Inject
    private EntityManager em;
    
//    @Inject
//    private UserRepository users;
    
    @Inject
    private GrantRepository grants;
    
    private final Random random = new Random();
    
    private byte[] key;
    
    @Configuration
    public static class Config {
        
        @Value("${app.security.key}")
        public String key;
    }
    
    @PostConstruct
    private void init() {
        key = ConversionUtils.BASE64.decode(config.key);
    }
    
    @Override
    public String hashPassword(int random, String password) {
        
        if (password == null) return null;
        
        return Hashing.md5()
                .newHasher()
                .putInt(random)
                .putString(password, Charset.forName("UTF-8"))
                .hash()
                .toString()
        ;
    }

    @Override
    public int randomInt() {
        return random.nextInt();
    }
    
    @Override
    public int randomInt(int min, int max) {
        return min + random.nextInt(max - min);
    }
    
    @Override
    public byte[] encrypt(byte[] bytes) {
        
        return CryptoUtils.AES128.encrypt(key, bytes);
    }

    @Override
    public byte[] decrypt(byte[] bytes) {
        return CryptoUtils.AES128.decrypt(key, bytes);
    }
    
    @Override
    public byte[] toByteArray(SecurityToken token) {
        
        ByteBuffer buffer = ByteBuffer.allocate(17)
            .putLong(token.id)
            .putInt(token.event)
            .putInt(token.signature)
        ;
        
        buffer.rewind();
        byte[] bytes = buffer.array();
        
        byte crc2 = (byte) 1011;
        for (int i = 0; i < 16; i++) {
            crc2 |= bytes[i];
        }
        
        buffer.position(bytes.length - 1);
        buffer.put(crc2);
        return buffer.array();
    }
    
    @Override
    public String toString(SecurityToken token) {
        return ConversionUtils.BASE62.encode(encrypt(toByteArray(token)));
    }
    
    @Override
    public SecurityToken parseToken(byte[] bytes) {
        
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.rewind();
        
        long id = buffer.getLong();
        int event = buffer.getInt();
        int signature = buffer.getInt();
        byte crc = buffer.get();
        
        byte crc2 = (byte) 1011;
        for (int i = 0; i < 16; i++) {
            crc2 |= bytes[i];
        }
        
        if (crc2 != crc) throw new IllegalArgumentException();
            
        return new SecurityToken(
            id,
            event,
            signature
        );
    }
    
    @Override
    public SecurityToken parseToken(String token) {
        return parseToken(decrypt(ConversionUtils.BASE62.decode(token)));
    }
    
    @Override
    public byte[] toByteArray(SecuritySession session) {
        
        ByteBuffer buffer = ByteBuffer.allocate(13)
            .putLong(session.id)
            .putInt(session.signature)
        ;
        
        buffer.rewind();
        byte[] bytes = buffer.array();
        
        byte crc2 = (byte) 1011;
        for (int i = 0; i < 12; i++) {
            crc2 |= bytes[i];
        }
        
        buffer.position(bytes.length - 1);
        buffer.put(crc2);
        return buffer.array();
    }
    
    public String toString(SecuritySession session) {
        return ConversionUtils.BASE62.encode(encrypt(toByteArray(session)));
    }

    @Override
    public SecuritySession parseSession(byte[] bytes) {
        
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.rewind();
        
        long id = buffer.getLong();
        int sign = buffer.getInt();
        byte crc = buffer.get();
        
        byte crc2 = (byte) 1011;
        for (int i = 0; i < 12; i++) {
            crc2 |= bytes[i];
        }
        
        if (crc2 != crc) throw new IllegalArgumentException();
            
        return new SecuritySession(
            id,
            sign
        );
    }
    
    @Override
    public SecuritySession parseSession(String session) {
        return parseSession(decrypt(ConversionUtils.BASE62.decode(session)));
    }
    
    @Transactional
    @Override
    public boolean isUserInRole(User user, String resource, String action) {
        
        return grants.check(user.getId(), resource, action) > 0;
    }
    
    @Override
    public void register(Resource resource, String alias) {
        
        resource.setAlias(alias);
        
        em.merge(resource);
        em.flush();
    }
    
    @Override
    public void grant(User user, String pattern, String action) {
        
        Grant grant = new Grant(); {
            
            grant.setUser(user);
            grant.setPattern(pattern);
            grant.setAction(action);
        }
        
        em.persist(grant);
        em.flush();
        
//        grant.setAlias(ResourceUtils.alias(user, "grants/i", grant));
        
        em.merge(grant);
        em.flush();
    }
}
