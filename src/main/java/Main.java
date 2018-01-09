package main.java;

import main.java.controller.Controller;
import main.java.entity.*;

import java.lang.reflect.Method;

import java.util.Scanner;

import main.java.entity.User;

/**
 * Created by devilpi on 06/01/2018.
 */
public class Main {
    static String action[] = {"login", "logout", "query", "modify"};

    static Controller controller = new Controller();
    static User user = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.print("Please input what you want to do: ");
            String act = sc.nextLine();
            boolean flag = false;
            for(int i = 0; i < action.length; i ++) {
                if(act.equals(action[i])) {
                    flag = true;
                    Class<Main> classType = Main.class;
                    try {
                        Method method = classType.getDeclaredMethod(action[i], null);
                        method.invoke(classType, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            if(!flag) System.out.println("Sorry, your action is illegal!");
        }
    }

    public static void login() {
        if(user != null) {
            System.out.println("You have already log in!");
            return;
        }

        System.out.print("\n" +
                "welcome to simple attendance system v1.0\n" +
                "Please input your username: ");
        Scanner sc = new Scanner(System.in);
        int employeeId = Integer.parseInt(sc.nextLine());
        System.out.print("Please input your password: ");
        String password = sc.nextLine();

        User tmp = new User();
        try {
            boolean flag = tmp.login(employeeId, password);
            if(flag) {
                System.out.println("You have log in!");
                tmp.log("login", "first log");
                switch (tmp.getType()) {
                    case EMPLOYEE: user = new Employee(tmp);
                        break;
                    case HR: user = new HRmanager();
                        break;
                    case MANAGER: user = new DepartmentManager();
                        break;
                    case ADMIN: user = new SystemManager();
                        break;
                    default: break;
                }
            }
            else {
                System.out.println("employeeId or password is wrong!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public static void logout() {
        if(user == null) {
            System.out.println("You have not log in!");
            return;
        }
        user = null;
    }


}
