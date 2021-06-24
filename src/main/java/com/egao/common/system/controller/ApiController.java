package com.egao.common.system.controller;

import com.alibaba.fastjson.JSON;
import com.egao.common.core.UploadConstant;
import com.egao.common.core.annotation.OperLog;
import com.egao.common.core.utils.CSVUtil;
import com.egao.common.core.utils.FileUploadUtil;
import com.egao.common.core.utils.IPUtil;
import com.egao.common.core.web.JsonResult;
import com.egao.common.system.service.IvipdealService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.net.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件服务器
 * Created by wangfan on 2018-12-24 16:10
 */
@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private IvipdealService ivipdealService;

//    @RequiresPermissions("sys:file:view")
    @RequestMapping("/test")
    public void view(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("tttttttttt");

        try {
            request.setAttribute("send_name", "abcde");
            System.out.println("重定向获取的属性:" + request.getAttribute("send_name"));
            System.out.println("SendServlet 的doGet 方法");

            String ipAddress = IPUtil.getIpAddress(request);

            String path = "https://bit.ly/3nqWHSV";
            response.sendRedirect(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "ivipdeal", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> ivipdeal(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("ivipdeal start...");

        try {

            int index = 0;
            List<String[]> resultLists = new ArrayList<String[]>();

            String[] tRows = new String[21];
            tRows[index] = "id";
            tRows[++index] = "item_group_id";
            tRows[++index] = "title";
            tRows[++index] = "description";
            tRows[++index] = "link";
            tRows[++index] = "image_link";
            tRows[++index] = "price";
            tRows[++index] = "availability";
            tRows[++index] = "condition";
            tRows[++index] = "google_product_category";
            tRows[++index] = "product_type";
            tRows[++index] = "additional_image_link";
            tRows[++index] = "sale_price";
            tRows[++index] = "brand";
            tRows[++index] = "gender";
            tRows[++index] = "age_group";
            tRows[++index] = "size";
            tRows[++index] = "size_type";
            tRows[++index] = "shipping";
            tRows[++index] = "custom_label_0";
            tRows[index] = "adwords_redirect";
            resultLists.add(tRows);

            System.out.println("resultLists："+ JSON.toJSON(resultLists));

            List<Map<String, Object>> lists = ivipdealService.selectByLockStatus(1);
            System.out.println("lists："+ JSON.toJSON(lists));

            for(Map<String, Object> ivipdealMap : lists){
                int index2 = 0;
                String[] rows = new String[21];
                rows[index2] = convert((String)ivipdealMap.get("ivipdeal_id"));
                rows[++index2] = (String)ivipdealMap.get("item_group_id");
                rows[++index2] = (String)ivipdealMap.get("title");
                rows[++index2] = (String)ivipdealMap.get("description");
                rows[++index2] = (String)ivipdealMap.get("link");
                rows[++index2] = (String)ivipdealMap.get("image_link");
                rows[++index2] = (String)ivipdealMap.get("price");
                rows[++index2] = (String)ivipdealMap.get("availability");
                rows[++index2] = (String)ivipdealMap.get("condition");
                rows[++index2] = (String)ivipdealMap.get("google_product_category");
                rows[++index2] = (String)ivipdealMap.get("product_type");
                rows[++index2] = (String)ivipdealMap.get("additional_image_link");
                rows[++index2] = (String)ivipdealMap.get("sale_price");
                rows[++index2] = (String)ivipdealMap.get("brand");
                rows[++index2] = (String)ivipdealMap.get("gender");
                rows[++index2] = (String)ivipdealMap.get("age_group");
                rows[++index2] = (String)ivipdealMap.get("size");
                rows[++index2] = (String)ivipdealMap.get("size_type");
                rows[++index2] = (String)ivipdealMap.get("shipping");
                rows[++index2] = (String)ivipdealMap.get("custom_label_0");
                rows[++index2] = (String)ivipdealMap.get("adwords_redirect");

                resultLists.add(rows);
            }

            // 文件原始名称
//            String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);  // 获取文件后缀
            String path = "ivipdeal/" + "IVIPDEAL-products-export.csv";
            // // C:\\upload\\ivipdeal\\IVIPDEAL-products-export.csv
            File outFile = new File(File.listRoots()[UploadConstant.UPLOAD_DIS_INDEX], UploadConstant.UPLOAD_DIR + path);
            System.out.println(".............outFile："+outFile.getPath());

            try {
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }
                CSVUtil.writeCsv(new FileOutputStream(outFile), resultLists);
//                file.transferTo(outFile);
            } catch (Exception e) {
                e.printStackTrace();
//                return JsonResult.error("上传失败").put("error", e.getMessage());
            }

//            String path = "ivipdeal/" + "IVIPDEAL-products-export.csv";
//            File outFile = new File(File.listRoots()[UploadConstant.UPLOAD_DIS_INDEX], UploadConstant.UPLOAD_DIR + path);
            System.out.println("outFile："+outFile);

            return export(new File(outFile.getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + "IVIPDEAL-products-export" + ".csv");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity .ok() .headers(headers) .contentLength(file.length()) .contentType(MediaType.parseMediaType("application/octet-stream")) .body(new FileSystemResource(file));
    }


    /**
     * 获取当前日期
     */
    public String getDate() {
        return getDate("yyyy/MM/dd/");
    }

    private String getDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
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


    /**
     * 上传文件
     */
    @OperLog(value = "文件管理", desc = "上传文件", param = false, result = true)
    @ResponseBody
    @PostMapping("/upload")
    public JsonResult upload(@RequestParam MultipartFile file) {
        System.out.println("1111111111111");
        return FileUploadUtil.upload(file);
    }

/*

    */
/**
     * 上传base64文件
     *//*

    @OperLog(value = "文件管理", desc = "上传base64文件", param = false, result = true)
    @ResponseBody
    @PostMapping("/upload/base64")
    public JsonResult uploadBase64(String base64) {
        return FileUploadUtil.upload(base64);
    }
*/

    /**
     * 预览文件
     */
    @GetMapping("/{dir}/{name:.+}")
    public void file(@PathVariable("dir") String dir, @PathVariable("name") String name, HttpServletResponse response) {
        FileUploadUtil.preview(dir + "/" + name, response);
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{dir}/{name:.+}")
    public void downloadFile(@PathVariable("dir") String dir, @PathVariable("name") String name, HttpServletResponse response) {
        FileUploadUtil.download(dir + "/" + name, response);
    }

    /**
     * 查看缩略图
     */
    @GetMapping("/thumbnail/{dir}/{name:.+}")
    public void smFile(@PathVariable("dir") String dir, @PathVariable("name") String name, HttpServletResponse response) {
        FileUploadUtil.thumbnail(dir + "/" + name, response);
    }

    /**
     * 删除文件
     */
    @OperLog(value = "文件管理", desc = "删除文件", result = true)
    @RequiresPermissions("sys:file:remove")
    @ResponseBody
    @RequestMapping("/remove")
    public JsonResult remove(String path) {
        if (path == null || path.trim().isEmpty()) {
            return JsonResult.error("参数不能为空");
        }
        if (FileUploadUtil.delete(path)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 查询文件列表
     */
    @OperLog(value = "文件管理", desc = "查询全部")
    @RequiresPermissions("sys:file:list")
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(String dir) {
        List<Map<String, Object>> list = FileUploadUtil.list(dir);
        list.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return ((Long) o2.get("updateTime")).compareTo((Long) o1.get("updateTime"));
            }
        });
        return JsonResult.ok().setData(list);
    }

}
