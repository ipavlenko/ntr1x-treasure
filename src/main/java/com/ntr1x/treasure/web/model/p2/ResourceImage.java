package com.ntr1x.treasure.web.model.p2;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p1.Image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resources_images")
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceImage extends Resource {

    @XmlElement
    @XmlInverseReference(mappedBy = "images")
    @ManyToOne
    @JoinColumn(name = "RelateId", nullable = false, updatable = false)
    private Resource relate;
    
    @XmlElement
    @ManyToOne
    @JoinColumn(name = "ImageId", nullable = false, updatable = false)
    private Image image;
}