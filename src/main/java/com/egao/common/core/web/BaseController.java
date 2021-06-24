package com.egao.common.core.web;

import com.egao.common.core.utils.DateUtil;
import com.egao.common.system.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller基类
 * Created by wangfan on 2017-06-10 10:10
 */
public class BaseController {

    public Logger logger = Logger.getLogger(BaseController.class.getName());

    /**
     * 获取当前登录的user
     */
    public User getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) return null;
        Object object = subject.getPrincipal();
        if (object != null) return (User) object;
        return null;
    }

    public static String getType(Object a) {
        return a.getClass().toString();
    }

    /**
     * 获取当前登录的userId
     */
    public Integer getLoginUserId() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserId();
    }

    public String getStartTime(String searchTime) {
        String startTime = StringUtils.substringBefore(searchTime, " - ");
        startTime = StringUtils.isEmpty(startTime) ? DateUtil.timestampToTime(System.currentTimeMillis() - 86400000*6l, "yyyy-MM-dd") : startTime;

        return startTime;
    }

    public String getEndTime(String searchTime) {
        String endTime = StringUtils.substringAfter(searchTime, " - ");
        // 获取昨天日期
        endTime = StringUtils.isEmpty(endTime) ? DateUtil.timestampToTime(System.currentTimeMillis(), "yyyy-MM-dd") : endTime;

        return endTime;
    }

    private String getDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 获取当前日期
     */
    public String getDate() {
        return getDate("yyyy/MM/dd/");
    }

    public String getPurchaseStatusStr(Integer purchase_status){
        String purchase_status_str = "待采购";
        if(purchase_status == 1){
            purchase_status_str = "已采购";
        }else if(purchase_status == 2){
            purchase_status_str = "待拣货";
        }else if(purchase_status == 3){
            purchase_status_str = "待发货";
        }else if(purchase_status == 4){
            purchase_status_str = "已发货";
        }else if(purchase_status == 5){
            purchase_status_str = "已作废";
        }
        return purchase_status_str;
    }


}
