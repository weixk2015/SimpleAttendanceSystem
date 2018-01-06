package main.java.dao;

/**
 * Created by devilpi on 11/10/2017.
 */

import com.ibatis.common.resources.Resources;

import java.sql.*;
import java.util.Properties;
public class DBHelper {
    public Connection conn = null;

    public DBHelper() {
        try {
            Properties props = Resources.getResourceAsProperties("jdbc.properties");
            String url = props.getProperty("url");
            String driver = props.getProperty("driver");
            String username = props.getProperty("username");
            String password = props.getProperty("password");
            Class.forName(driver);//指定连接类型
            conn = DriverManager.getConnection(url, username, password);//获取连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DBHelper(String dataBaseName) {
        try {
            Properties props = Resources.getResourceAsProperties("jdbc.properties");
            String url = props.getProperty("url") + dataBaseName;
            String driver = props.getProperty("driver");
            String username = props.getProperty("username");
            String password = props.getProperty("password");
            Class.forName(driver);//指定连接类型
            conn = DriverManager.getConnection(url, username, password);//获取连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
