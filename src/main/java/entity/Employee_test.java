package main.java.entity;/*
 * Created by xk on 2018/1/9 13.
 */

import java.sql.SQLException;

public class Employee_test {
    public static void main(String[] args) throws SQLException {
        Employee employee = new Employee(new User());
        employee.employeeId = 0;
        employee.signIn();
    }
}
