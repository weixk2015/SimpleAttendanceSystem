package main.java;

import main.java.Exception.PermisionDeniedException;
import main.java.controller.Controller;
import main.java.entity.*;

import java.lang.reflect.Method;

import java.util.Scanner;

import main.java.entity.User;

/**
 * Created by devilpi on 06/01/2018.
 */
public class Main {
    static String action[] = {"login", "logout", "query", "add", "modify", "delete", "trip", "leave", "checkin", "checkout"};

    static Controller controller = new Controller();
    static User user = null;

    static Scanner sc;

    public static void main(String[] args) {
        System.out.println("\nwelcome to simple attendance system v1.0\n");
        sc = new Scanner(System.in);
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

        System.out.print(
                "Please input your employee_id: ");
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
                    case HR: user = new HRmanager(tmp);
                        break;
                    case MANAGER: user = new DepartmentManager(tmp);
                        break;
                    case ADMIN: user = new SystemManager(tmp);
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
        if(!checkLogin()) return;
        user = null;
    }

    public static void add() {
        if(!checkLogin()) return;
        if(user instanceof Employee || user instanceof DepartmentManager) {
            System.out.println("Sorry, your permission is insufficient!");
        } else {
            addManager();
        }
    }

    public static void addManager() {
        System.out.println("Please choose what you want to add: " +
                "1.new employee\t2.new department\n");
        int type = Integer.parseInt(sc.nextLine());
        switch (type) {
            case 1:
                addEmployee();
                break;
            case 2:
                addDepartment();
                break;
            default:
                System.out.println("No such choice!");
                break;
        }
    }

    public static void modify() {
        if(!checkLogin()) return;
        if(user instanceof Employee || user instanceof DepartmentManager) modifyEmployee();
        else modifyManager();
    }

    public static void modifyEmployee() {
        System.out.print("Please input your new password: ");
        String password = sc.nextLine();
        System.out.print("Please input your new name: ");
        String name = sc.nextLine();
        System.out.print("Please input your new age: ");
        int age = Integer.parseInt(sc.nextLine());
        try {
            user.modify(age, password, name);
            System.out.println("modify success!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modifyManager() {
        System.out.println("Please choose what you want to modify: " +
                "1.self info\t2.new employee\t3.modify employee\n" +
                "4.new department\t5.modify department");
        int type = Integer.parseInt(sc.nextLine());
        switch (type) {
            case 1:
                modifyEmployee();
                break;
            case 2:
                modifyOtherEmployee();
                break;
            case 3:
                modifyDepartment();
                break;
            default:
                System.out.println("No such choice!");
                break;
        }
    }

    public static void addEmployee() {
        System.out.println("Please input name: ");
        String name = sc.nextLine();
        System.out.println("Please input password: ");
        String password = sc.nextLine();
        System.out.println("Please input age: ");
        int age = Integer.parseInt(sc.nextLine());
        try {
            int employeeId = ((Manager)user).addUser(age, name, password, 0);
            System.out.println("Please input department_name: ");
            String departmentName = sc.nextLine();
            int departmentId = ((Manager)user).getDepartmentID(departmentName);
            boolean flag = ((Manager)user).addEmployee(employeeId, departmentId);
            if(flag) {
                System.out.println("Add employee success!");
            } else {
                System.out.println("Add employee failed!");
            }
        } catch (Exception e) {
            System.out.println("Add employee failed!");
        }
    }

    public static void modifyOtherEmployee() {
        System.out.println("Please input employee_id: ");
        int employeeId = Integer.parseInt(sc.nextLine());
        System.out.println("modify person info?(yes, no): ");
        String ans = sc.nextLine();
        if(ans.equals("yes")) {
            System.out.println("Please input new name: ");
            String name = sc.nextLine();
            System.out.println("Please input new password: ");
            String password = sc.nextLine();
            System.out.println("Please input new age: ");
            int age = Integer.parseInt(sc.nextLine());
            ((Manager)user).modifyUser(age, name, password, employeeId);
            System.out.println("modify success!");
        }
        System.out.println("modify department info?(yes, no): ");
        ans = sc.nextLine();
        if(ans.equals("yes")) {
            System.out.println("Please input new department_name: ");
            String name = sc.nextLine();
            try {
                int departmentId = ((Manager)user).getDepartmentID(name);
                ((Manager)user).modifyEmployee(employeeId, departmentId);
                System.out.println("modify success!");
            } catch (Exception e) {
                System.out.println("modify failed!");
            }
        }

    }

    public static void addDepartment() {
        System.out.println("Please input department_name: ");
        String departmentName = sc.nextLine();
        System.out.println("Please input manager_id: ");
        int managerId = Integer.parseInt(sc.nextLine());
        try {
            ((Manager)user).addDepartment(departmentName, managerId);
            ((Manager)user).modifyUserType(2, managerId);
            System.out.println("modify success!");
        } catch (Exception e) {
            System.out.println("modify failed!");
        }

    }

    public static void modifyDepartment() {
        System.out.println("Please input department_id: ");
        int departmentId = Integer.parseInt(sc.nextLine());
        System.out.println("Please input new department name: ");
        String departmentName = sc.nextLine();
        System.out.println("Please input new department manager id: ");
        int managerId = Integer.parseInt(sc.nextLine());
        try {
            ((Manager)user).modifyDepartment(departmentId, departmentName, managerId);
            System.out.println("modify success!");
        } catch (Exception e) {
            System.out.println("modify failed!");
        }
    }

    public static void query() {
        if(!checkLogin()) return;
        if(user instanceof Employee) queryEmployee();
        else queryManager();
    }

    public static void queryEmployee() {
        System.out.println("Please choose the type you want to query: \n" +
                "1.self info\t2.attendance\t3.leave info\t4.business trip");
        int type = Integer.parseInt(sc.nextLine());
        switch (type) {
            case 1:
                queryEmployeeInfo();
                break;
            case 2:
                queryEmployeeAttendance();
                break;
            case 3:
                queryEmployeeLeaveInfo();
                break;
            case 4:
                queryEmployeeBusinessTrip();
                break;
            default:
                System.out.println("No such choice!");
                break;
        }
    }

    public static void queryEmployeeInfo() {
        try {
            user.queryUser();
        } catch (Exception e) {
            System.out.println("query failed!");
        }
    }

    public static void queryEmployeeAttendance() {

    }

    public static void queryEmployeeLeaveInfo() {
        System.out.println("Please choose order type: \n" +
                "1.asc\t2.desc");
        int orderType = Integer.parseInt(sc.nextLine()) - 1;
        System.out.println("Please input longest days(if no need, input -1): ");
        int dayUp = Integer.parseInt(sc.nextLine());
        System.out.println("Please input shortest days(if no need, input -1): ");
        int dayLow = Integer.parseInt(sc.nextLine());
        try {
            ((Employee)user).queryLeave(orderType, dayUp, dayLow);
        } catch (Exception e) {
            System.out.println("query failed!");
        }
    }

    public static void queryEmployeeBusinessTrip() {
        System.out.println("Please choose order type: \n" +
                "1.asc\t2.desc");
        int orderType = Integer.parseInt(sc.nextLine()) - 1;
        System.out.println("Please input longest days(if no need, input -1): ");
        int dayUp = Integer.parseInt(sc.nextLine());
        System.out.println("Please input shortest days(if no need, input -1): ");
        int dayLow = Integer.parseInt(sc.nextLine());
        try {
            ((Employee)user).queryTrip(orderType, dayUp, dayLow);
        } catch (Exception e) {
            System.out.println("query failed!");
        }
    }

    public static void queryManager() {
        System.out.println("Please choose the type you want to query: \n" +
                "1.employee info\t2.attendance\t3.leave info\t4.business trip");
        int type = Integer.parseInt(sc.nextLine());
        switch (type) {
            case 1:
                queryManagerInfo();
                break;
            case 2:
                queryManagerAttendance();
                break;
            case 3:
                queryManagerLeaveInfo();
                break;
            case 4:
                queryManagerBusinessTrip();
                break;
            default:
                System.out.println("No such choice!");
                break;
        }
    }

    public static void queryManagerInfo() {
        System.out.println("Please input employee id(-1 if you want to query by name): ");
        int employeeId = Integer.parseInt(sc.nextLine());
        if(employeeId == -1) {
            System.out.println("Please input employee name: ");
            String name = sc.nextLine();
            try {
                ((Manager)user).queryEmployeeByName(name);
            } catch (PermisionDeniedException pe) {
                System.out.println("Your permission is insufficient");
            } catch (Exception e) {
                System.out.println("query failed!");
            }
        } else {
            try {
                ((Manager)user).queryEmployeeById(employeeId);
            } catch (PermisionDeniedException) {
                System.out.println("Your permission is insufficient");
            } catch (Exception e) {
                System.out.println("query failed!");
            }
        }
    }

    public static void queryManagerAttendance() {

    }

    public static void queryManagerLeaveInfo() {

    }

    public static void queryManagerBusinessTrip() {

    }

    public static void delete() {
        if(!checkLogin()) return;
        if(user instanceof SystemManager) {
            System.out.println("Please input employee_id: ");
            int employeeId = Integer.parseInt(sc.nextLine());
            try {
                ((Manager)user).deleteUser(employeeId);
                System.out.println("delete success");
            } catch (Exception e) {
                System.out.println("delete failed");
            }
        } else {
            System.out.println("Sorry, your permission is insufficient!");
        }
    }

    public static void trip() {
        if(!checkLogin()) return;
        if(user instanceof Employee) {
            tripEmployee();
        } else if(user instanceof DepartmentManager) {
            tripDepartmentManager();
        } else {
            tripManager();
        }
    }

    public static void tripEmployee() {
        System.out.println("");
    }

    public static void tripDepartmentManager() {

    }

    public static void tripManager() {

    }

    public static void leave() {
        if(!checkLogin()) return;
        if(user instanceof Employee) {

        } else if(user instanceof DepartmentManager) {

        } else {

        }
    }

    public static void checkin() {
        if(!checkLogin()) return;
        if(user instanceof Employee) {
            try {
                ((Employee)user).checkIn();
                System.out.println("checkin success");
            } catch (Exception e) {
                System.out.println("checkin failed!");
            }
        } else {
            System.out.println("You don't need to checkin!");
        }
    }

    public static void checkout() {
        if(!checkLogin()) return;
        if(user instanceof Employee) {
            try {
                ((Employee)user).checkOff();
                System.out.println("checkout success");
            } catch (Exception e) {
                System.out.println("checkout failed!");
            }
        } else {
            System.out.println("You don't need to checkout!");
        }
    }

    public static boolean checkLogin() {
        if(user == null) {
            System.out.println("You have not log in!");
            return false;
        }
        return true;
    }
}
