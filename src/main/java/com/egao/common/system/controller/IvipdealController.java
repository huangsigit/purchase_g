package com.egao.common.system.controller;

import cn.hutool.poi.excel.ExcelReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egao.common.core.UploadConstant;
import com.egao.common.core.annotation.OperLog;
import com.egao.common.core.utils.*;
import com.egao.common.core.web.BaseController;
import com.egao.common.core.web.JsonResult;
import com.egao.common.core.web.PageParam;
import com.egao.common.system.entity.Role;
import com.egao.common.system.entity.User;
import com.egao.common.system.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/sys/ivipdeal")
public class IvipdealController extends BaseController {


    @Autowired
    private AdService adService;

    @Autowired
    private ItemsService itemsService;


    @Autowired
    private IvipdealService ivipdealService;



    @Autowired
    private ChannelService channelService;

    @Autowired
    private UserItemService userItemService;


//    @RequiresPermissions("sys:ivipdeal:view")
    @RequestMapping()
    public String view(Model model) {

        User loginUser = getLoginUser();
        Integer userId = loginUser.getUserId();

        return "system/ivipdeal.html";
    }



    @OperLog(value = "产品管理", desc = "分页查询")
    @RequiresPermissions("sys:ivipdeal:list")
    @ResponseBody
    @RequestMapping("/page")
    public JsonResult list(HttpServletRequest request
            , @RequestParam(name = "page", required = false)Integer page, @RequestParam(name = "limit", required = false)Integer limit, @RequestParam(name = "channelId", required = false)Integer channelId
            , @RequestParam(name = "itemsId", required = false)String itemsId, @RequestParam(name = "jobNumber", required = false)String jobNumber
            , @RequestParam(name = "adAccount", required = false)String adAccount, @RequestParam(name = "searchTime", required = false)String searchTime
            , @RequestParam(name = "adChannel", required = false)String adChannel) {

        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(new String[]{"id"}, null);

        System.out.println("产品管理管理 分页查询数据...");

        System.out.println("page："+page);
        System.out.println("limit："+limit);
        System.out.println("searchTime："+searchTime);

        if(searchTime == null){

            searchTime = "";
        }


        User loginUser = getLoginUser();
        Integer userId = loginUser.getUserId();

        Integer loginUserId = getLoginUserId();
        System.out.println("userId："+userId);
        System.out.println("loginUserId："+loginUserId);

        String loginJobNumber = loginUser.getJobNumber();

        List<Role> rolesList = loginUser.getRoles();

        boolean isEmployee = false; // 是否员工

        String startTime = StringUtils.substringBefore(searchTime, " - ");

        System.out.println("startTime1："+startTime);
        System.out.println("startTime11："+(System.currentTimeMillis() - 86400000*30l));
        // 获取7天前日期
//        startTime = StringUtils.isEmpty(startTime) ? DateUtil.timestampToTime(System.currentTimeMillis() - 86400000 * 7, "yyyy-MM") : startTime;
        // 获取一年前日期
        startTime = StringUtils.isEmpty(startTime) ? DateUtil.timestampToTime(System.currentTimeMillis() - 86400000*30l, "yyyy-MM-dd") : startTime;
        System.out.println("startTime2："+startTime);

        String endTime = StringUtils.substringAfter(searchTime, " - ");
        // 获取昨天日期
        endTime = StringUtils.isEmpty(endTime) ? DateUtil.timestampToTime(System.currentTimeMillis(), "yyyy-MM-dd") : endTime;

        if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
            try {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ; //使用了默认的格式创建了一个日期格式化对象。

                Date startDate = dateFormat.parse(startTime); //注意:指定的字符串格式必须要与SimpleDateFormat的模式要一致。
                Date endDate = dateFormat.parse(endTime); //注意:指定的字符串格式必须要与SimpleDateFormat的模式要一致。

                if(startDate.getTime() > endDate.getTime()){
                    System.out.println("startDate.getTime()："+startDate.getTime());
                    System.out.println("endDate.getTime()："+endDate.getTime());

                    return JsonResult.error("开始日期不能大于结束日期...");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        Map map = new HashMap();
        map.put("page", (page-1)*limit);
        map.put("rows", limit);
//        map.put("itemsId", itemsId);
        map.put("channelId", channelId);
        map.put("jobNumber", isEmployee ? loginJobNumber : jobNumber);
        map.put("adAccount", adAccount);
        map.put("adChannel", adChannel);
        map.put("userId", userId);
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        System.out.println("map:"+map);

        List<Map<String, Object>> costList = ivipdealService.select(map);

        int costCount = 0;
        if(costList.size() > 0){
            costCount = ivipdealService.selectCount(map);

        }

        JsonResult data = JsonResult.ok(0, costCount,"成功").put("data", costList);

        System.out.println("产品管理 data:"+JSONObject.toJSON(data));
        return data;
    }




    /**
     * 添加数据
     */
    @OperLog(value = "产品管理管理", desc = "添加数据", result = true)
    @RequiresPermissions("sys:ivipdeal:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(HttpServletRequest request, @RequestParam(name = "month", required = false)String month, @RequestParam(name = "dates", required = false)String dates
            , @RequestParam(name = "itemsId", required = false)Long itemsId, @RequestParam(name = "channelId", required = false)Long channelId
            , @RequestParam(name = "cost", required = false)String costStr) {

        System.out.println("产品管理管理 add:"+request);

        System.out.println("itemId:"+itemsId);
        System.out.println("channelId:"+channelId);
        System.out.println("month:"+month);
        System.out.println("adCost:"+costStr);




        try {

            BigDecimal cost = new BigDecimal(costStr);
            BigDecimal zero = new BigDecimal(0.00);
            if(cost.compareTo(zero) < 1){
                System.out.println("cost小于等于0："+cost);
                return JsonResult.error("成本不能小于等于0");
            }


            Map map = new HashMap();
            map.put("item_id", itemsId);
            map.put("channel_id", channelId);
//            map.put("month", month);
            map.put("dates", dates);
            map.put("cost", cost);

            System.out.println("map："+map);


//            Map<String, Object> ivipdealMap = ivipdealService.selectChannelCostByItemIdAndChannelId(month, itemsId, channelId);
            Map<String, Object> ivipdealMap = null;
            if(ivipdealMap != null){
                return JsonResult.error("该成本已存在");
            }



            Integer userId = getLoginUserId();
            List<Map<String, Object>> userItemList = userItemService.selectUserItemByUserId(userId);
            map.put("userItemList", userItemList);
            map.put("userId", userId);

            ivipdealService.insert(map);

            return JsonResult.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("添加失败");
    }








    /**
     * 锁定数据
     */
    @OperLog(value = "产品管理管理", desc = "锁定数据", result = true)
    @RequiresPermissions("sys:ivipdeal:add")
    @ResponseBody
    @RequestMapping("/lock")
    public JsonResult lock(@RequestBody List<Long> list) {

        System.out.println("锁定数据 lock:"+JSON.toJSON(list));

        try {

            for(Long id : list){
                ivipdealService.updateLockStatus(id, 1); // 0未锁定 1锁定
            }

            return JsonResult.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("添加失败");
    }


    /**
     * 解除锁定
     */
    @OperLog(value = "产品管理管理", desc = "解除锁定", result = true)
    @RequiresPermissions("sys:ivipdeal:add")
    @ResponseBody
    @RequestMapping("/unlock")
    public JsonResult unlock(@RequestBody List<Long> list) {

        System.out.println("解除锁定 lock:"+JSON.toJSON(list));

        try {

            for(Long id : list){
                ivipdealService.updateLockStatus(id, 0); // 0未锁定 1锁定
            }

            return JsonResult.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("添加失败");
    }



    /**
     * 修改数据
     */
    @OperLog(value = "产品管理管理", desc = "修改数据", result = true)
    @RequiresPermissions("sys:ivipdeal:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(HttpServletRequest request, @RequestParam(name = "cost_id", required = false)Long costId, @RequestParam(name = "channelId", required = false)Long channelId
            , @RequestParam(name = "itemsId", required = false)Long itemsId, @RequestParam(name = "dates", required = false)String dates
            , @RequestParam(name = "cost", required = false)String costStr) {

        System.out.println("成本管理 update:"+request);

        System.out.println("itemsId:"+itemsId);
        System.out.println("dates:"+dates);



        try {

            BigDecimal cost = new BigDecimal(costStr);
            BigDecimal zero = new BigDecimal(0.00);
            if(cost.compareTo(zero) < 1){
                System.out.println("adCost小于等于0："+cost);
                return JsonResult.error("成本不能小于等于0");
            }


            Map map = new HashMap();
            map.put("costId", costId);
            map.put("item_id", itemsId);
            map.put("channel_id", channelId);
            map.put("dates", dates);
            map.put("cost", cost);
            System.out.println("map："+map);


            Map<String, Object> ivipdealMap = ivipdealService.selectById(channelId);

            if(ivipdealMap != null){
                Long cost_id = (Long)ivipdealMap.get("cost_id");
                System.out.println("database cost_id："+cost_id);
                System.out.println("costId："+costId);

                if(!cost_id.equals(costId)){
                    return JsonResult.error("该成本已存在");
                }
            }


            Integer userId = getLoginUserId();
            List<Map<String, Object>> userItemList = userItemService.selectUserItemByUserId(userId);
            map.put("userItemList", userItemList);
            map.put("userId", userId);
            ivipdealService.update(map);


            return JsonResult.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("添加失败");
    }


    /**
     * 删除数据
     */
    @OperLog(value = "产品管理管理", desc = "删除数据", result = true)
    @RequiresPermissions("sys:ivipdeal:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult remove(Long id) {

        System.out.println("成本管理 删除数据id："+id);

//        boolean result = ivipdealService.deleteById(id);
        boolean result = true;
        System.out.println("成本管理 删除数据 result："+result);

        if (result) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }



    public boolean isAuth(List<Map<String, Object>> userItemList, Integer itemsId, Integer userId){
        for(Map<String, Object> userItemMap : userItemList){
            Integer user_id = (Integer)userItemMap.get("user_id");
            Integer item_id = (Integer)userItemMap.get("item_id");
            if(itemsId.equals(item_id) && userId.equals(user_id)){
                return true;
            }
        }
        return false;
    }

    public static String setUrlForChn(String url) throws Exception{
        String regEx = "[\u4e00-\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(url);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            m.appendReplacement(sb, URLEncoder.encode(m.group(), "UTF-8"));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public String convert(String str) {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++)
        {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>>8); //取出高8位 
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
            sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); //取出低8位 
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
            sb.append("0");
            sb.append(tmp);

        }
        return (new String(sb));
    }

    /**
     * excel导入成本
     */
    @Transactional
    @OperLog(value = "产品管理", desc = "excel导入", param = false, result = true)
    @ResponseBody
    @RequestMapping("/import")
    public JsonResult importBatch(MultipartFile file) {

        System.out.println("产品管理导入开始："+System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        try {

            String originalFileName = file.getOriginalFilename();
            System.out.println("originalFileName："+originalFileName);

//            String str = "C:\\upload\\ivipdeal\\IVIPDEAL-products-export2.csv";
            String str = "C:\\upload\\ivipdeal\\IVIPDEAL-products-export-0526141246 - 副本.csv";

//            ExcelUtil.readCsvFile2(file.getInputStream());
            List<String[]> lists = CSVUtil.readCsv(file.getInputStream());
            System.out.println("lists："+JSON.toJSON(lists));

            int index = 0;
            List<String[]> resultLists = new ArrayList<String[]>();

            String[] tRows = new String[21];
            tRows[index++] = "id";
            tRows[index++] = "item_group_id";
            tRows[index++] = "title";
            tRows[index++] = "description";
            tRows[index++] = "link";
            tRows[index++] = "image_link";
            tRows[index++] = "price";
            tRows[index++] = "availability";
            tRows[index++] = "condition";
            tRows[index++] = "google_product_category";
            tRows[index++] = "product_type";
            tRows[index++] = "additional_image_link";
            tRows[index++] = "sale_price";
            tRows[index++] = "brand";
            tRows[index++] = "gender";
            tRows[index++] = "age_group";
            tRows[index++] = "size";
            tRows[index++] = "size_type";
            tRows[index++] = "shipping";
            tRows[index++] = "custom_label_0";
            tRows[index] = "adwords_redirect";
            resultLists.add(tRows);

            System.out.println("resultLists："+JSON.toJSON(resultLists));
/*
            ivipdealService.deleteAll();

            Map allMap = new HashMap();
            allMap.put("page", 0);
            allMap.put("rows", 1000000);
            ivipdealService.select(allMap);
*/


            Map map = new HashMap();
            for(int i = 1; i< lists.size(); i++){
                String[] rows = lists.get(i);
                String item_group_id = rows[0];

                // 第二行如果不是数字就跳过
                /*if(i == 1 && !isNumeric(item_group_id)){
                    continue;
                }
*/

                String title = rows[1];
                String description = rows[2];
                String col3 = rows[3];
                String image_link = rows[4];
                String col5 = rows[5];
                String col6 = rows[6];
                String availability = rows[7];
                String id = rows[8];
                String col9 = rows[9];
                String color = rows[10];
                String col11 = rows[11];
                String size = rows[12];
                String col13 = rows[13];
                String col14 = rows[14];
                String col15 = rows[15];
                String col16 = rows[16];
                String col17 = rows[17];
                String col18 = rows[18];
                String additional_image_link = rows[19];
                String sale_price = rows[20];
                String price = rows[21];
                String col22 = rows[22];
                String col23 = rows[23];
                String col24 = rows[24];
                String col25 = rows[25];
                String col26 = rows[26];
//                String link = rows[27];

                String[] trRows = new String[21];

                if(StringUtils.isEmpty(id)){
                    continue;
                }

                String encode = convert(id);
                trRows[0] = encode;

                if(StringUtils.isEmpty(title) || StringUtils.isEmpty(title) || StringUtils.isEmpty(title)){
                    List<Map<String, Object>> list = ivipdealService.selectByItemGroupId(item_group_id);
                    if(list.size() > 0){
                        title = StringUtils.isEmpty(title) ? (String)list.get(0).get("title") : title;
                        image_link = StringUtils.isEmpty(image_link) ? (String)list.get(0).get("image_link") : image_link;
                    }
                }

                String linkUrl = "https://www.ivipdeal.com/products/";
                String linkTitle = "";
                if(StringUtils.isNotEmpty(title)){
                    linkTitle = linkUrl + title.toLowerCase().replace(" ", "-");
                }

//                trRows[1] = idStr + "0";
                trRows[1] = item_group_id;
//                trRows[1] = " "+title;
                trRows[2] = title;
//                trRows[3] = description;
                trRows[3] = title;
                trRows[4] = linkTitle;
                trRows[5] = image_link;
                trRows[6] = price;
                trRows[7] = StringUtils.isNotEmpty(availability) && availability.equals("Y") ? "in stock" : "out stock";
                trRows[8] = "";
                trRows[9] = "";
                trRows[10] = "";
                trRows[11] = additional_image_link;
                trRows[12] = sale_price;
                trRows[13] = "";
                trRows[14] = "";
                trRows[15] = "";
                trRows[16] = size;
                trRows[17] = "";
                trRows[18] = "";
                trRows[19] = "";
                trRows[20] = "";


                map.put("ivipdeal_id", id);
                map.put("item_group_id", trRows[1]);
                map.put("title", trRows[2]);
                map.put("description", trRows[3]);
                map.put("link", trRows[4]);
                map.put("image_link", trRows[5]);
                map.put("price", trRows[6]);
                map.put("availability", trRows[7]);
                map.put("conditions", trRows[8]);
                map.put("google_product_category", trRows[9]);
                map.put("product_type", trRows[10]);
                map.put("additional_image_link", trRows[11]);
                map.put("sale_price", trRows[12]);
                map.put("brand", trRows[13]);
                map.put("gender", trRows[14]);
                map.put("age_group", trRows[15]);
                map.put("size", trRows[16]);
                map.put("size_type", trRows[17]);
                map.put("shipping", trRows[18]);
                map.put("custom_label_0", trRows[19]);
                map.put("adwords_redirect", trRows[20]);
                map.put("create_time", DateUtil.timestampToTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                System.out.println("map:"+map);

                ivipdealService.insert(map);

                resultLists.add(trRows);
            }

            System.out.println("resultLists2："+JSON.toJSON(resultLists));

/*
            if(true){
                return JsonResult.error("上传失败");
            }
*/

            Date date = new Date(System.currentTimeMillis());
            System.out.println("date:"+date);

            String path;  // 文件路径
            // 文件原始名称
            String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);  // 获取文件后缀
            File outFile;
            if (UploadConstant.UUID_NAME) {  // uuid命名
                path = getDate() + UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
                outFile = new File(File.listRoots()[UploadConstant.UPLOAD_DIS_INDEX], UploadConstant.UPLOAD_DIR + path);
            } else {  // 使用原名称，存在相同着加(1)
                String prefix = originalFileName.substring(0, originalFileName.lastIndexOf("."));  // 获取文件名称
                path = getDate() + originalFileName;
//                path = "ivipdeal/" + originalFileName;
                path = "ivipdeal/" + "IVIPDEAL-products-export.csv";
                // // C:\\upload\\ivipdeal\\IVIPDEAL-products-export.csv
                outFile = new File(File.listRoots()[UploadConstant.UPLOAD_DIS_INDEX], UploadConstant.UPLOAD_DIR + path);
                System.out.println(".............outFile："+outFile.getPath());
            }

/*
            try {
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }

                CSVUtil.writeCsv(new FileOutputStream(outFile), resultLists);
//                file.transferTo(outFile);
            } catch (Exception e) {
                e.printStackTrace();
                return JsonResult.error("上传失败").put("error", e.getMessage());
            }
*/

//            return JsonResult.ok("导入完成，成功" + okNum + "条，失败" + errorNum + "条");

            System.out.println("产品管理导入结束："+System.currentTimeMillis());
            return JsonResult.ok(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("导入失败");
    }



}
