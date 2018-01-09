package main.java.controller;

import main.java.entity.User;

/**
 * Created by devilpi on 06/01/2018.
 */
public class Controller {
    User user;
    public void login(int employeeId, String password) {
        User tmp = new User();
        try {
            boolean flag = tmp.login(employeeId, password);
            if(flag) {
                System.out.println("true");
            }
            else System.out.println("false");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dumpUser(User user) {
        System.out.println(user.getEmployeeId());
        System.out.println(user.getAge());
        System.out.println(user.getName());
        System.out.println(user.getPassword());
        System.out.println(user.getType());
    }

    public void logout() {
        
    }
}
