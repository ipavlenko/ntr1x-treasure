package com.ntr1x.treasure.web.bootstrap;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.bootstrap.BootstrapCategories.Directories;
import com.ntr1x.treasure.web.bootstrap.BootstrapCategories.Localizations;
import com.ntr1x.treasure.web.bootstrap.BootstrapCategories.Specialiations;
import com.ntr1x.treasure.web.bootstrap.BootstrapUsers.Users;
import com.ntr1x.treasure.web.converter.AppConverterProvider;
import com.ntr1x.treasure.web.services.IProfilerService;

@Service
public class Bootstrap implements IBootstrap {
    
    @Inject
    private IProfilerService profiler;
    
    @Value("${app.host}")
    private String host;

    private BootstrapResults results;
    
    private Users accounts;
    
    public Specialiations specializations;
    public Directories directories;
    public Localizations localizations;

//    private List<Publication> publications;
    
    public BootstrapResults bootstrap() {
        
        WebTarget target = ClientBuilder
            .newClient()
            .register(AppConverterProvider.class)
            .register(MoxyXmlFeature.class)
            .register(MoxyJsonFeature.class)
            .register(MultiPartFeature.class)
            .target(String.format("http://%s", host))
        ;
        
        profiler.withDisabledSecurity(() -> {
            
            BootstrapUsers accounts = new BootstrapUsers();
            this.accounts = accounts.createAccounts(target);
        });
        
        results = new BootstrapResults();
        
        profiler.withCredentials(target, accounts.admin.getEmail(), accounts.adminPassword, (token) -> {

            results.adminToken = token;
            
            BootstrapCategories categories = new BootstrapCategories(this);
            
            directories = categories.createDirectories(target, token);
            specializations = categories.createSpecializatons(target, token);
            localizations = categories.createLocalizations(target, token);
            
            BootstrapPublications publications = new BootstrapPublications(this);
            
            /*this.publications = */publications.createPublications(target, token);
        });
        
        profiler.withCredentials(target, accounts.user.getEmail(), accounts.userPassword, (token) -> {
            
            results.userToken = token;
        });
        
        return results;
    }
}
