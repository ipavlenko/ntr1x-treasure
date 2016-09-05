package com.ntr1x.treasure.web.model.assets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_upload")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Upload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Uuid", nullable = false)
    private String uuid;

    @Column(name = "Title", nullable = false)
    private String title;

	@Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false)
    private Type uplType;

	public static enum Type {
	    
		COVER("Обложка"),
		IMAGE("Картинка"),
		COLOR("Картинка цвета"),
		LOGO("Лого"),
		AVATAR("Аватар"),
		BANNER("Баннер");

	    @Getter
		private final String caption;
		
		private Type(final String caption) {
			this.caption = caption;
		}
	}

//    public enum Type {
//        IMAGE(0, "Картинка"),
//        LOGO(1, "Лого"),
//        AVATAR(2, "Аватар"),
//        PROMO_IMAGE(3, "Промо картинка"),
//        BANNER(4, "Баннер");
//
//        private static final Map<Integer, Type> $CODE_LOOKUP = new HashMap<>();
//
//        static {
//            for (Type status : Type.values()) {
//                $CODE_LOOKUP.put(status.code, status);
//            }
//        }
//
//        private final int code;
//        private final String caption;
//
//        private Type(final int code, final String caption) {
//            this.code = code;
//            this.caption = caption;
//        }
//
//        public int getCode() {
//            return this.code;
//        }
//
//        public String getCaption() {
//            return this.caption;
//        }
//
//        public static Type findByCode(final int code) {
//
//            if ($CODE_LOOKUP.containsKey(code)) {
//                return $CODE_LOOKUP.get(code);
//            }
//
//            throw new java.lang.IllegalArgumentException(java.lang.String.format("Enumeration \'Status\' has no value \'%s\'", code));
//        }
//
//    }

}
