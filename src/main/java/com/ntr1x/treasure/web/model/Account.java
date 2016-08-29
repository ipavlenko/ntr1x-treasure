package com.ntr1x.treasure.web.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
@Table(
	name = "accounts",
	indexes= {
		@Index(columnList = "Email", unique = true),
	}
)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Account extends Resource {
	
	@Column(name = "Email")
	private String email;

	@XmlTransient
	@Column(name = "Random")
	@ApiModelProperty(hidden = true)
	private int random;
	
	@XmlTransient
	@Column(name = "Pwdhash")
	@ApiModelProperty(hidden = true)
	private String pwdhash;
	
	@SessionsView
	@XmlElement
	@XmlInverseReference(mappedBy = "account")
	@OneToMany(mappedBy = "account")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
    @ResourceProperty(create = Type.IGNORE, update = Type.IGNORE)
	private List<Session> sessions;
	
	@GrantsView
    @XmlElement
    @XmlInverseReference(mappedBy = "account")
    @OneToMany(mappedBy = "account")
    @CascadeOnDelete
    @ApiModelProperty(hidden = true)
    @ResourceProperty(create = Type.IGNORE, update = Type.IGNORE)
    private List<Grant> grants;
	
	@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@EntityFiltering
	public static @interface SessionsView {
		
		public static class Factory extends AnnotationLiteral<SessionsView> implements SessionsView {
			
			private static final long serialVersionUID = -971363300488244186L;

			public static SessionsView get() {
				return new Factory();
			}
		}
	}
	
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @EntityFiltering
    public static @interface GrantsView {
        
        public static class Factory extends AnnotationLiteral<GrantsView> implements GrantsView {
            
            private static final long serialVersionUID = -1701397280023104688L;

            public static GrantsView get() {
                return new Factory();
            }
        }
    }
}
