package com.imooc.spark.project.utils;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * HBase操作工具类：Java工具类建议采用单利模式进行封装
 */
public class HBaseUtils {

    HBaseAdmin admin = null;
    Configuration configuration = null;

    private HBaseUtils(){
        configuration = new Configuration();
        configuration.set("hbase.zookeeper.quorum", "localhost:2181");
        configuration.set("hbase.rootdir", "hdfs://localhost:19000/hbase");

        try {
            admin = new HBaseAdmin(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 单例模式
    private static HBaseUtils instance = null;
    public static synchronized HBaseUtils getInstance() {
        if (instance == null) {
            instance = new HBaseUtils();
        }
        return instance;
    }

    /**
     * 根据表明获取到HTable实例
     * @param tableName
     * @return
     */
    public HTable getTable(String tableName) {
        HTable table = null;
        try {
            table = new HTable(configuration, tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return table;
    }

    /**
     * 添加记录
     * @param tableName HBase表明
     * @param rowkey HBase表的rowkey
     * @param cf HBase表的columnfamily
     * @param column HBase表的列
     * @param value 写入HBase表的值
     */
    public void putData(String tableName, String rowkey, String cf, String column, String value) {
        HTable table = getTable(tableName);

        Put put = new Put(Bytes.toBytes(rowkey));
        put.add(Bytes.toBytes(cf), Bytes.toBytes(column), Bytes.toBytes(value));

        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        HTable tableNameTest = HBaseUtils.getInstance().getTable("imooc_course_clickcount");
        System.out.println(tableNameTest.getName().getNameAsString());

        String tableName = "imooc_course_clickcount";
        String rowkey = "20171111_1";
        String cf = "info";
        String column = "click_count";
        String value = "10";
        HBaseUtils.getInstance().putData(tableName, rowkey, cf, column, value);

    }

}
