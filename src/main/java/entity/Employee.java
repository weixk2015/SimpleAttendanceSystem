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
        if (resultSet.next())
            return resultSet.getInt(1);
        else
            throw new SQLException();
    }
    boolean signIn(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String sql = String.format("INSERT INTO attendance (employee_id, date, sign_in_time) " +
                "VALUES (%d, \'%s\', \'%s\')",employeeId,sdf.format(date),sdf1.format(date));
        System.out.println(sql);
        DBUtils.executeUpdate(sql);
        return true;
    }
}
