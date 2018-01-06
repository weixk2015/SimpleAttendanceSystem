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
            tmp.login(employeeId, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        
    }
}
