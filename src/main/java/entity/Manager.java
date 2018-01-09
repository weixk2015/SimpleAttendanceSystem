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
    int addUser(int age, String name, String password) throws Exception {
        if (this.type==TYPE.MANAGER)
            throw new PermisionDeniedException();
        String sql = String.format("INSERT INTO user (age, name, password) " +
                "VALUES (%d, \'%s\', \'%s\')",age, name, password);
        return DBUtils.executeIncInsert(sql);
    }
    void deleteUser(int employee_id) throws Exception {
        if (this.type==TYPE.MANAGER)
            throw new PermisionDeniedException();
        String sql = String.format("DELETE FROM user WHERE employee_id = %d",employee_id);
        DBUtils.executeUpdate(sql);
    }
    void deleteEmployee(int employee_id) throws Exception {
        if (this.type==TYPE.MANAGER)
            throw new PermisionDeniedException();
        String sql = String.format("DELETE FROM employee WHERE employee_id = %d",employee_id);
        DBUtils.executeUpdate(sql);
    }
    boolean addEmployee(int ID,int departmentID) throws SQLException, NoSuchUserException, DuplicateException, PermisionDeniedException {
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
    public void dumpUser(ResultSet resultSet) throws Exception {
        System.out.printf("%-8d %-10s %-15s %-8d ", resultSet.getInt("employee_id"),
                resultSet.getString("name"), resultSet.getString("password"), resultSet.getInt("age"));
        System.out.println(TYPE.values()[resultSet.getInt("type")]);
    }


}
