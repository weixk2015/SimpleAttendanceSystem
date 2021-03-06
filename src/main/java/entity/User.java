package main.java.entity;

import main.java.dao.DBHelper;
import main.java.dao.DBUtils;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import java.sql.ResultSet;

/**
 * Created by devilpi on 06/01/2018.
 */
public class User {
    public enum AttendanceStatus{normal,late,early_quit,absent,leave,trip,other}
    public enum LeaveType{sick, personal, maternity, wedding}
    public enum Trip{company, personal}
    public enum Status{waiting, permitted, rejected}
    public enum TYPE {EMPLOYEE,HR,MANAGER,ADMIN}
    protected int employeeId;
    protected int age;
    protected String password;
    protected String name;
    protected TYPE type;

    public User() {}

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(int type) {
        this.type = TYPE.values()[type];
    }

    public boolean login(int employeeId, String password) throws Exception {
        String sql = String.format("SELECT * FROM user WHERE employee_id=%d and password=\'%s\'", employeeId, password);
        ResultSet resultSet = DBUtils.executeSql(sql);
        boolean ret = resultSet.next();

        if(ret) {
            setName(resultSet.getString("name"));
            setAge(resultSet.getInt("age"));
            setType(resultSet.getInt("type"));
            setPassword(resultSet.getString("password"));
            setEmployeeId(resultSet.getInt("employee_id"));
        }

        return ret;
    }

    public void log(String operate, String description) {
        long curTime = new Date().getTime();
        Timestamp timestamp = new Timestamp(curTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String sql = String.format("INSERT INTO log (employee_id, time, action, description) " +
                "VALUES (%d, \'%s\', \'%s\', \'%s\')", employeeId, sdf.format(timestamp), operate, description);
        try {
            DBUtils.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modify(int age, String password, String name) throws Exception {
        log("modify_user","modify self");
        String sql = String.format("UPDATE user SET age=%d, password=\'%s\', name=\'%s\' WHERE employee_id=%d", age, password, name, employeeId);
        DBUtils.executeUpdate(sql);
    }
    public void queryUser() throws Exception {
        String sql = "SELECT * FROM user WHERE employee_id = "+employeeId;
        ResultSet resultSet = DBUtils.executeSql(sql);

        System.out.println("id       name       password        age      type");
        while(resultSet.next()) {
            dumpUser(resultSet);
        }
    }
    public void dumpUser(ResultSet resultSet) throws Exception {
        System.out.printf("%-8d %-10s %-15s %-8d ", resultSet.getInt("employee_id"),
                resultSet.getString("name"), resultSet.getString("password"), resultSet.getInt("age"));
        System.out.println(TYPE.values()[resultSet.getInt("type")]);
    }

}
