package com.ntr1x.treasure.web.model.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import javax.enterprise.util.AnnotationLiteral;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.filtering.ResourceFiltering;
import com.ntr1x.treasure.web.model.assets.DocumentEntity;
import com.ntr1x.treasure.web.model.assets.Upload;
import com.ntr1x.treasure.web.model.attributes.AttributeValue;
import com.ntr1x.treasure.web.model.msg.CommentEntity;
import com.ntr1x.treasure.web.model.purchase.ResourceType;
import com.ntr1x.treasure.web.model.sociality.LikeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "security_resources")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Entity", discriminatorType = DiscriminatorType.STRING, length=20)
public class SecurityResource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	@Column(name = "Locked", nullable = false)
	private boolean locked;

	@Column(name = "Alias")
	private String alias;

	@Column(name = "Tags", nullable = false, columnDefinition = "MEDIUMTEXT")
	private String tags;

	@Column(name = "ResourceType")
	@Enumerated(EnumType.STRING)
	private ResourceType resType;

	@XmlElement
    @XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ResourceRelation
	private List<CommentEntity> comments;

	@XmlElement
    @XmlInverseReference(mappedBy = "relate")
    @OneToMany(mappedBy = "relate")
    @CascadeOnDelete
    @ResourceRelation
	private List<LikeEntity> likes;

	@XmlElement
    @XmlInverseReference(mappedBy = "relate")
    @OneToMany(mappedBy = "relate")
    @CascadeOnDelete
    @ResourceRelation
	private List<DocumentEntity> documents;

	@ManyToMany
	@JoinTable(
		name = "core_resource_upload_map",
		joinColumns = @JoinColumn(name = "ResourceId"),
		inverseJoinColumns = @JoinColumn(name = "UploadId")
	)
	@ResourceRelation
	private List<Upload> images;

	@ManyToMany
	@JoinTable(
		name = "core_resource_personal_upload_map",
		joinColumns = @JoinColumn(name="ResourceId"),
		inverseJoinColumns = @JoinColumn(name="UploadId")
	)
	@ResourceRelation
	private List<Upload> personalImages;

	@XmlElement
    @XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
    @OrderBy("attribute ASC")
	@ResourceRelation
	private List<AttributeValue> attributes;
	
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
