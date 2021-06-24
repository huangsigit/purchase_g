package com.egao.common.system.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egao.common.core.Constants;
import com.egao.common.core.utils.HttpUtil;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

// FB广告
public class Test15 {

    public static String GRAPH_URL = "https://graph.facebook.com/v7.0/";

    public static String ACCESS_TOKEN = "EAAH92JtasVMBAJ2iHbMXEdLwzMZAH2PidkMGwvQbhFZCZAAcPmUHOxfwaPfNg4M3vXCBonOVZAHLIrj7gdZCJqT9pQs8CAMGrBp7ECuNKOdFIO5txnP3UylNAI959oXBqp1hZAJloEBqSvVdt3hVhXYDu7WGdoZCgZCqrqX0PVE5LKKdGtlzQMxZBmrY8YWjQARUZD";

    public static String BUSINESS_ID = "144436283227029";

    private boolean environment = Constants.DEVELOPMENT_ENVIRONMENT;


    public static void main(String[] args) throws Exception {


        String serverURL = "https://api-cn.ezsale.com/ezsale-order/seller-outbound-order/page/outboundOrder";
        StringBuffer sbf = new StringBuffer();
        String strRead = null;
        URL url = new URL(serverURL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");//请求post方式
        connection.setDoInput(true);
        connection.setDoOutput(true);
        //header内的的参数在这里set。||connection.setRequestProperty("健, "值");
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//        connection.setRequestProperty("Authorization", "Bearer 59e0-9fcc-c3faea0e2a6c");
        connection.setRequestProperty("ezsale-auth", "bearer eyJ0eXAiOiJKc29uV2ViVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJpc3N1c2VyIiwiYXVkIjoiYXVkaWVuY2UiLCJ0ZW5hbnRfaWQiOiIwMDAwMDAwMSIsInJvbGVfbmFtZSI6IiIsInVzZXJfdHlwZSI6MSwidXNlcl9pZCI6MTM0OTczOTU5ODUwNjQxNDA5Miwic3lzdGVtX3R5cGUiOjIsInJvbGVfaWQiOiIiLCJ1c2VyX25hbWUiOiJZb29zYnV5IiwidG9rZW5fdHlwZSI6ImFjY2Vzc190b2tlbiIsImFjY291bnQiOiJZb29zYnV5IiwiY2xpZW50X2lkIjoiMjAyMTAxMTIxMjI0NTI2MjE2IiwiZXhwIjoxNjE2MTg3NzIzLCJuYmYiOjE2MTUxOTU3MjN9.ZzElFoTZOcf0osFS0AAJoNx2mFjmRs5uL9mZww_eCec");
        connection.connect();
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
        //body参数在这里put到JSONObject中
        JSONObject parm = new JSONObject();
        parm.put("current", 1);
        parm.put("size", 30);
        parm.put("warehouseType", "virtual");

//        parm.put("platformOrderId", "210302AH7V6MHS");
        parm.put("platformOrderId", "");
//        parm.put("logistNum", "624651130235");

        System.out.println("parm:"+parm);
        writer.write(parm.toString());
        writer.flush();
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while ((strRead = reader.readLine()) != null) {
            sbf.append(strRead);
            //sbf.append("\r\n");
        }
        reader.close();
        connection.disconnect();
        String results = sbf.toString();

        System.out.println("result:"+results);





if(true){
    return;
}







        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("current", "1");
        params.add("size", "50");
        params.add("warehouseType", "virtual");


        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        headers.add("content-type", "application/json;charset=UTF-8");
        headers.add("authorization", "Basic MjAyMTAxMTIxMjI0NTI2MjE2OmIyeWkxZm52YTRkNHA3ejE4MGo3ZXB4Y3EzeWc1N2JxdndpZWRneXBpdzV0NTBscDc5cjlrbjA4dWVsM3R5dGs=");
        headers.add("ezsale-auth", "bearer eyJ0eXAiOiJKc29uV2ViVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJpc3N1c2VyIiwiYXVkIjoiYXVkaWVuY2UiLCJ0ZW5hbnRfaWQiOiIwMDAwMDAwMSIsInJvbGVfbmFtZSI6IiIsInVzZXJfdHlwZSI6MSwidXNlcl9pZCI6MTM0OTczOTU5ODUwNjQxNDA5Miwic3lzdGVtX3R5cGUiOjIsInJvbGVfaWQiOiIiLCJ1c2VyX25hbWUiOiJZb29zYnV5IiwidG9rZW5fdHlwZSI6ImFjY2Vzc190b2tlbiIsImFjY291bnQiOiJZb29zYnV5IiwiY2xpZW50X2lkIjoiMjAyMTAxMTIxMjI0NTI2MjE2IiwiZXhwIjoxNjE1MDU4NjU1LCJuYmYiOjE2MTQwNjY2NTV9.yNifCdwQq0jvlU9uZ_jbyMEYQyPvS3FGyMmIqhBmIHs");

//        String url = "https://api-cn.ezsale.com/ezsale-order/seller-outbound-order/page/outboundOrder";
//        String data = HttpUtil.post(url, params, headers);
//        System.out.println("data:"+data);












if(true){
    return;
}







        // 获取广告账户
        MultiValueMap<String, String> adAccountParams = new LinkedMultiValueMap<>();
        adAccountParams.add("access_token", ACCESS_TOKEN);
        adAccountParams.add("fields", "id,name,account_id,spend");

        String adAccountUrl = GRAPH_URL + BUSINESS_ID + "/client_ad_accounts?";
//        logger.info("adAccountParams："+adAccountParams);
        System.out.println("adAccountParams："+adAccountParams);


        String fields = "id,name,account_id,spend";

        adAccountUrl = adAccountUrl + "access_token=" + ACCESS_TOKEN + "&fields=" + fields;

//        logger.info("adAccountUrl："+adAccountUrl);
        System.out.println("adAccountUrl："+adAccountUrl);



        Map adAccountMap = new HashMap();
        adAccountMap.put("access_token", ACCESS_TOKEN);
        adAccountMap.put("fields", "id,name,account_id,spend,adcreatives{id,name,url_tags}");
        adAccountMap.put("access_token", ACCESS_TOKEN);

        HttpUtil httpUtil = new HttpUtil();

        String adAccountResult = httpUtil.doGet(adAccountUrl, adAccountMap);
        System.out.println("adAccountResult:"+adAccountResult);

        JSONObject adAccountObject = JSONObject.parseObject(adAccountResult);
        JSONArray adAccountDataArr = adAccountObject.getJSONArray("data");

        for(int i = 0; i < adAccountDataArr.size(); i++){


            JSONObject adAccountObj = adAccountDataArr.getJSONObject(i);
            String adAccountId = adAccountObj.getString("account_id");
            JSONObject adCreativesObj = adAccountObj.getJSONObject("adcreatives");
            String adCreativesStr = adCreativesObj.toJSONString();

            // 如果没有工号就跳过
            if(!adCreativesStr.contains("%5B") && !adCreativesStr.contains("%5D")){
                continue;
            }

            String start = adCreativesStr.substring(0, adCreativesStr.indexOf("%5B"));
            System.out.println("start：" + start);
            String end = adCreativesStr.substring(0, adCreativesStr.indexOf("%5D"));
            System.out.println("end：" + end);
            String jobNumber = adCreativesStr.substring(start.length()+3, end.length());


            // 获取广告系列
            String insightsUrl = GRAPH_URL + "act_" + adAccountId + "/insights?";
            /*
            MultiValueMap<String, String> campaignsParams = new LinkedMultiValueMap<>();
            campaignsParams.add("access_token", ACCESS_TOKEN);
            campaignsParams.add("time_range", "%7b'since':'2020-08-31','until':'2020-08-31'%7d");
            campaignsParams.add("fields", "account_id,spend,ad_id,campaign_id,date_start,date_stop,account_name,website_purchase_roas");*/


            String campaignsFields = "name,start_time,objective,status,spend";
//            campaignsUrl = campaignsUrl + "access_token=" + ACCESS_TOKEN + "&limit=" + 100 + "&field=" + campaignsFields;
//            logger.info("campaignsUrl:"+campaignsUrl);
            System.out.println("campaignsUrl:"+insightsUrl);

            Map insightsMap = new HashMap();
            insightsMap.put("access_token", ACCESS_TOKEN);
//            campaignsParams.put("time_range", "%7b'since':'2020-08-31','until':'2020-08-31'%7d");
            insightsMap.put("time_range", "{'since':'2020-09-01','until':'2020-09-01'}");
            insightsMap.put("fields", "account_id,spend,ad_id,campaign_id,date_start,date_stop,account_name,website_purchase_roas");

//            String campaignsResult = HttpUtil.get(campaignsUrl, campaignsParams);
            String insightsResult = httpUtil.doGet(insightsUrl, insightsMap);
            System.out.println(".............campaignsResult："+insightsResult);

            JSONObject insightsObjs = JSONObject.parseObject(insightsResult);
            JSONArray insightsDataArr = insightsObjs.getJSONArray("data");


            if(insightsDataArr.size() > 0){

                JSONObject insightsObj = insightsDataArr.getJSONObject(i);
                Double spend = insightsObj.getDouble("spend"); // 成本
                String date = insightsObj.getString("date_start");
                String accountName = insightsObj.getString("account_name");

                JSONArray purchaseRoasArr = insightsObj.getJSONArray("purchase_roas"); // 花费回报
                Double value = 0.00;
                if(purchaseRoasArr != null){
                    JSONObject purchaseRoasObj = purchaseRoasArr.getJSONObject(0);
                    value = purchaseRoasObj.getDouble("value");
                }

                Map dataMap = new HashMap<>();
                dataMap.put("items_id", adAccountId);
    //            map.put("job_number", jobNumber);
                dataMap.put("job_number", jobNumber);
                dataMap.put("ad_account", adAccountId);
    //            map.put("ad_name", campaignsName);
                dataMap.put("ad_name", accountName);
                dataMap.put("source", "facebook.com/cpc"); // 固定不变 写死

                BigDecimal revenue = new BigDecimal(spend*value).setScale(2, RoundingMode.HALF_UP);
                dataMap.put("revenue", String.format("%.2f", revenue)); // 收入
                dataMap.put("cost", spend); // 成本
                dataMap.put("type", 1); // 成本 ga0 fb1
                //                    map.put("create_time", DateUtil.timestampToTime(System.currentTimeMillis()-86400000, "yyyy-MM-dd"));
                dataMap.put("create_time", date);

                System.out.println("++++++++++++++++map:"+dataMap);

            }





        }


    }
}
