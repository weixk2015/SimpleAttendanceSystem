package main.java.entity;/*
 * Created by xk on 2018/1/9 19.
 */

import main.java.Exception.DuplicateException;
import main.java.Exception.NoSuchUserException;
import main.java.Exception.PermisionDeniedException;
import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Manager extends User {
    public int addUser(int age, String name, String password,int type) throws Exception {
        if (this.type==TYPE.MANAGER)
            throw new PermisionDeniedException();
        String sql = String.format("INSERT INTO user (age, name, password, type) " +
                "VALUES (%d, \'%s\', \'%s\', %d)",age, name, password, type);
        return DBUtils.executeIncInsert(sql);
    }
    public void deleteUser(int employee_id) throws Exception {
        if (this.type==TYPE.MANAGER)
            throw new PermisionDeniedException();
        String sql = String.format("DELETE FROM user WHERE employee_id = %d",employee_id);
        DBUtils.executeUpdate(sql);
    }
    public void deleteEmployee(int employee_id) throws Exception {
        if (this.type==TYPE.MANAGER)
            throw new PermisionDeniedException();
        String sql = String.format("DELETE FROM employee WHERE employee_id = %d",employee_id);
        DBUtils.executeUpdate(sql);
    }
    public boolean addEmployee(int ID,int departmentID) throws SQLException, NoSuchUserException, DuplicateException, PermisionDeniedException {
        if (this.type==TYPE.MANAGER)
            throw new PermisionDeniedException();
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
    public void showUsers() throws Exception {

    }
    public void showDepartment(int departmentID) throws Exception {
        String sql = "SELECT * FROM department WHERE department_id = "+ departmentID;
        ResultSet resultSet = DBUtils.executeSql(sql);

        System.out.println("id       name       manager");
        while(resultSet.next()) {
            dumpDepartment(resultSet);
        }
    }
    public int getDepartmentID(String departmentName) throws Exception {
        String sql = "SELECT department_id FROM department WHERE department_n = "+departmentName;
        ResultSet resultSet = DBUtils.executeSql(sql);

        if (resultSet.next()) {
            return resultSet.getInt("department_id");
        }
        return -1;
    }
    public void dumpUser(ResultSet resultSet) throws Exception {
        System.out.printf("%-8d %-10s %-15s %-8d ", resultSet.getInt("employee_id"),
                resultSet.getString("name"), resultSet.getString("password"), resultSet.getInt("age"));
        System.out.println(TYPE.values()[resultSet.getInt("type")]);
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
