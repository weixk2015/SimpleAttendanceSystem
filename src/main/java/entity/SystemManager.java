package main.java.entity;

import main.java.Exception.IllegalParameterException;
import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by devilpi on 06/01/2018.
 */
public class SystemManager extends User {
    public void showLog(String start, String end) throws Exception, IllegalParameterException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sdf.parse(start);
            sdf.parse(end);
        } catch (Exception e) {
            throw new IllegalParameterException();
        }

        String sql = String.format("SELECT * FROM log WHERE time BETWEEN \'%s\' and \'%s\'", start, end);
        ResultSet resultSet = DBUtils.executeSql(sql);
        System.out.println("id       time                      action       description");
        while(resultSet.next()) {
            dumpLog(resultSet);
        }
    }

    public void dumpLog(ResultSet resultSet) throws Exception {
        System.out.printf("%-8d %s %10s       %s\n", resultSet.getInt("employee_id"),
                resultSet.getString("time"), resultSet.getString("action"), resultSet.getString("description"));
    }

    public void dumpUser(ResultSet resultSet) throws Exception {
        System.out.printf("%-8d %-10s %-15s %-8d ", resultSet.getInt("employee_id"),
                resultSet.getString("name"), resultSet.getString("password"), resultSet.getInt("age"));
        System.out.println(TYPE.values()[resultSet.getInt("type")]);
    }

    public void deleteLog(String start, String end) throws Exception, IllegalParameterException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sdf.parse(start);
            sdf.parse(end);
        } catch (Exception e) {
            throw new IllegalParameterException();
        }

        String sql = String.format("DELETE FROM log WHERE time BETWEEN \'%s\' and \'%s\'", start, end);

        DBUtils.executeUpdate(sql);

    }

    public void showUsers() throws Exception {
        String sql = "SELECT * FROM user";
        ResultSet resultSet = DBUtils.executeSql(sql);

        System.out.println("id       name       password        age      type");
        while(resultSet.next()) {
            dumpUser(resultSet);
        }
    }
}
