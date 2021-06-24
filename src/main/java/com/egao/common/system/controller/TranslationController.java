package com.egao.common.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.egao.common.core.annotation.OperLog;
import com.egao.common.core.utils.ExcelUtil;
import com.egao.common.core.web.BaseController;
import com.egao.common.core.web.JsonResult;
import com.egao.common.core.web.PageParam;
import com.egao.common.system.entity.User;
import com.egao.common.system.service.CustomerService;
import com.egao.common.system.service.SupplierMarkService;
import com.egao.common.system.service.TranslationService;
import com.egao.common.system.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/translation")
public class TranslationController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private SupplierMarkService supplierMarkService;


    @RequiresPermissions("sys:translation:view")
    @RequestMapping()
    public String view(Model model) {

        System.out.println("system/translation.html");

        return "system/translation.html";
    }




    @OperLog(value = "本地词翻译", desc = "分页查询")
//    @RequiresPermissions("sys:translation:list")
    @ResponseBody
    @RequestMapping("/page")
    public JsonResult list(HttpServletRequest request
            , @RequestParam(name = "page", required = false, defaultValue = "0")Integer page, @RequestParam(name = "limit", required = false, defaultValue = "10")Integer limit
            , @RequestParam(name = "type", required = false)String type, @RequestParam(name = "local_language", required = false)String local_language
            , @RequestParam(name = "chinese", required = false)String chinese) {

        PageParam pageParam = new PageParam(request);
        pageParam.setDefaultOrder(new String[]{"id"}, null);

        System.out.println("本地词翻译 分页查询数据...");

        User loginUser = getLoginUser();
        Integer userId = loginUser.getUserId();

        Integer loginUserId = getLoginUserId();
        System.out.println("userId："+userId);
        System.out.println("loginUserId："+loginUserId);

        Map map = new HashMap();
        map.put("page", (page-1)*limit);
        map.put("rows", limit);
//        map.put("itemsId", itemsId);
        map.put("type", type);
        map.put("local_language", local_language);
        map.put("chinese", chinese);

        System.out.println("map:"+map);

        List<Map<String, Object>> list = translationService.select(map);

        int count = 0;
        if(list.size() > 0){
            count = translationService.selectCount(map);
        }

        JsonResult data = JsonResult.ok(0, count,"成功").put("data", list);

        System.out.println("渠道成本 data:"+JSONObject.toJSON(data));
        return data;
    }





    /**
     * 添加数据
     */
    @OperLog(value = "本地词翻译", desc = "添加数据", result = true)
    @RequiresPermissions("sys:translation:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(HttpServletRequest request
            , @RequestParam(name = "type", required = false)String type, @RequestParam(name = "local_language", required = false)String local_language
            , @RequestParam(name = "chinese", required = false)String chinese) {

        System.out.println("本地词翻译 add:"+request);


        try {

            Map map = new HashMap();
            map.put("type", type);
            map.put("local_language", local_language);
            map.put("chinese", chinese);
            System.out.println("map："+map);

            Map<String, Object> dataMap = translationService.selectByLocalLanguage(local_language);
            if(dataMap!=null){
                return JsonResult.error("本地词已存在");
            }

            boolean result = translationService.insert(map);
            if(result){
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
    @OperLog(value = "本地词翻译", desc = "修改数据", result = true)
    @RequiresPermissions("sys:translation:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(HttpServletRequest request, @RequestParam(name = "id", required = false)Long id, @RequestParam(name = "type", required = false)String type
            , @RequestParam(name = "local_language", required = false)String local_language
            , @RequestParam(name = "chinese", required = false)String chinese) {

        System.out.println("本地词翻译 update:"+request);

        try {


            Map map = new HashMap();
            map.put("id", id);
            map.put("type", type);
            map.put("local_language", local_language);
            map.put("chinese", chinese);
            System.out.println("map："+map);

            // 判断本地词是否存在
            Map<String, Object> dataMap = translationService.selectByLocalLanguage(local_language);
            if(dataMap!=null){
                Long idDb = (Long)dataMap.get("id");
                if(idDb != id){
                    return JsonResult.error("本地词已存在");
                }
            }


            boolean result = translationService.update(map);
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
    @OperLog(value = "本地词翻译", desc = "删除数据", result = true)
    @RequiresPermissions("sys:translation:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult remove(Long id) {

        System.out.println("本地词翻译 删除数据 translation_id："+id);
/*

        boolean result = translationService.deleteById(id);

        if (result) {
            return JsonResult.ok("删除成功");
        }
*/






        ExcelUtil obj = new ExcelUtil();
//        File file = new File("F:\\purchase\\越南翻译.xls");
//        File file = new File("F:\\purchase\\泰语翻译.xls");
        File file = new File("F:\\purchase\\供应商代码.xls");

        List excelList = obj.readExcel(file);
        System.out.println("list中的数据打印出来："+ JSONArray.toJSONString(excelList));

        for (int i = 0; i < excelList.size(); i++) {
            List list = (List) excelList.get(i);
            System.out.print("---------------");
/*
            Map map = new HashMap();
            map.put("type", list.get(2));
            String local_language = (String)list.get(0);
            map.put("local_language", local_language);
            map.put("chinese", list.get(1));
            System.out.println("map："+map);
            Map<String, Object> dataMap = translationService.selectByLocalLanguage(local_language);
            if(dataMap!=null){
                System.out.println("本地词已存在");
            }
            boolean result = translationService.insert(map);
*/

            System.out.println("list："+list);

            String supplier_id = (String)list.get(0);
            String supplier_name = (String)list.get(1);
            Map map = new HashMap();
            map.put("supplier_id", supplier_id);
            map.put("supplier_name", supplier_name);
            System.out.println("map："+map);


            boolean result = supplierMarkService.insert(map);
//            boolean result = true;


            if(result){
                System.out.println("添加成功");
            }
        }







        return JsonResult.error("删除失败");
    }





}
