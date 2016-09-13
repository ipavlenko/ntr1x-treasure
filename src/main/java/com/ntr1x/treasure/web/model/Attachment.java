//package com.ntr1x.treasure.web.model;
//
//import java.util.UUID;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Index;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.PrimaryKeyJoinColumn;
//import javax.persistence.Table;
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import org.eclipse.persistence.annotations.CascadeOnDelete;
//import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Entity
//@Table(
//	name = "attachments",
//	indexes= {
//		@Index(columnList = "Dir, Path", unique = true),
//	}
//)
//@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
//@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
//@CascadeOnDelete
//public class Attachment extends Resource {
//
//	@XmlElement
//	@XmlInverseReference(mappedBy = "attachments")
//	@ManyToOne
//	@JoinColumn(name = "RelateId", nullable = false, updatable = false)
//	private Resource relate;
//	
//	@Column(name = "Uuid")
//	private UUID uuid;
//	
//	@Column(name = "Original")
//	private String original;
//	
//	@Column(name = "Dir")
//	private String dir;
//	
//	@Column(name = "Path")
//	private String path;
//}
