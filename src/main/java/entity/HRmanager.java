package main.java.entity;

import main.java.Exception.DuplicateException;
import main.java.dao.DBUtils;

/**
 * Created by devilpi on 06/01/2018.
 */
public class HRmanager extends User {
    int addUser(int age, String name, String password) throws Exception {
        String sql = String.format("INSERT INTO user (age, name, password) " +
                "VALUES (%d, \'%s\', \'%s\')",age, name, password);
        return DBUtils.executeIncInsert(sql);
    }
    boolean addEmployee(int employeeID,int departmentID){
        return true;
    }
}
