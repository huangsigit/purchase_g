package com.egao.common.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户
 * Created by AutoGenerator on 2018-12-24 16:10
 */
@TableName("sys_user")
public class User implements Serializable {
    private static final long serialVersionUID = 242146703513492331L;
    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;
    /**
     * 客户编号
     */
    private String customerId;

    /**
     * 工号
     */
    private String jobNumber;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 二维密码
     */
    private String twoPassword;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 邮箱是否验证,0否,1是
     */
    private Integer emailVerified;
    /**
     * 真实姓名
     */
    private String trueName;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 个人简介
     */
    private String introduction;
    /**
     * 机构id
     */
    private Integer organizationId;
    /**
     * 状态，0正常，1冻结
     */
    private Integer state;
    /**
     * 状态，0管理员，1普通用户
     */
    private Integer type;
    /**
     * 注册时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 是否删除,0否,1是
     */
    @TableLogic
    private Integer deleted;
    /**
     * 权限列表
     */
    @TableField(exist = false)
    private List<String> authorities;
    /**
     * 角色列表
     */
    @TableField(exist = false)
    private List<Role> roles;
    /**
     * 角色id
     */
    @TableField(exist = false)
    private List<Integer> roleIds;
    /**
     * 机构名称
     */
    @TableField(exist = false)
    private String organizationName;
    /**
     * 性别名称
     */
    @TableField(exist = false)
    private String sexName;
    /**
     * 分组状态
     */
    @TableField(exist = false)
    private Integer groupStatus;

    /**
     * 服务费
     */
    private BigDecimal serviceCharge;
    /**
     * 最低开卡限额
     */
    private BigDecimal minOpenCardLimit;
    /**
     * 开卡费用
     */
    private BigDecimal openCardCharge;


/*
    */
/**
     * 分组状态
     *//*

    @TableField(exist = false)
    private Float serviceCharge;
*/

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Integer emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Integer getGroupStatus() {
//        groupStatus == null ? 0 : groupStatus;
        return groupStatus == null ? 0 : groupStatus;
    }

    public void setGroupStatus(Integer groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTwoPassword() {
        return twoPassword;
    }

    public void setTwoPassword(String twoPassword) {
        this.twoPassword = twoPassword;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getMinOpenCardLimit() {
        return minOpenCardLimit;
    }

    public void setMinOpenCardLimit(BigDecimal minOpenCardLimit) {
        this.minOpenCardLimit = minOpenCardLimit;
    }

    public BigDecimal getOpenCardCharge() {
        return openCardCharge;
    }

    public void setOpenCardCharge(BigDecimal openCardCharge) {
        this.openCardCharge = openCardCharge;
    }

    @Override
    public String toString() {
        return "User{" +
                ", userId=" + userId +
                ", jobNumber=" + jobNumber +
                ", username=" + username +
                ", password=" + password +
                ", nickName=" + nickName +
                ", avatar=" + avatar +
                ", sex=" + sex +
                ", phone=" + phone +
                ", email=" + email +
                ", emailVerified=" + emailVerified +
                ", trueName=" + trueName +
                ", idCard=" + idCard +
                ", birthday=" + birthday +
                ", introduction=" + introduction +
                ", organizationId=" + organizationId +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                ", organizationName=" + organizationName +
                "}";
    }

}
