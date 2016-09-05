package com.ntr1x.treasure.web.bootstrap;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.bootstrap.BootstrapUsers.Users;
import com.ntr1x.treasure.web.services.IProfilerService;

@Service
public class Bootstrap implements IBootstrap {
    
    @Inject
    private IProfilerService profiler;

    @Inject
    private BootstrapUsers bootstrapUsers;

    private Users users;
    
//    private Accounts accounts;
//    
//    public Specialiations specializations;
//    public Directories directories;
//    public Localizations localizations;

//    private List<Publication> publications;
    
    public BootstrapResults bootstrap() {
        
        WebTarget target = ClientBuilder
            .newClient()
            .target(String.format("http://localhost:%d", 8080))
        ;
        
        profiler.withDisabledSecurity(() -> {
            
            users = bootstrapUsers.createUsers(target);
        });
        
//        results = new BootstrapResults();
        
//        profiler.withCredentials(target, accounts.admin.getEmail(), accounts.adminPassword, (token) -> {
//
//            results.adminToken = token;
//            
//            BootstrapCategories categories = new BootstrapCategories(this);
//            
//            directories = categories.createDirectories(target, token);
//            specializations = categories.createSpecializatons(target, token);
//            localizations = categories.createLocalizations(target, token);
//            
//            BootstrapPublications publications = new BootstrapPublications(this);
//            
//            /*this.publications = */publications.createPublications(target, token);
//        });
//        
//        profiler.withCredentials(target, accounts.user.getEmail(), accounts.userPassword, (token) -> {
//            
//            results.userToken = token;
//        });
        
        return new BootstrapResults();
    }
}
