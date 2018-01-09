package main.java.entity;

import main.java.dao.DBHelper;
import main.java.dao.DBUtils;
import java.util.date;

import java.sql.ResultSet;

/**
 * Created by devilpi on 06/01/2018.
 */
public class User {

    enum TYPE {EMPLOYEE,HR,MANAGER,ADMIN};
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
        String sql = String.format("SELECT * FROM user WHERE employee_id=%d and password='%s'", employeeId, password);
        ResultSet resultSet = DBUtils.executeSql(sql);
        if(resultSet.getRow() != 1) return false;
        while(resultSet.next()) {
            setName(resultSet.getString("name"));
            setAge(resultSet.getInt("age"));
            setType(resultSet.getInt("type"));
            setPassword(resultSet.getString("password"));
            setEmployeeId(resultSet.getInt("employee_id"));
        }
        return true;
    }

    public void log(String operate) throws Exception {
        DateTime.now
    }

}
