package com.egao.common.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.egao.common.core.annotation.OperLog;
import com.egao.common.core.utils.DateUtil;
import com.egao.common.core.web.BaseController;
import com.egao.common.core.web.JsonResult;
import com.egao.common.system.service.ExpressRecordService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/expressRecord")
public class ExpressRecordController extends BaseController {

    @Autowired
    private ExpressRecordService expressRecordService;

    @RequiresPermissions("sys:expressRecord:view")
    @RequestMapping()
    public String view(Model model) {

        System.out.println("system/expressRecord.html");

        return "system/expressRecord.html";
    }




    @OperLog(value = "发货记录", desc = "分页查询")
//    @RequiresPermissions("sys:expressRecord:list")
    @ResponseBody
    @RequestMapping("/page")
    public JsonResult list(HttpServletRequest request
            , @RequestParam(name = "page", required = false, defaultValue = "0")Integer page, @RequestParam(name = "limit", required = false, defaultValue = "10")Integer limit
            , @RequestParam(name = "company", required = false)String company, @RequestParam(name = "express_number", required = false)String express_number
            , @RequestParam(name = "searchTime", required = false)String searchTime) {


        System.out.println("发货记录 分页查询数据...");


        String startTime = StringUtils.substringBefore(searchTime, " - ");

        System.out.println("startTime1："+startTime);
        System.out.println("startTime11："+(System.currentTimeMillis() - 86400000*6l));
        // 获取7天前日期
//        startTime = StringUtils.isEmpty(startTime) ? DateUtil.timestampToTime(System.currentTimeMillis() - 86400000 * 7, "yyyy-MM") : startTime;
        // 获取一年前日期
        startTime = StringUtils.isEmpty(startTime) ? DateUtil.timestampToTime(System.currentTimeMillis() - 86400000*6l, "yyyy-MM-dd") : startTime;
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
        map.put("company", company);
        map.put("express_number", express_number);
//        map.put("fees", fees);
        map.put("startTime", startTime+" 00:00:00");
        map.put("endTime", endTime+ " 23:59:59");

        System.out.println("map:"+map);

        List<Map<String, Object>> list = expressRecordService.select(map);

        int count = 0;
        if(list.size() > 0){
            count = expressRecordService.selectCount(map);
        }

        JsonResult data = JsonResult.ok(0, count,"成功").put("data", list);

        System.out.println("发货记录 data:"+JSONObject.toJSON(data));
        return data;
    }





    /**
     * 添加数据
     */
    @OperLog(value = "发货记录", desc = "添加数据", result = true)
    @RequiresPermissions("sys:expressRecord:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(HttpServletRequest request
            , @RequestParam(name = "company", required = false)String company, @RequestParam(name = "express_number", required = false)String express_number
            , @RequestParam(name = "fees", required = false)String fees, @RequestParam(name = "express_time", required = false)String express_time) {

        System.out.println("发货记录 add:"+request);


        try {

            Map map = new HashMap();
            map.put("company", company);
            map.put("express_number", express_number);
            map.put("fees", fees);
            map.put("express_time", express_time);
            System.out.println("map："+map);

            Long result = expressRecordService.insert(map);
            if(result != null){
                return JsonResult.ok("添加成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("添加失败");
    }



    /**
     * 修改数据
     */
    @OperLog(value = "发货记录", desc = "修改数据", result = true)
    @RequiresPermissions("sys:expressRecord:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(HttpServletRequest request, @RequestParam(name = "id", required = false)Integer id
            , @RequestParam(name = "company", required = false)String company, @RequestParam(name = "express_number", required = false)String express_number
            , @RequestParam(name = "fees", required = false)String fees, @RequestParam(name = "express_time", required = false)String express_time) {

        System.out.println("发货记录 update:"+request);

        try {


            Map map = new HashMap();
            map.put("id", id);
            map.put("company", company);
            map.put("express_number", express_number);
            map.put("fees", fees);
            map.put("express_time", express_time);
            System.out.println("map："+map);

            boolean result = expressRecordService.update(map);
            System.out.println("result："+result);
            if(result){
                return JsonResult.ok("修改成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.error("修改失败");
    }


    /**
     * 删除数据
     */
    @OperLog(value = "发货记录", desc = "删除数据", result = true)
    @RequiresPermissions("sys:expressRecord:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult remove(long id) {

        System.out.println("发货记录 删除数据 expressRecord_id："+id);

        boolean result = expressRecordService.deleteById(id);

        if (result) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }





}
