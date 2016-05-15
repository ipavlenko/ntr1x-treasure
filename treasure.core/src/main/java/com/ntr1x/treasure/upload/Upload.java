//package com.ntr1x.treasure.upload;
//
//import com.ntr1x.treasure.files.FileService;
//import com.ntr1x.treasure.images.ImageService;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.apache.commons.io.FileUtils;
//import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.PostPersist;
//import javax.persistence.PostRemove;
//import javax.persistence.PostUpdate;
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//import javax.xml.bind.annotation.XmlTransient;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.Serializable;
//import java.util.UUID;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "media_upload")
//public class Upload implements Serializable {
//
//    private static final long serialVersionUID = -6407837225825038334L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "Id")
//    public long id;
//
//    @Column(name = "Name", nullable = false)
//    public String name;
//
//    @Column(name = "Directory", nullable = false)
//    public String dir;
//
//    @Column(name = "path", nullable = false)
//    public String path;
//
//    @Transient
//    @XmlTransient
//    private String temp;
//
//    @Transient
//    @XmlTransient
//    private InputStream stream;
//
//    @Transient
//    @XmlTransient
//    private FormDataContentDisposition previewHeader;
//
//    @Transient
//    @XmlTransient
//    private FileService files;
//
//    @Transient
//    @XmlTransient
//    private ImageService images;
//
//    public Upload(InputStream previewInput, FormDataContentDisposition previewHeader, FileService files, ImageService images) {
//
//        this.stream = previewInput;
//        this.previewHeader = previewHeader;
//
//        this.images = images;
//        this.files = files;
//    }
//
//    @PreUpdate
//    @PrePersist
//    public void prePersist() {
//        String filename = UUID.randomUUID().toString();
//        this.path = String.format("%s.%s", filename, "png");
//        this.name = this.previewHeader.getFileName();
//        this.dir = "media/thumbnails";
//    }
//
//    @PostUpdate
//    @PostPersist
//    public void postPersist() {
//        // Копирование файла из inputstream-а туда, куда надо
//        File dir = files.resolve(String.format("catalog/media/%s", this.id));
//        dir.mkdirs();
//
//        String previewName = previewHeader.getFileName();
//        if (previewName != null && !previewName.isEmpty()) {
//
//            try (OutputStream previewOutput = new FileOutputStream(new File(dir, "preview.png"))) {
//
//                images.fitToWidth(this.stream, previewOutput, null, "png");
//
//            } catch (IOException e) {
//
//                throw new IllegalStateException(e);
//            }
//        }
//    }
//
//    @PostRemove
//    public void postRemove() {
//        // Удалить файл
//        FileUtils.deleteQuietly(files.resolve(String.format("catalog/media/%s", this.id)));
//    }
//
//}
