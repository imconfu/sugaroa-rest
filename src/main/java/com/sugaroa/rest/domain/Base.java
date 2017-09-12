package com.sugaroa.rest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.bouncycastle.asn1.cmp.ProtectedPart;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)    //null值不返回
public class Base {
    @Id
    @GeneratedValue
    protected Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", columnDefinition = "timestamp default CURRENT_TIMESTAMP", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", columnDefinition = "timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;

    protected Boolean deleted = Boolean.TRUE;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}