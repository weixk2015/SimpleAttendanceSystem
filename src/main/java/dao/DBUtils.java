package main.java.dao;

import java.sql.*;
import java.util.Properties;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
/**
 * Created by devilpi on 06/01/2018.
 */
public class DBUtils {
    static DBHelper dbHelper = new DBHelper("attendance_eva");
    public static ResultSet executeSql(String sql) {
        ResultSet resultSet = null;
        try {
            Statement stmt = dbHelper.conn.createStatement();
            resultSet = stmt.executeQuery("SELECT * FROM room");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void executeUpdate(String sql) {
        try {
            Statement stmt = dbHelper.conn.createStatement();
            stmt.executeUpdate("SELECT * FROM room");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
