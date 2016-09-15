package com.ntr1x.treasure.web.model;
//package com.ntr1x.treasure.web.model;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "documents")
//@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
//public class Document extends Resource {
//
//	@XmlElement
//    @XmlInverseReference(mappedBy = "documents")
//	@ManyToOne
//	@JoinColumn(name = "RelateId")
//	private Resource relate;
//
//	@Column(name = "Title", nullable = false)
//	private String title;
//}
