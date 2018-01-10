package main.java.entity;

import main.java.Exception.IllegalParameterException;
import main.java.Exception.NoSuchUserException;
import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by devilpi on 06/01/2018.
 */
public class SystemManager extends Manager {
    public SystemManager(User user) {
        this.age = user.age;
        this.employeeId = user.employeeId;
        this.name = user.name;
        this.password = user.password;
        this.type = TYPE.EMPLOYEE;
    }
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
    public void queryEmployeeById(int id) throws Exception {
        String sql = "SELECT * FROM user WHERE employee_id = "+id;
        ResultSet resultSet = DBUtils.executeSql(sql);
        System.out.println("id       name        age      type");
        while(resultSet.next()) {
            dumpUserInfo(resultSet);
        }
    }
    public void queryEmployeeByName(String name) throws Exception {
        String sql = String.format("SELECT * FROM user WHERE name = \'%s\'",name );
        ResultSet resultSet = DBUtils.executeSql(sql);
        System.out.println("id       name        age      type");
        while(resultSet.next()) {
            dumpUserInfo(resultSet);
        }
    }
    public void setOfficeHour(String begin, String end) throws Exception {
        String sql = String.format("Update config SET office_begin = \'%s\' AND office_end = \'%s\'",begin,end);
        DBUtils.executeUpdate(sql);
    }
    public void setHoliday(String day, int isHoliday) throws Exception {
        String sql = String.format("UPDATE holiday SET is_holiday = %d WHERE day = \'%s\'",isHoliday,day);
        DBUtils.executeUpdate(sql);
    }
}
