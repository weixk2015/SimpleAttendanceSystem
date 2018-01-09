package main.java.entity;

import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by devilpi on 06/01/2018.
 */
public class Employee extends User {
    int deparmentID;
    Employee(User user) throws SQLException {
        this.age = user.age;
        this.employeeId = user.employeeId;
        this.name = user.name;
        this.password = user.password;
        this.type = TYPE.EMPLOYEE;
        this.deparmentID = queryDepartmentID();
    }
    private int queryDepartmentID() throws SQLException {

        String sql =  String.format( "SELECT department_id FROM employee WHERE employee_id = %d",employeeId);
        ResultSet resultSet = DBUtils.executeSql(sql);
        return resultSet.getInt(0);
    }
    boolean signIn(){
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String sql = String.format("INSERT INTO attendance (employee_id, date, sign_in_time) " +
                "VALUES (%d, %s, %s)",employeeId,timestamp,timestamp);
        return true;
    }
}
