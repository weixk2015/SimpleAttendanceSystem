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
    static String attendanceStatus[] = {"normal", "late", "early_quit", "absent", "leave", "trip", "all"};

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
            System.out.println("modify successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modifyManager() {
        System.out.println("Please choose what you want to modify: " +
                "1.self info\t2.modify employee\t3.modify department\n");
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
                System.out.println("Add employee successfully!");
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
            System.out.println("modify successfully!");
        }
        System.out.println("modify department info?(yes, no): ");
        ans = sc.nextLine();
        if(ans.equals("yes")) {
            System.out.println("Please input new department_name: ");
            String name = sc.nextLine();
            try {
                int departmentId = ((Manager)user).getDepartmentID(name);
                ((Manager)user).modifyEmployee(employeeId, departmentId);
                System.out.println("modify successfully!");
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
            System.out.println("modify successfully!");
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
            System.out.println("modify successfully!");
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
        System.out.println("Please input begin date(YYYY-mm-dd, eg, 2018-01-10, or null, if not necessary): ");
        String begin = sc.nextLine();
        if("null".equals(begin)) begin = "";
        System.out.println("Please input end date(YYYY-mm-dd, eg, 2018-01-11, or null, if not necessary): ");
        String end = sc.nextLine();
        if("null".equals(end)) end = "";
        try {
            ((Employee)user).queryAttendance(begin, end);
        } catch (Exception e) {
            System.out.println("query failed!");
        }
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
            } catch (PermisionDeniedException e) {
                System.out.println("Your permission is insufficient");
            } catch (Exception e) {
                System.out.println("query failed!");
            }
        } else {
            try {
                ((Manager)user).queryEmployeeById(employeeId);
            } catch (PermisionDeniedException e) {
                System.out.println("Your permission is insufficient");
            } catch (Exception e) {
                System.out.println("query failed!");
            }
        }
    }

    public static void queryManagerAttendance() {
        System.out.println("Please choose date condition: \n" +
                "0.none\t1.single day\t2.a period");
        int type = Integer.parseInt(sc.nextLine());
        String begin = "", end = "";
        switch (type) {
            case 0:
                break;
            case 1:
                System.out.println("Please input date(YYYY-mm-dd, eg, 2018-01-10): ");
                begin = end = sc.nextLine();
                break;
            case 2:
                System.out.println("Please input begin date(YYYY-mm-dd, eg, 2018-01-10): ");
                begin = sc.nextLine();
                System.out.println("Please input end date(YYYY-mm-dd, eg, 2018-01-10): ");
                end = sc.nextLine();
                break;
            default:
                System.out.println("No such choice!");
                break;
        }
        System.out.println("Please choose a department id(-1 for all): ");
        int departmentId = Integer.parseInt(sc.nextLine());
        System.out.println("Please input a employee(id or name, none if not necessary): ");
        int employeeId = 0;
        String nameOrId = sc.nextLine();
        if(!"none".equals(nameOrId)) {
            try {
                employeeId = Integer.parseInt(nameOrId);
            } catch (Exception e) {
                try {
                    employeeId = ((Manager)user).getEmoployeeID(nameOrId);
                } catch (Exception ex) {
                    System.out.println("Employee is not exist!");
                }
            }
        }
        System.out.println("Please choose a status: ");
        for(int i = 0; i < attendanceStatus.length; i ++) {
            System.out.println(i + "." + attendanceStatus[i] + " ");
        }
        int status = Integer.parseInt(sc.nextLine());
        if(status < 0 || status > 5) status = -1;

        String order = "";
        System.out.println("Please input the property you want to order by(0 for default, \n" +
                "1 for date, 2 for department, 3 for employee, 4 for status):");
        int orderNum = Integer.parseInt(sc.nextLine());
        switch (orderNum) {
            case 0:
                break;
            case 1:
                order = "date ";
                break;
            case 2:
                order = "department_id ";
                break;
            case 3:
                order = "employee_id ";
                break;
            case 4:
                order = "status ";
                break;
            default:
                System.out.println("No such choice!");
                break;
        }
        if(orderNum >= 1 && orderNum <= 4) {
            System.out.println("Please input(0 for ascending, 1 for descending): ");
            int typeNum = Integer.parseInt(sc.nextLine());
            switch (typeNum) {
                case 0:
                    order += "asc";
                    break;
                case 1:
                    order += "desc";
                    break;
                default:
                    System.out.println("No such choice!");
                    break;
            }
        }
        try {
            ((Manager)user).queryAttendance(employeeId, departmentId, begin, end, status, order, "", -1);
        } catch (PermisionDeniedException e) {
            System.out.println("Sorry, your permission is insufficient!");
        } catch (Exception e) {
            System.out.println("query failed!");
        }
        System.out.println("Do you want to quit this query?(yes, no): ");
        if("yes".equals(sc.nextLine())) return;
        System.out.println("Please choose the type you want to query \n" +
                "1.number of days\t2.number of employee");
        type = Integer.parseInt(sc.nextLine());
        switch (type) {
            case 1:
                
                break;
            case 2:

                break;
            default:
                System.out.println("No such choice!");
                break;
        }
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
                System.out.println("delete successfully");
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
        System.out.println("Please input trip type: \n" +
                "1.company assigned\t2.personal application");
        int type = Integer.parseInt(sc.nextLine());
        if(type > 2 || type < 1) {
            System.out.println("No such choice!");
            return;
        }
        System.out.println("Please input begin date(YYYY-mm-dd, eg, 2018-01-10): ");
        String begin = sc.nextLine();
        System.out.println("Please input end date(YYYY-mm-dd, eg, 2018-01-11): ");
        String end = sc.nextLine();
        System.out.println("Please input your business trip reason: ");
        String reason = sc.nextLine();
        try {
            ((Employee)user).askTrip(begin, end, type, reason);
            System.out.println("apply successfully!");
        } catch (Exception e) {
            System.out.println("apply failed!");
        }
    }

    public static void tripDepartmentManager() {
        try {
            ((DepartmentManager)user).checkApply();
            while(true) {
                System.out.println("Please select an apply id(type -1 to quit): ");
                int applyId = Integer.parseInt(sc.nextLine());
                if(applyId == -1) break;
                System.out.println("Please input your response(1 for agree, 2 for reject): ");
                int result = Integer.parseInt(sc.nextLine());
                if(result == 1) {
                    try {
                        ((DepartmentManager)user).permitTrip(applyId);
                        System.out.println("agree successfully!");
                    } catch (Exception e) {
                        System.out.println("agree failed!");
                    }
                } else if(result == 2) {
                    System.out.println("Please input your reject reason: ");
                    String reason = sc.nextLine();
                    try {
                        ((DepartmentManager)user).rejectTrip(applyId, reason);
                        System.out.println("reject successfully!");
                    } catch (Exception e) {
                        System.out.println("reject failed!");
                    }
                } else {
                    System.out.println("No such choice!");
                }
            }
        } catch (Exception e) {
            System.out.println("something wrong happened!");
        }
    }

    public static void tripManager() {
        try {
            ((DepartmentManager)user).checkApply();
        } catch (Exception e) {
            System.out.println("something wrong happened!");
        }
    }

    public static void leave() {
        if(!checkLogin()) return;
        if(user instanceof Employee) {
            leaveEmployee();
        } else if(user instanceof DepartmentManager) {
            leaveDepartmentManager();
        } else {
            leaveManager();
        }
    }

    public static void leaveEmployee() {
        System.out.println("Please input leave type: \n" +
                "1.sick leave\t2.personal leave\t3.maternity leave\t4.wedding leave\n");
        int type = Integer.parseInt(sc.nextLine());
        if(type > 4 || type < 1) {
            System.out.println("No such choice!");
            return;
        }
        System.out.println("Please input begin date(YYYY-mm-dd, eg, 2018-01-10): ");
        String begin = sc.nextLine();
        System.out.println("Please input end date(YYYY-mm-dd, eg, 2018-01-11): ");
        String end = sc.nextLine();
        System.out.println("Please input your leave reason: ");
        String reason = sc.nextLine();
        try {
            ((Employee)user).askLeave(begin, end, type, reason);
            System.out.println("apply successfully!");
        } catch (Exception e) {
            System.out.println("apply failed!");
        }
    }

    public static void leaveDepartmentManager() {
        try {
            ((DepartmentManager)user).checkApply();
            while(true) {
                System.out.println("Please select an apply id(type -1 to quit): ");
                int applyId = Integer.parseInt(sc.nextLine());
                if(applyId == -1) break;
                System.out.println("Please input your response(1 for agree, 2 for reject): ");
                int result = Integer.parseInt(sc.nextLine());
                if(result == 1) {
                    try {
                        ((DepartmentManager)user).permitLeave(applyId);
                        System.out.println("agree successfully!");
                    } catch (Exception e) {
                        System.out.println("agree failed!");
                    }
                } else if(result == 2) {
                    System.out.println("Please input your reject reason: ");
                    String reason = sc.nextLine();
                    try {
                        ((DepartmentManager)user).rejectLeave(applyId, reason);
                        System.out.println("reject successfully!");
                    } catch (Exception e) {
                        System.out.println("reject failed!");
                    }
                } else {
                    System.out.println("No such choice!");
                }
            }
        } catch (Exception e) {
            System.out.println("something wrong happened!");
        }
    }

    public static void leaveManager() {
        try {
            ((DepartmentManager)user).checkApply();
        } catch (Exception e) {
            System.out.println("something wrong happened!");
        }
    }

    public static void checkin() {
        if(!checkLogin()) return;
        if(user instanceof Employee) {
            try {
                ((Employee)user).checkIn();
                System.out.println("checkin successfully");
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
                System.out.println("checkout successfully");
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
