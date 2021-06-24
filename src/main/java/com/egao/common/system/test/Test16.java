package com.egao.common.system.test;

import com.egao.common.core.utils.DateUtil;
import com.egao.common.core.utils.OrderUtil;

import java.io.File;

public class Test16 {

    public static void main(String[] args) {

        String skuStr = "哈哈_嘿嘿_68166#+奥尼女装网购_68166#+奥尼女装网购_ดำ_M";
//        String skuStr = "68027# 奥尼女装网购_68027# 奥尼女装网购_น้ำนมแอปริคอท_M";
        String sku = OrderUtil.getSku(skuStr);

        String goodsNo = OrderUtil.getGoodsNo(sku);
        System.out.println("goodsNo:"+goodsNo);

//        String supplierName = OrderUtil.getSupplierName(sku);

        int index = sku.lastIndexOf("#");
        String supplierName = sku.substring(index + 2, sku.length());
        System.out.println("supplierName:"+supplierName);










/*
        String str = "哈哈_嘿嘿_68166#+奥尼女装网购_68166#+奥尼女装网购_ดำ_M";

//        String mystring = "012.345.678.9";
        int index = str.lastIndexOf('_', str.lastIndexOf("_")-2);
        System.out.println("index："+index);
        System.out.println("index2："+str.substring(index+1));
        System.out.println("index3："+str.substring(0, index));
        String str2 = str.substring(0, index);
        System.out.println("index4："+str2);
        int index2 = str2.lastIndexOf('_', str2.lastIndexOf("_"));
        System.out.println("index5："+index2);
        System.out.println("index6："+str2.substring(index2+1));
*/



/*
        String path="/home/henry/Desktop/1.txt";

        //获得"Desktop/1.txt",并且不需要前面的"/"
        String oo=path.substring(path.lastIndexOf("/",path.lastIndexOf("/")-5)+1);
        //"+1"代表在定位时往后取一位,即去掉"/"
        //"-1"代表以"/"字符定位的位置向前取一位
        //从path.lastIndexOf("/")-1位置开始向前寻找倒数第二个"/"的位置

        System.out.println(oo);
*/




/*

        String variationName = "สีม่วง,XL";
        int index = variationName.indexOf(",");
        String colour = variationName.substring(0, index);
        String size = variationName.substring(index+1);

        System.out.println("colour："+colour);
        System.out.println("size："+size);
*/







/*
        String str = "shopeetemp - 泰国 - Yoosbuy的泰国shopee临时店 (本土)";
        //先获取最后一个  \ 所在的位置
        int index1 = str.lastIndexOf("-");
        //然后获取从最后一个\所在索引+1开始 至 字符串末尾的字符
        String ss1 = str.substring(index1+1, str.length()-4);
        System.out.println("截取最后一个："+ss1);
        System.out.println("截取最后一个2："+ss1.trim());
*/




/*
        int index=str.indexOf("-");
        index=str.indexOf("-", index+2);
        String result=str.substring(index);
        //输出结果
        System.out.println(result);
*/



    }
}
