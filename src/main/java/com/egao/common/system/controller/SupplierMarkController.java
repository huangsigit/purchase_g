package com.egao.common.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egao.common.core.annotation.OperLog;
import com.egao.common.core.utils.ExcelUtil;
import com.egao.common.core.web.BaseController;
import com.egao.common.core.web.JsonResult;
import com.egao.common.system.service.SupplierMarkService;
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
@RequestMapping("/sys/supplierMark")
public class SupplierMarkController extends BaseController {

    @Autowired
    private SupplierMarkService supplierMarkService;

    @RequiresPermissions("sys:supplierMark:view")
    @RequestMapping()
    public String view(Model model) {

        System.out.println("system/supplierMark.html");

        return "system/supplierMark.html";
    }




    @OperLog(value = "供应商标识", desc = "分页查询")
    @RequiresPermissions("sys:supplierMark:list")
    @ResponseBody
    @RequestMapping("/page")
    public JsonResult list(HttpServletRequest request
            , @RequestParam(name = "page", required = false, defaultValue = "0")Integer page, @RequestParam(name = "limit", required = false, defaultValue = "10")Integer limit
            , @RequestParam(name = "supplier_id", required = false)String supplier_id, @RequestParam(name = "supplier_name", required = false)String supplier_name) {

        System.out.println("供应商标识 分页查询数据...");

        Map map = new HashMap();
        map.put("page", (page-1)*limit);
        map.put("rows", limit);
        map.put("supplier_id", supplier_id);
        map.put("supplier_name", supplier_name);

        System.out.println("map:"+map);

        List<Map<String, Object>> list = supplierMarkService.select(map);

        int count = 0;
        if(list.size() > 0){
            count = supplierMarkService.selectCount(map);
        }

        JsonResult data = JsonResult.ok(0, count,"成功").put("data", list);

        System.out.println("供应商标识 data:"+JSONObject.toJSON(data));
        return data;
    }





    /**
     * 添加数据
     */
    @OperLog(value = "供应商标识", desc = "添加数据", result = true)
    @RequiresPermissions("sys:supplierMark:add")
    @ResponseBody
    @RequestMapping("/add")
    public JsonResult add(HttpServletRequest request
            , @RequestParam(name = "supplier_id", required = false)String supplier_id, @RequestParam(name = "supplier_name", required = false)String supplier_name
            , @RequestParam(name = "supplier_code", required = false)String supplier_code) {

        System.out.println("供应商标识 add:"+request);

        try {

            Map map = new HashMap();
            map.put("supplier_id", supplier_id);
            map.put("supplier_name", supplier_name);
            map.put("supplier_code", supplier_code);
            System.out.println("map："+map);



            boolean result = supplierMarkService.insert(map);
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
    @OperLog(value = "供应商标识", desc = "修改数据", result = true)
    @RequiresPermissions("sys:supplierMark:update")
    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(HttpServletRequest request, @RequestParam(name = "id", required = false)Integer id
            , @RequestParam(name = "supplier_id", required = false)String supplier_id, @RequestParam(name = "supplier_name", required = false)String supplier_name
            , @RequestParam(name = "supplier_code", required = false)String supplier_code
            ) {

        System.out.println("供应商标识 update:"+request);

        try {


            Map map = new HashMap();
            map.put("id", id);
            map.put("supplier_id", supplier_id);
            map.put("supplier_name", supplier_name);
            map.put("supplier_code", supplier_code);
            System.out.println("map："+map);

            boolean result = supplierMarkService.update(map);
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
    @OperLog(value = "供应商标识", desc = "删除数据", result = true)
    @RequiresPermissions("sys:supplierMark:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult remove(long id) {

        System.out.println("供应商标识 删除数据 supplierMark_id："+id);

        boolean result = supplierMarkService.deleteById(id);


/*
        boolean result = true;

        List list = ExcelUtil.readExcel(new File("C:\\upload\\purchase\\供应商代码.xls"));

        System.out.println("list:"+ JSON.toJSON(list));
        for(int i = 1; i < list.size(); i++){
            List obj = (List)list.get(i);

            String supplier_id = (String)obj.get(0);
            String supplier_name = (String)obj.get(1);
            String supplier_code = (String)obj.get(2);

            Map map = new HashMap();
            map.put("supplier_id", supplier_id);
            map.put("supplier_name", supplier_name);
            map.put("supplier_code", getSupplierCode(supplier_code));
            supplierMarkService.insert(map);

        }
*/



        if (result) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    public String getSupplierCode(String supplierCode){
        try {
            return supplierCode.length() < 2 ? "00"+supplierCode : supplierCode.length() < 3 ? "0" + supplierCode : supplierCode;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @OperLog(value = "供应商标识", desc = "获取供应商")
    @RequiresPermissions("sys:supplierMark:list")
    @ResponseBody
    @RequestMapping("/getSupplier")
    public JsonResult getSupplier(HttpServletRequest request) {

        System.out.println("获取供应商 分页查询数据...");

        Map map = new HashMap();
        map.put("page", 0);
        map.put("rows", 1000);

        System.out.println("map:"+map);

        List<Map<String, Object>> list = supplierMarkService.select(map);

        JsonResult data = JsonResult.ok().put("data", list);

        System.out.println("获取供应商 data:"+JSONObject.toJSON(data));
        return data;
    }


}
