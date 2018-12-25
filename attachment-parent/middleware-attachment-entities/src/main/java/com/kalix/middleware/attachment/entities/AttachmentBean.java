package com.kalix.middleware.attachment.entities;

import com.kalix.framework.core.api.persistence.PersistentEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author chenyanxu
 */
@Entity
@Table(name = "middleware_attachment")
public class AttachmentBean extends PersistentEntity {
    private long mainId;                    //主表id
    private String attachmentId;            //couchdb中的附件id
    private String attachmentRev;           //couchdb中的附件版本号
    private String attachmentName;          //附件名称
    private String attachmentType;          //附件类型
    private long attachmentSize;            //附件大小
    private String attachmentPath;          //附件路径
    @JsonFormat(shape= JsonFormat.Shape.STRING ,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadDate=new Date();     //上传日期

    private String downloadStatus = "0";    //下载状态，0未下载过，1已下载过
    @Transient
    private String downloadStatusName;      //下载状态名称

    public long getMainId() {
        return mainId;
    }

    public void setMainId(long mainId) {
        this.mainId = mainId;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentRev() {
        return attachmentRev;
    }

    public void setAttachmentRev(String attachmentRev) {
        this.attachmentRev = attachmentRev;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public long getAttachmentSize() {
        return attachmentSize;
    }

    public void setAttachmentSize(long attachmentSize) {
        this.attachmentSize = attachmentSize;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public String getDownloadStatusName() {
        return downloadStatusName;
    }

    public void setDownloadStatusName(String downloadStatusName) {
        this.downloadStatusName = downloadStatusName;
    }
}
