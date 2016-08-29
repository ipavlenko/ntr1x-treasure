package com.ntr1x.treasure.web.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import javax.enterprise.util.AnnotationLiteral;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.glassfish.jersey.message.filtering.EntityFiltering;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
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
	
	@ResourceRelations
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
	private List<Tag> tags;
	
	@ResourceRelations
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
    private List<Comment> comments;
	
	@ResourceRelations
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
    private List<Good> goods;
	
	@ResourceRelations
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
    private List<Attachment> attachments;
	
	@ResourceRelations
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
    private List<ResourceCategory> categories;
	
	@ResourceRelations
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
    private List<Category> subcategories;
	
	@ResourceRelations
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
    private List<Like> likes;
	
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @EntityFiltering
    public static @interface ResourceRelations {
        
        public static class Factory extends AnnotationLiteral<ResourceRelations> implements ResourceRelations {
            
            private static final long serialVersionUID = 8232838804784177641L;

            public static ResourceRelations get() {
                return new Factory();
            }
        }
    }
}
