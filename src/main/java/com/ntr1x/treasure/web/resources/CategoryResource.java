//package com.ntr1x.treasure.web.resources;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.function.BiConsumer;
//
//import javax.annotation.security.PermitAll;
//import javax.annotation.security.RolesAllowed;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.transaction.Transactional;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Component;
//
//import com.ntr1x.treasure.web.model.Action;
//import com.ntr1x.treasure.web.model.Category;
//import com.ntr1x.treasure.web.reflection.ResourceUtils;
//import com.ntr1x.treasure.web.repository.CategoryRepository;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiModelProperty;
//import io.swagger.annotations.ApiParam;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//
//@Path("categories")
//@Api("Categories")
//@PermitAll
//@Component
//public class CategoryResource {
//
//    @Inject
//    private EntityManager em;
//    
//    @Inject
//    private CategoryRepository categories;
//    
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public List<Category> list(
//            @QueryParam("aspect") String aspect,
//            @QueryParam("relate") Long relate,
//            @QueryParam("page") @ApiParam(example = "0") int page,
//            @QueryParam("size") @ApiParam(example = "10") int size
//    ) {
//        return categories.findByRelateAndAspect(relate, aspect, new PageRequest(0, size)).getContent();
//    }
//    
//    @GET
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public Category select(@PathParam("id") long id) {
//        
//        return categories.findOne(id);
//    }
//    
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    @RolesAllowed({ "res:///categories:admin" })
//    public Category create(CategoryCreate category) {
//
//        Category persisted = new Category(); {
//            
//            persisted.setRelate(
//                category.relate == null
//                    ? null
//                    : em.find(Category.class, category.relate)
//            );
//            persisted.setTitle(category.title);
//            persisted.setDescription(category.description);
//            persisted.setAspects(Arrays.asList(category.aspects));
//            
//            em.persist(persisted);
//            em.flush();
//            
//            persisted.setAlias(ResourceUtils.alias(null, "categories", persisted));
//            
//            em.merge(persisted);
//            em.flush();
//            
//            BiConsumer<Category, CategoryCreate.Subcategory> visitor =
//            new BiConsumer<Category, CategoryCreate.Subcategory>() {
//                
//                @Override
//                public void accept(Category parent, CategoryCreate.Subcategory c) {
//                
//                    Category child = new Category(); {
//                        
//                        child.setRelate(parent);
//                        child.setTitle(c.title);
//                        child.setDescription(c.description);
//                        child.setAspects(Arrays.asList(c.aspects));
//                        
//                        em.persist(child);
//                        em.flush();
//                        
//                        child.setAlias(ResourceUtils.alias(parent, "subcategories", child));
//                        
//                        em.merge(child);
//                        em.flush();
//                        
//                        if (c.subcategories != null) {
//                            for (CategoryCreate.Subcategory s : c.subcategories) {
//                                accept(child, s);
//                            }
//                        }
//                    }
//                }
//            };
//            
//            if (category.subcategories != null) {
//                for (CategoryCreate.Subcategory s : category.subcategories) {
//                    visitor.accept(persisted, s);
//                }
//            }
//            
//            em.flush();
//        }
//        
//        return persisted;
//    }
//    
//    @PUT
//    @Path("/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    @RolesAllowed({ "res:///categories/{id}:admin" })
//    public Category update(@PathParam("id") long id, CategoryUpdate category) {
//        
//        Category persisted = em.find(Category.class, id); {
//            
//            persisted.setTitle(category.title);
//            persisted.setDescription(category.description);
//            persisted.setAspects(Arrays.asList(category.aspects));
//            
//            em.merge(persisted);
//            em.flush();
//            
//            BiConsumer<Category, CategoryUpdate.Subcategory> visitor =
//            new BiConsumer<Category, CategoryUpdate.Subcategory>() {
//                
//                @Override
//                public void accept(Category parent, CategoryUpdate.Subcategory c) {
//                
//                    switch (c._action) {
//                        case CREATE: {
//                            
//                            Category child = new Category(); {
//                                
//                                if (c.id != null) {
//                                    throw new WebApplicationException("Cannot create Subcategory with specified id", Response.Status.CONFLICT);
//                                }
//                                
//                                child.setRelate(parent);
//                                child.setTitle(c.title);
//                                child.setDescription(c.description);
//                                child.setAspects(Arrays.asList(c.aspects));
//                                
//                                em.persist(child);
//                                em.flush();
//                                
//                                child.setAlias(ResourceUtils.alias(parent, "subcategories", child));
//                                
//                                em.merge(child);
//                                em.flush();
//                                
//                                if (c.subcategories != null) {
//                                    for (CategoryUpdate.Subcategory s : c.subcategories) {
//                                        accept(child, s);
//                                    }
//                                }
//                            }
//                            break;
//                        }
//                        case UPDATE: {
//                            
//                            Category child = em.find(Category.class, c.id); {
//                                
//                                if (child.getRelate().getId() != persisted.getId()) {
//                                    throw new WebApplicationException("Subcategory belongs to another Category", Response.Status.CONFLICT);
//                                }
//                                
//                                child.setTitle(c.title);
//                                child.setDescription(c.description);
//                                child.setAspects(Arrays.asList(c.aspects));
//                                
//                                em.merge(child);
//                                em.flush();
//                                
//                                if (c.subcategories != null) {
//                                    for (CategoryUpdate.Subcategory s : c.subcategories) {
//                                        accept(child, s);
//                                    }
//                                }
//                            }
//                            break;
//                        }
//                        case REMOVE: {
//                            
//                            Category child = em.find(Category.class, c.id); {
//                                
//                                if (child.getRelate().getId() != persisted.getId()) {
//                                    throw new WebApplicationException("Subcategory belongs to another Category", Response.Status.CONFLICT);
//                                }
//                                em.remove(child);
//                                em.flush();
//                            }
//                            break;
//                        }
//                        case IGNORE:
//                        default:
//                            break;
//                    }
//                }
//            };
//            
//            if (category.subcategories != null) {
//                for (CategoryUpdate.Subcategory s : category.subcategories) {
//                    visitor.accept(persisted, s);
//                }
//            }
//            
//            em.flush();
//        };
//        
//        return persisted;
//    }
//    
//    @DELETE
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    @RolesAllowed({ "res:///categories/{id}:admin" })
//    public Category remove(@PathParam("id") long id) {
//        
//        Category persisted = em.find(Category.class, id); {
//            
//            em.remove(persisted);
//            em.flush();
//        }
//        
//        return persisted;
//    }
//    
//    @XmlRootElement
//    public static class CategoryCreate {
//        
//        public Long relate;
//        public String title;
//        public String description;
//        public String[] aspects;
//        
//        @ApiModelProperty(hidden = true)
//        @XmlElement
//        public Subcategory[] subcategories;
//        
//        @AllArgsConstructor
//        @RequiredArgsConstructor
//        public static class Subcategory {
//            
//            public String title;
//            public String description;
//            public String[] aspects;
//            
//            @XmlElement
//            public Subcategory[] subcategories;
//        }
//    }
//    
//    @XmlRootElement
//    public static class CategoryUpdate {
//        
//        public String title;
//        public String description;
//        public String[] aspects;
//        
//        @ApiModelProperty(hidden = true)
//        @XmlElement
//        public Subcategory[] subcategories;
//        
//        @AllArgsConstructor
//        @RequiredArgsConstructor
//        public static class Subcategory {
//            
//            public Long id;
//            public String title;
//            public String description;
//            public String[] aspects;
//            public Action _action;
//            
//            @XmlElement
//            public Subcategory[] subcategories;
//        }
//    }
//}
