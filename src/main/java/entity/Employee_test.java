package main.java.entity;/*
 * Created by xk on 2018/1/9 13.
 */

import java.sql.SQLException;

public class Employee_test {
    public static void main(String[] args) throws SQLException {
       HRmanager hRmanager = new HRmanager();
        try {
            System.out.println(hRmanager.addUser(340,"32","3"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
