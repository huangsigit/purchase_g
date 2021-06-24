package com.egao.common.system.test;

import com.egao.common.core.utils.DateUtil;
import com.egao.common.core.utils.OrderUtil;
import org.apache.commons.lang3.StringUtils;

import javax.smartcardio.TerminalFactory;
import java.util.TimeZone;

public class Test17 {


    public static void main(String[] args) {


        String sku = "05095#+ANNZWG_05095#+ANNZWG_สีเทาเข้ม_XXL";
        sku = OrderUtil.getSku(sku);
        System.out.println("sku:"+sku);
        String goodsNo = OrderUtil.getGoodsNo(sku);
        System.out.println("goodsNo:"+goodsNo);
        String supplierName = OrderUtil.getSupplierName(sku);
        System.out.println("supplierName:"+supplierName);



/*
        long current = System.currentTimeMillis();
        long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
        System.out.println("zero:"+zero);
        System.out.println("twelve:"+twelve);

        System.out.println("zero2:"+ DateUtil.getTodayZero(1615741250000l));
        System.out.println("twelve2:"+DateUtil.getTodayTwelve(1615741250000l));
*/









/*
        String skus = "4066+173WG_4066+173WG_Xámđen_L";
        String sku = OrderUtil.getSku(skus); // 截取符号_倒数第三个字符串
        String goodsNo = OrderUtil.getGoodsNo(sku);// 货号
        String supplierName = OrderUtil.getSupplierName(sku);
        System.out.println("goodsNo:"+goodsNo);
        System.out.println("supplierName:"+supplierName);
*/





/*
        String str = "11111,222222";
        StringUtils.substringBefore(str, ",");
        String s1 = StringUtils.substringBefore(str, ",");
        String s2 = StringUtils.substringAfterLast(str, ",");

        System.out.println("s1:"+s1);
        System.out.println("s2:"+s2);
*/


    }
}
