package main.java.entity;

import main.java.Exception.DuplicateException;
import main.java.Exception.NoSuchUserException;
import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by devilpi on 06/01/2018.
 */
public class HRmanager extends User {
    int addUser(int age, String name, String password) throws Exception {
        String sql = String.format("INSERT INTO user (age, name, password) " +
                "VALUES (%d, \'%s\', \'%s\')",age, name, password);
        return DBUtils.executeIncInsert(sql);
    }
    boolean addEmployee(int ID,int departmentID) throws SQLException, NoSuchUserException, DuplicateException {
        String sql =  String.format( "SELECT * FROM user WHERE employee_id = %d",ID);
        //System.out.println(sql);
        ResultSet resultSet = null;
        try {
            resultSet = DBUtils.executeSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!resultSet.next())
            throw new NoSuchUserException();
        String sql1 = String.format("INSERT INTO employee (employee_id, department_id) " +
                "VALUES (%d, %d)",ID, departmentID);
        try {
           DBUtils.executeUpdate(sql1);
        } catch (Exception e) {
            throw new DuplicateException();
        }
        return true;
    }
    boolean addDepartment(String departmentName, int managerID) throws SQLException, NoSuchUserException {
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
                "VALUES (%s, %d)",departmentName, managerID);
        try {
            DBUtils.executeUpdate(sql1);
        } catch (Exception e) {
            throw new SQLException();
        }
        return true;
    }
    public boolean modifyUser(int age, String name, int ID) {
        String sql = String.format("SELECT * FROM user WHERE employee_id=%d", ID);
        ResultSet resultSet = null;
        try {
            resultSet = DBUtils.executeSql(sql);
            boolean ret = resultSet.next();
            if (!ret)
                return false;
            TYPE type = TYPE.values()[resultSet.getInt("type")];
            if (type!=TYPE.EMPLOYEE)
                return false;
            String sql1 = String.format("UPDATE user SET age=%d, name=\'%s\' WHERE employee_id=%d", age, name, ID);
            DBUtils.executeUpdate(sql1);
            } catch (Exception e) {
            return false;
        }
        return true;
    }
    public void modifyEmployee(int ID, int departmentID) throws Exception {
        String sql = String.format("UPDATE employee SET department_id = %d WHERE employee_id=%d", departmentID, ID);
        DBUtils.executeUpdate(sql);
    }
}
