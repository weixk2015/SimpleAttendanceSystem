package main.java.entity;

import main.java.Exception.DuplicateException;
import main.java.Exception.NoSuchUserException;
import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by devilpi on 06/01/2018.
 */
public class HRmanager extends Manager {

    int addDepartment(String departmentName, int managerID) throws SQLException, NoSuchUserException {
        String sql =  String.format( "SELECT * FROM user WHERE employee_id = %d",managerID);
        System.out.println(sql);
        ResultSet resultSet = null;
        try {
            resultSet = DBUtils.executeSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!resultSet.next())
            throw new NoSuchUserException();
        String sql1 = String.format("INSERT INTO department (department_name, manager_id) " +
                "VALUES (\'%s\', %d)",departmentName, managerID);
        try {
            return DBUtils.executeIncInsert(sql1);
        } catch (Exception e) {
            return -1;
        }

    }
    boolean modifyDepartment(int departmentID, String departmentName, int managerID) throws SQLException, NoSuchUserException {
        String sql =  String.format( "SELECT * FROM user WHERE employee_id = %d",managerID);
        System.out.println(sql);
        ResultSet resultSet = null;
        try {
            resultSet = DBUtils.executeSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!resultSet.next())
            throw new NoSuchUserException();
        String sql1 = String.format("UPDATE department SET department_name =  \'%s\',manager_id = %d " +
                "WHERE department_id = %d" ,departmentName, managerID, departmentID);
        try {
            DBUtils.executeIncInsert(sql1);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    public void showDepartments() throws Exception {
        String sql = "SELECT * FROM department";
        ResultSet resultSet = DBUtils.executeSql(sql);

        System.out.println("id       name       manager");
        while(resultSet.next()) {
            dumpDepartment(resultSet);
        }
    }
    public void dumpDepartment(ResultSet resultSet) throws Exception {
        int id= resultSet.getInt("manager_id");
        String sql = String.format("SELECT name FROM user WHERE employee_id = %d",id);
        String name = "";
        ResultSet resultSet1 = DBUtils.executeSql(sql);
        if (resultSet1.next())
            name = resultSet1.getString("name");
        System.out.printf("%-8d %-10s %-15s \n", resultSet.getInt("department_id"),
                resultSet.getString("department_name"),name );
    }
}
