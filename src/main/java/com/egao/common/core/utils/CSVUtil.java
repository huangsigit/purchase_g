package com.egao.common.core.utils;


import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON解析工具类
 * Created by hs on 2017-06-10 10:10
 */
public class CSVUtil {



    private static final Logger LOGGER = LoggerFactory.getLogger(CSVUtil.class);

    public static final String ENCODE = "GBK";
//    public static final String ENCODE = "UTF-8";

    /**
     * readCsv:根据路径读取CSV文件.<br/>
     *
     * @param csvFilePath
     * @return
     */
    public static List<String[]> readCsv(String csvFilePath) {
        List<String[]> csvList = new ArrayList<String[]>();
        try {
            CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName(ENCODE));
            /** 跳过表头 如果需要表头的话，不要写这句 */
            reader.readHeaders();
            while (reader.readRecord()) {
                csvList.add(reader.getValues());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("read csv file error. : {}", e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("read csv file error. : {}", e.getLocalizedMessage());
        }
        return csvList;
    }

    /**
     * readCsv:根据数据流读取CSV文件.<br/>
     *
     * @param csvIs
     * @return
     */
    public static List<String[]> readCsv(InputStream csvIs) {

        List<String[]> csvList = new ArrayList<String[]>();
        try {
            CsvReader reader = new CsvReader(csvIs, Charset.forName(ENCODE));
            /** 跳过表头 如果需要表头的话，不需要写这句 */
//            reader.readHeaders();
            while (reader.readRecord()) {
                csvList.add(reader.getValues());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("read csv inputStream error. : {}", e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("read csv inputStream error. : {}", e.getLocalizedMessage());
        }
        return csvList;
    }

    /**
     * writeCsv:把内容写入到文件中.<br/>
     *
     * @param csvFilePath
     * @param contents
     */
    public static void writeCsv(String csvFilePath, List<String[]> contents) {
        try {
            CsvWriter wr = new CsvWriter(csvFilePath, ',', Charset.forName(ENCODE));
            for (int i = 0; i < contents.size(); i++) {
                wr.writeRecord(contents.get(i));
            }
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("write csv file error. : {}", e.getLocalizedMessage());
        }
    }

    /**
     *
     * writeCsv:把内容写到输出流.<br/>
     *
     * @param ou
     * @param list
     */
    public static void writeCsv(OutputStream ou, List<String[]> list) {
        CsvWriter cw = null;
        try {
            cw = new CsvWriter(ou, ',', Charset.forName(ENCODE));
            for (String[] s : list) {
                cw.writeRecord(s);
            }
            // 在文件中增加BOM,该处的byte[] 可以针对不同编码进行修改
//            ou.write(new byte[] {(byte)0xEF, (byte)0xBB, (byte)0xBF});
            ou.write(new byte[] {(byte)0xEF, (byte)0xBB, (byte)0xBF});
            cw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("write csv outputStream error. : {}", e.getLocalizedMessage());
        } finally {
            if (null != cw) {
                cw.close();
            }
        }

    }

}
