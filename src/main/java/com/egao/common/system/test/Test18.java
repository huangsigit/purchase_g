package com.egao.common.system.test;

import com.alibaba.fastjson.JSON;
import com.egao.common.core.utils.CSVUtil;
import com.egao.common.core.utils.OrderUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test18 {


    public static void main(String[] args) {

        String sku = "2073#+ANNZWG";

/*
        System.out.println("let："+sku.length());
        System.out.println("sku："+sku.lastIndexOf("_"));
        System.out.println("sku："+sku.substring(sku.lastIndexOf("_")));

        String colour = sku.substring(sku.lastIndexOf("_",sku.lastIndexOf("_")-1)+1, sku.lastIndexOf("_"));
        System.out.println("colour："+colour);
        System.out.println("size："+sku.substring(sku.lastIndexOf("_")+1));
*/

        System.out.println("goods_no:"+ OrderUtil.getGoodsNo(sku));
        System.out.println("getSupplierName:"+ OrderUtil.getSupplierName(sku));

        String str = "2";
        System.out.println(str.length() < 2 ? "00"+str : str.length() < 3 ? "0" + str : str);



/*
        List<Map<String,Object>> lists = new ArrayList();
        for(Map<String,Object>list:lists){
            Object data = list.get("data");
            if(data.equals("111")){
                list.put("aa", 111);
            }
        }


        Map map = new HashMap<>();
        map.put("data4", "数据");
        lists.add(map);


        String str = "1.此";
        boolean numeric = isNumeric(str);
        System.out.println("numeric："+numeric);
*/


//            Test18 test = new Test18();
//            test.test(6,2);

//        ExcelUtil.readAndSortInputFile("C:\\upload\\ivipdeal\\test - 副本 (2).csv");

/*
        String path = "C:\\upload\\ivipdeal\\test01.csv";

        List<String[]> result = CSVUtil.readCsv(path);
        System.out.println("result："+ JSON.toJSON(result));
*/

/*
        String str = "Foldable Space-saving Bottle";
        System.out.println("str:"+str.toLowerCase().replace(" ", "-"));
*/

/*

        String item_group_id = "16048935921093811937732040";
        System.out.println("new BigDecimal(item_group_id)："+new BigDecimal(item_group_id));

        System.out.println("len:"+item_group_id.length());
        System.out.println("len:"+(item_group_id.length()-1));

        String start = item_group_id.substring(0, item_group_id.length() - 1);
        String end = item_group_id.substring(item_group_id.length() - 1);
        System.out.println("new BigDecimal(item_group_id)："+start+end);
*/













        /*byte a1=2;
        short a2=2;
        int a3=1;
        long a4=2L;
        System.out.println(getType(a1));
        System.out.println(getType(a2));
        System.out.println(getType(a3));
        System.out.println(getType(a4));
        System.out.println(getType(item_group_id));
*/

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

    private static String getType(Object a) {
        return a.getClass().toString();
    }

    public  void test(int row,int col){
        try {
            BufferedReader reade = new BufferedReader(new FileReader("C:\\upload\\ivipdeal\\test - 副本 (2).csv"));//换成你的文件名
            String line = null;
            int index=0;
            while((line=reade.readLine())!=null){
                String item[] = line.split("TJN");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                if(index==row-1){
                    System.out.println(item.length);
                    if(item.length>=col-1){
                        String last = item[col-1];//这就是你要的数据了
                        System.out.println(last);
                    }
                }
                //int value = Integer.parseInt(last);//如果是数值，可以转化为数值
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
