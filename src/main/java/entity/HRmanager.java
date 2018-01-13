package main.java.entity;

import main.java.Exception.DuplicateException;
import main.java.Exception.NoSuchUserException;
import main.java.Exception.PermisionDeniedException;
import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by devilpi on 06/01/2018.
 */
public class HRmanager extends Manager {
    public HRmanager(User user) {
        this.age = user.age;
        this.employeeId = user.employeeId;
        this.name = user.name;
        this.password = user.password;
        this.type = TYPE.EMPLOYEE;
    }
    public void queryEmployeeById(int id) throws Exception {

        String sql = "SELECT * FROM user WHERE employee_id = "+id;
        ResultSet resultSet = DBUtils.executeSql(sql);
        int type = resultSet.getInt("type");
        if (type==1||type==3)
            throw new PermisionDeniedException();
        System.out.println("id       name        age      type");
        while(resultSet.next()) {
            dumpUserInfo(resultSet);
        }
    }
    public void queryEmployeeByName(String name) throws Exception {
        String sql = String.format("SELECT * FROM user WHERE name = \'%s\'",name );
        ResultSet resultSet = DBUtils.executeSql(sql);
        System.out.println("id       name        age      type");
        while(resultSet.next()) {
            int type = resultSet.getInt("type");
            if (type==1||type==3)
                throw new PermisionDeniedException();
            dumpUserInfo(resultSet);
        }
    }
}
