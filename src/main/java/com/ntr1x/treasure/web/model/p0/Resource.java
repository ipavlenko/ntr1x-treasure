package com.ntr1x.treasure.web.model.p0;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import javax.enterprise.util.AnnotationLiteral;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.filtering.ResourceFiltering;
import com.ntr1x.treasure.web.model.p1.Category;
import com.ntr1x.treasure.web.model.p1.Tag;
import com.ntr1x.treasure.web.model.p2.Comment;
import com.ntr1x.treasure.web.model.p2.ResourceCategory;
import com.ntr1x.treasure.web.model.p2.ResourceImage;
import com.ntr1x.treasure.web.model.p2.ResourceLike;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "resources")
@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resource {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	@ApiModelProperty(readOnly = true)
	private Long id;

	@Column(name = "Alias")
	@ApiModelProperty(readOnly = true)
	private String alias;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="aspects", joinColumns = { @JoinColumn(name = "RelateId") })
	@Column(name="Aspect")
	@CascadeOnDelete
	private List<String> aspects;
	
	@ResourceRelation
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
	private List<Tag> tags;
	
	@ResourceRelation
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
    private List<Comment> comments;
	
	@ResourceRelation
    @XmlElement
    @XmlInverseReference(mappedBy = "relate")
    @OneToMany(mappedBy = "relate")
    @CascadeOnDelete
    @ApiModelProperty(hidden = true)
    private List<ResourceImage> images;
	
	@ResourceRelation
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
    private List<ResourceCategory> categories;
	
	@ResourceRelation
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
    private List<Category> subcategories;
	
	@ResourceRelation
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
    private List<ResourceLike> likes;
	
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResourceFiltering
    public static @interface ResourceProperty {
	    
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
	    public static class Factory extends AnnotationLiteral<ResourceProperty> implements ResourceProperty {
	        
            private static final long serialVersionUID = 681781097193743580L;
	        
            public static ResourceProperty get() {
                
                return new Factory();
            }
	    }
    }
	
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ResourceFiltering
    public static @interface ResourceRelation {
	    
	    @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Factory extends AnnotationLiteral<ResourceRelation> implements ResourceRelation {
	        
            private static final long serialVersionUID = 6273053186871368162L;

            public static ResourceRelation get() {
                
                return new Factory();
            }
        }
	}
}
