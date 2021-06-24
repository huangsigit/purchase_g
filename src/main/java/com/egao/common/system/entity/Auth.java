package com.egao.common.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 认证
 * Created by AutoGenerator on 2018-12-24 16:10
 */
@TableName("pur_auth")
public class Auth implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * url
     */
    private String url;
    /**
     * 认证标识
     */
    private String auth;
    /**
     * 同步数量
     */
    private Integer size;



    public Auth() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", auth='" + auth + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
