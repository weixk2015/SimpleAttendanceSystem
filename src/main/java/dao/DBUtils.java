package main.java.dao;

import java.sql.*;
import java.util.Properties;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
/**
 * Created by devilpi on 06/01/2018.
 */
public class DBUtils {
    public static DBHelper dbHelper = new DBHelper("attendance_eva");
    public static ResultSet executeSql(String sql) throws Exception {
        //dumpSql(sql);
        ResultSet resultSet = null;

        Statement stmt = dbHelper.conn.createStatement();
        resultSet = stmt.executeQuery(sql);
        return resultSet;
    }

    public static void executeUpdate(String sql) throws Exception {

        Statement stmt = dbHelper.conn.createStatement();
        stmt.executeUpdate(sql);

    }
    public static int executeIncInsert(String sql) throws Exception{
        Statement stmt = dbHelper.conn.createStatement();
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
        if ( rs.next() ) {
            return rs.getInt(1);
        }
        return -1;
    }
    public static void dumpSql(String sql) {
        System.out.println(sql);
    }
}
