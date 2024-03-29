package com.egao.common.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志
 * Created by wangfan on 2018-12-24 16:10
 */
@TableName("pur_email_record")
public class EmailRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int TYPE_LOGIN = 0;  // 访问成功
    public static final int TYPE_ERROR = 1;  // 访问失败
    public static final int TYPE_LOGOUT = 2;  // 退出登录
    public static final int TYPE_REFRESH = 3;  // 刷新token
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 操作系统
     */
    private String os;
    /**
     * 设备名
     */
    private String device;
    /**
     * 浏览器类型
     */
    private String browser;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 操作类型,0访问成功,1访问失败,2退出登录,3刷新token
     */
    private Integer operType;
    /**
     * 备注
     */
    private String comments;
    /**
     * 操作时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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


    @Override
    public String toString() {
        return "EmailRecord{" +
                ", id=" + id +
                ", os=" + os +
                ", device=" + device +
                ", browser=" + browser +
                ", ip=" + ip +
                ", operType=" + operType +
                ", comments=" + comments +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
