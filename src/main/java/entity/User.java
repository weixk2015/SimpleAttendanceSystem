package main.java.entity;

import main.java.dao.DBHelper;

/**
 * Created by devilpi on 06/01/2018.
 */
public class User {
    protected int employeeId;
    protected int age;
    protected String password;
    protected String name;
    protected int type;

    public User() {}

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean login(int employeeId, String password) {
        String sql = "INSERT INTO ";
        return false;
    }
}
