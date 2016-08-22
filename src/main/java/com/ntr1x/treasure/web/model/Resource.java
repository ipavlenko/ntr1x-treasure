package com.ntr1x.treasure.web.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.glassfish.hk2.api.AnnotationLiteral;
import org.glassfish.jersey.message.filtering.EntityFiltering;

import com.ntr1x.treasure.web.reflection.ResourceProperty;
import com.ntr1x.treasure.web.reflection.ResourceProperty.Type;

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
	@ResourceProperty(create = Type.IGNORE, update = Type.IGNORE)
	private Long id;

	@Column(name = "Alias")
	@ApiModelProperty(readOnly = true)
	@ResourceProperty(create = Type.IGNORE, update = Type.IGNORE)
	private String alias;
	
	@Transient
    @ApiModelProperty(hidden = true)
    private Action _action;
	
	@TagsView
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
	@ResourceProperty(create = Type.CASCADE, update = Type.CASCADE)
	private List<Tag> tags;
	
	@CommentsView
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
	@ResourceProperty(create = Type.CASCADE, update = Type.CASCADE)
    private List<Comment> comments;
	
	@GoodsView
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
	@ResourceProperty(create = Type.CASCADE, update = Type.CASCADE)
    private List<Good> goods;
	
	@AttachmentsView
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
	@ResourceProperty(create = Type.CASCADE, update = Type.CASCADE)
    private List<Attachment> attachments;
	
	@CategoriesView
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
	@ResourceProperty(create = Type.CASCADE, update = Type.CASCADE)
    private List<ResourceCategory> categories;
	
	@SubcategoriesView
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
	@ResourceProperty(create = Type.CASCADE, update = Type.CASCADE)
    private List<Category> subcategories;
	
	@LikesView
	@XmlElement
	@XmlInverseReference(mappedBy = "relate")
	@OneToMany(mappedBy = "relate")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
	@ResourceProperty(create = Type.CASCADE, update = Type.CASCADE)
    private List<Like> likes;
	
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@EntityFiltering
	public static @interface CommentsView {
		
		public static class Factory extends AnnotationLiteral<CommentsView> implements CommentsView {
			
			private static final long serialVersionUID = 8232838804784177641L;

			public static CommentsView get() {
				return new Factory();
			}
		}
	}
	
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@EntityFiltering
	public static @interface GoodsView {
		
		public static class Factory extends AnnotationLiteral<GoodsView> implements GoodsView {
			
			private static final long serialVersionUID = 1025351524708266390L;

			public static GoodsView get() {
				return new Factory();
			}
		}
	}
	
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@EntityFiltering
	public static @interface TagsView {
		
		public static class Factory extends AnnotationLiteral<TagsView> implements TagsView {
			
			private static final long serialVersionUID = 2672639184455729764L;

			public static TagsView get() {
				return new Factory();
			}
		}
	}
	
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@EntityFiltering
	public static @interface AttachmentsView {
		
		public static class Factory extends AnnotationLiteral<AttachmentsView> implements AttachmentsView {

			private static final long serialVersionUID = 7152352904127255938L;

			public static AttachmentsView get() {
				return new Factory();
			}
		}
	}
	
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@EntityFiltering
	public static @interface CategoriesView {
		
		public static class Factory extends AnnotationLiteral<CategoriesView> implements CategoriesView {

			private static final long serialVersionUID = 8740042155448513430L;

			public static CategoriesView get() {
				return new Factory();
			}
		}
	}

	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@EntityFiltering
	public static @interface SubcategoriesView {
		
		public static class Factory extends AnnotationLiteral<SubcategoriesView> implements SubcategoriesView {
			
			private static final long serialVersionUID = -23820791446269189L;

			public static SubcategoriesView get() {
				return new Factory();
			}
		}
	}
	
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@EntityFiltering
	public static @interface LikesView {
		
		public static class Factory extends AnnotationLiteral<LikesView> implements LikesView {

			private static final long serialVersionUID = -3903982603383103237L;

			public static LikesView get() {
				return new Factory();
			}
		}
	}
}
