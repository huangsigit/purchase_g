package com.egao.common.core.utils;


import java.io.*;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Stream;


/**
 * JSON解析工具类
 * Created by hs on 2017-06-10 10:10
 */
public class ExcelUtil {

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    public static void main(String[] args) {
        try {

            ExcelUtil obj = new ExcelUtil();
            // 此处为我创建Excel路径：E:/zhanhj/studysrc/jxl下
//            File file = new File("F:\\purchase\\越南翻译.xls");
            File file = new File("F:\\purchase\\供应商代码.xls");

            List excelList = obj.readExcel(file);
            System.out.println("list中的数据打印出来："+ JSONArray.toJSONString(excelList));

            for (int i = 0; i < excelList.size(); i++) {
                List list = (List) excelList.get(i);
//                for (int j = 0; j < list.size(); j++) {
                    System.out.print("---------------");
//                    System.out.print(list.get(j));
                    Map map = new HashMap();
                    map.put("type", list.get(2));
                    map.put("local_language", list.get(0));
                    map.put("chinese", list.get(1));

                    System.out.println("map："+map);
//                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public static List readExcel(File file) {
        try {
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                List<List> outerList=new ArrayList<List>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getRows(); i++) {
                    List innerList=new ArrayList();
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getColumns(); j++) {
                        String cellinfo = sheet.getCell(j, i).getContents();
                        /*if(cellinfo.isEmpty()){
                            continue;
                        }*/
                        innerList.add(cellinfo);
//                        System.out.print(cellinfo);
                    }
                    outerList.add(i, innerList);
//                    System.out.println();
                }
                return outerList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int exportToExcel(HttpServletResponse response,
                                    List<Map<String, Object>> objData, String sheetName,
                                    List<String> columns) {
        int flag = 0;
        //声明工作簿jxl.write.WritableWorkbook
        WritableWorkbook wwb;
        try {
            //根据传进来的file对象创建可写入的Excel工作薄
            OutputStream os = response.getOutputStream();

            wwb = jxl.Workbook.createWorkbook(os);

            /*
             * 创建一个工作表、sheetName为工作表的名称、"0"为第一个工作表
             * 打开Excel的时候会看到左下角默认有3个sheet、"sheet1、sheet2、sheet3"这样
             * 代码中的"0"就是sheet1、其它的一一对应。
             * createSheet(sheetName, 0)一个是工作表的名称，另一个是工作表在工作薄中的位置
             */
            WritableSheet ws = wwb.createSheet(sheetName, 0);

            SheetSettings ss = ws.getSettings();
            ss.setVerticalFreeze(1);//冻结表头

            WritableFont font1 = new WritableFont(WritableFont.createFont("微软雅黑"), 10, WritableFont.BOLD);
            // WritableFont font2 =new WritableFont(WritableFont.createFont("微软雅黑"), 9 ,WritableFont.NO_BOLD);
            WritableCellFormat wcf = new WritableCellFormat(font1);
            // WritableCellFormat wcf2 = new WritableCellFormat(font2);
            // WritableCellFormat wcf3 = new WritableCellFormat(font2);//设置样式，字体

            //创建单元格样式
            //WritableCellFormat wcf = new WritableCellFormat();

            //背景颜色
            // wcf.setBackground(jxl.format.Colour.YELLOW);
//            wcf.setAlignment(Alignment.CENTRE);  //平行居中
//            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
            //  wcf3.setAlignment(Alignment.CENTRE);  //平行居中
            //  wcf3.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
            //  wcf3.setBackground(Colour.LIGHT_ORANGE);
            // wcf2.setAlignment(Alignment.CENTRE);  //平行居中
            // wcf2.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中

            /*
             * 这个是单元格内容居中显示
             * 还有很多很多样式
             */
            //   wcf.setAlignment(Alignment.CENTRE);
            //判断一下表头数组是否有数据
            if (columns != null && columns.size() > 0) {

                //循环写入表头
                for (int i = 0; i < columns.size(); i++) {
                    ws.setColumnView(i, 20);//设置列宽
//                    ws.setRowView(i+1, 600, false); //设置行高
                    /*
                     * 添加单元格(Cell)内容addCell()
                     * 添加Label对象Label()
                     * 数据的类型有很多种、在这里你需要什么类型就导入什么类型
                     * 如：jxl.write.DateTime 、jxl.write.Number、jxl.write.Label
                     * Label(i, 0, columns[i], wcf)
                     * 其中i为列、0为行、columns[i]为数据、wcf为样式
                     * 合起来就是说将columns[i]添加到第一行(行、列下标都是从0开始)第i列、样式为什么"色"内容居中
                     */
                    ws.addCell(new Label(i, 0, columns.get(i), wcf));
                }

                //判断表中是否有数据
                if (objData != null && objData.size() > 0) {
                    //循环写入表中数据
                    for (int i = 0; i < objData.size(); i++) {

                        //转换成map集合{activyName:测试功能,count:2}
                        Map<String, Object> map = objData.get(i);
                        //循环输出map中的子集：既列值
                        int j = 0;
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                        for (Object o : map.keySet()) {
                            //ps：因为要“”通用”“导出功能，所以这里循环的时候不是get("Name"),而是通过map.get(o)
                            String content = "";
//                            if (map.get(o).toString().contains(".") && CommonUtils.isNumber(map.get(o).toString())) {
                            if (map.get(o).toString().contains(".") && isNumeric(map.get(o).toString())) {
                                content = decimalFormat.format(Float.valueOf(map.get(o).toString()));
                                ws.addCell(new Label(j, i + 1, content));
                            } else if (map.get(o).toString().contains("-") && map.get(o).toString().contains(":")) {
                                content = String.valueOf(map.get(o)).split("\\.")[0];
                                ws.addCell(new Label(j, i + 1, content));
                            }
                            //图片处理
//                            else if (map.get(o).toString().contains("http") || map.get(o).toString().contains("https")){
//                                ws.setColumnView(j, 15);//设置列宽
//                                String path ="/resources/"+ String.valueOf(map.get(o)).split("upload/")[1];
//                                File imgFile = new File(path);
//                                WritableImage image = new WritableImage(j,i+1,1,1,imgFile);
//                                ws.addImage(image);
//                            }
                            else {
                                content = String.valueOf(map.get(o));
                                ws.addCell(new Label(j, i + 1, content));
                            }
                            j++;
                        }

                       /* for(int b=0;b<map.size();b++){
                        	 ws.addCell(new Label(b,i+1,String.valueOf(map.get(String.valueOf(b)))));
                        }*/
                    }
                } else {
                    flag = -1;
                }

                //写入Exel工作表
                wwb.write();

                //关闭Excel工作薄对象
                wwb.close();

                //关闭流
                os.flush();
                os.close();
            }
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (Exception ex) {
            flag = 0;
            ex.printStackTrace();
        }

        return flag;
    }

    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }


    public static List<List<Object>> readCsv(InputStream in) {
        List<List<Object>> records = new ArrayList<>();
        String record;
        // 设定UTF-8字符集，使用带缓冲区的字符输入流BufferedReader读取文件内容
        BufferedReader file = null;
        try {
//            file = new BufferedReader(new InputStreamReader(new FileInputStream(path), "GBK"));
            file = new BufferedReader(new InputStreamReader(in, "GBK"));


//            System.out.println("file："+file.);

            file.readLine(); //跳过表头所在的行
            file.readLine(); //跳过表头所在的行
//            System.out.println("file.readLine()："+file.lines());



            // 遍历数据行并存储在名为records的ArrayList中
            while ((record = file.readLine()) != null) {


                String fields[] = record.split(",");
                List<Object> list = Arrays.asList(fields);
                records.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (file!= null){
                try {
                    // 关闭文件
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return records;

    }


    public static List<List<Object>> readCsv2(InputStream in) {
        List<List<Object>> records = new ArrayList<>();
        String record;
        // 设定UTF-8字符集，使用带缓冲区的字符输入流BufferedReader读取文件内容
        BufferedReader file = null;
        try {
//            file = new BufferedReader(new InputStreamReader(new FileInputStream(path), "GBK"));
            file = new BufferedReader(new InputStreamReader(in, "GBK"));
            file.readLine(); //跳过表头所在的行

            // 遍历数据行并存储在名为records的ArrayList中
            while ((record = file.readLine()) != null) {
                String fields[] = record.split(",");
                List<Object> list = Arrays.asList(fields);
                records.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (file!= null){
                try {
                    // 关闭文件
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return records;

    }



    /**
     *计算指定文字的个数。
     *
     * @param str 文字列
     * @param c 文字
     * @param start  开始位置
     * @return 个数
     */
    private int countChar(String str, char c, int start) {
        int i = 0;
        int index = str.indexOf(c, start);
        return index == -1 ? i : countChar(str, c, index + 1) + 1;
    }
/*

    public static void readCsvFile(String filePath){
        try {
            ArrayList<String[]> csvList = new ArrayList<String[]>();
            CsvReader reader = new CsvReader(filePath,',', Charset.forName("GBK"));
//          reader.readHeaders(); //跳过表头,不跳可以注释掉

            while(reader.readRecord()){
                csvList.add(reader.getValues()); //按行读取，并把每一行的数据添加到list集合
            }
            reader.close();
            System.out.println("读取的行数："+csvList.size());

            for(int row=0;row<csvList.size();row++){
                System.out.println("-----------------");
                //打印每一行的数据
                System.out.print(csvList.get(row)[0]+",");
                System.out.print(csvList.get(row)[1]+",");
                System.out.print(csvList.get(row)[2]+",");
                System.out.println(csvList.get(row)[3]+",");
                //如果第一列（即姓名列）包含lisa，则打印出lisa的年龄
                if(csvList.get(row)[0].equals("lisa")){
                    System.out.println("lisa的年龄为："+csvList.get(row)[2]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    public static void readCsvFile2(InputStream in){

        System.out.println("readCsvFile2开始执行。。。");
        // 1. .csv文件的路径。注意只有一个\的要改成\\
//        File csv = new File(filePath); // CSV文件路径

        BufferedReader br = null;
        try {
//            br = new BufferedReader(new FileReader(csv));
            br = new BufferedReader(new InputStreamReader(in, "GBK"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
        List<String> allString = new ArrayList<>();
        try {



            while ((line = br.readLine()) != null) // 读取到的内容给line变量
            {
                everyLine = line;
                System.out.println(everyLine);

//                System.out.println("br.lines()："+br.lines().toArray());
                System.out.println("------------------");

                allString.add(everyLine);
            }
            System.out.println("csv表格：" + allString);
            System.out.println("csv表格中所有行数：" + allString.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static final String[] GENRES = { "Action", "Drama",

            "Sci-Fi/Fantasy", "Thriller/Suspense", "Comedy" };

    private static final String NEW_LINE = "\n";

    private static final String DELIMITER = ",";

    private static final int NUM_OF_COL = 5;

    private static final int GENRE_1 = 0;

    private static final int GENRE_2 = 1;

    private static final int GENRE_3 = 2;

    private static final int GENRE_4 = 3;

    private static final int GENRE_5 = 4;

    private static String moviesFile;

    private HashMap<String, Object> moviesByGenre;

    static ArrayList actionMovies;

    static ArrayList dramaMovies;

    static ArrayList sciFiFantasyMovies;

    static ArrayList thrillerSuspenseMovies;

    static ArrayList comedyMovies;

    public ExcelUtil() {
        moviesFile = "";

        moviesByGenre = new HashMap<>();

        actionMovies = new ArrayList();

        dramaMovies = new ArrayList();

        sciFiFantasyMovies = new ArrayList();

        thrillerSuspenseMovies = new ArrayList();

        comedyMovies = new ArrayList();

    }

    public static void readAndSortInputFile(String fileOfMovies) {
        try {
            BufferedReader buffRdr = new BufferedReader(new FileReader(

                    new File(fileOfMovies)));

            String line = "";

            while ((line = buffRdr.readLine()) != null) {
                String[] lnPtr = line.split(",", NUM_OF_COL);

                int diff = Math.min(lnPtr.length, NUM_OF_COL);

                for (int i = 0; i < diff; i++) {
                    if ((i == GENRE_1) && !lnPtr[i].isEmpty()) {
                        actionMovies.add(lnPtr[i]);

                    } else if ((i == GENRE_2) && !lnPtr[i].isEmpty()) {
                        dramaMovies.add(lnPtr[i]);

                    } else if ((i == GENRE_3) && !lnPtr[i].isEmpty()) {
                        sciFiFantasyMovies.add(lnPtr[i]);

                    } else if ((i == GENRE_4) && !lnPtr[i].isEmpty()) {
                        thrillerSuspenseMovies.add(lnPtr[i]);

                    } else if ((i == GENRE_5) && !lnPtr[i].isEmpty()) {
                        comedyMovies.add(lnPtr[i]);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
