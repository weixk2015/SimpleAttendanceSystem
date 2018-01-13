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

    static String employeeAction[] = {"logout", "check in", "check out", "apply trip", "apply leave", "check trip", "check leave",
        "modify self info", "query self info", "query attendance info"};

    static String HRManagerAction[] = {"logout", "query trip", "query leave", "check attendance",
            "modify self info", "query self info", "add employee", "modify employee info", "query employee info", "add department",
        "modify department info"};

    static String departmentManagerAction[] = {"logout", "check trip", "check leave", "check attendance",
            "modify self info", "query self info", "query trip", "query leave", "query employee info"};

    static String systemManagerAction[] = {"logout", "query trip", "query leave", "check attendance",
            "modify self info", "query self info", "add employee", "modify employee info", "query employee info", "add department",
            "modify department info", "delete user", "check log", "system config", "modify employee attendance status"};


    static String attendanceStatus[] = {"normal", "late", "early_quit", "absent", "leave", "trip", "all"};
    static String transactionStatus[] = {"waiting", "permitted", "rejected", "all"};

    static User user = null;

    static Scanner sc;

    public static void main(String[] args) {
        System.out.println("\nwelcome to simple attendance system v1.0\n");
        sc = new Scanner(System.in);
        while(true) {
            if(user == null) {
                System.out.println("Please login first!");
                login();
            } else if(user instanceof Employee) {
                solveEmployee();
            } else if(user instanceof HRmanager) {
                solveHRManager();
            } else if(user instanceof DepartmentManager) {
                solveDepartmentManager();
            } else {
                solveSystemManager();
            }
        }
    }

    public static void solveEmployee() {
        for(int i = 0; i < employeeAction.length; i ++) {
            System.out.print(i + "." + employeeAction[i] + " ");
        }
        System.out.print("\nPlease input what you want to do: ");
        int type = Integer.parseInt(sc.nextLine());
        switch (type) {
            case 0:
                logout();
                break;
            case 1:
                checkin();
                break;
            case 2:
                checkout();
                break;
            case 3:
                tripEmployee();
                break;
            case 4:
                leaveEmployee();
                break;
            case 5:
                queryEmployeeBusinessTrip();
                break;
            case 6:
                queryEmployeeLeaveInfo();
                break;
            case 7:
                modifyEmployee();
                break;
            case 8:
                queryEmployeeInfo();
                break;
            case 9:
                queryEmployeeAttendance();
                break;
            default:
                System.out.println("No such choice!");
                break;
        }
    }

    public static void solveHRManager() {
        for(int i = 0; i < HRManagerAction.length; i ++) {
            System.out.print(i + "." + HRManagerAction[i] + " ");
        }
        System.out.print("\nPlease input what you want to do: ");
        int type = Integer.parseInt(sc.nextLine());
        switch (type) {
            case 0:
                logout();
                break;
            case 1:
                queryManagerBusinessTrip();
                break;
            case 2:
                queryManagerLeaveInfo();
                break;
            case 3:
                queryManagerAttendance();
                break;
            case 4:
                modifyEmployee();
                break;
            case 5:
                queryEmployeeInfo();
                break;
            case 6:
                addEmployee();
                break;
            case 7:
                modifyOtherEmployee();
                break;
            case 8:
                queryManagerInfo();
                break;
            case 9:
                addDepartment();
                break;
            case 10:
                modifyDepartment();
                break;
            default:
                System.out.println("No such choice!");
                break;
        }
    }

    public static void solveDepartmentManager() {
        for(int i = 0; i < departmentManagerAction.length; i ++) {
            System.out.print(i + "." + departmentManagerAction[i] + " ");
        }
        System.out.print("\nPlease input what you want to do: ");
        int type = Integer.parseInt(sc.nextLine());
        switch (type) {
            case 0:
                logout();
                break;
            case 1:
                tripDepartmentManager();
                break;
            case 2:
                leaveDepartmentManager();
                break;
            case 3:
                queryManagerAttendance();
                break;
            case 4:
                modifyEmployee();
                break;
            case 5:
                queryEmployeeInfo();
                break;
            case 6:
                queryManagerBusinessTrip();
                break;
            case 7:
                queryManagerLeaveInfo();
                break;
            case 8:
                queryManagerInfo();
                break;
            default:
                System.out.println("No such choice!");
                break;
        }
    }

    public static void solveSystemManager() {
        for(int i = 0; i < systemManagerAction.length; i ++) {
            System.out.print(i + "." + systemManagerAction[i] + " ");
        }
        System.out.print("\nPlease input what you want to do: ");
        int type = Integer.parseInt(sc.nextLine());
        switch (type) {
            case 0:
                logout();
                break;
            case 1:
                queryManagerBusinessTrip();
                break;
            case 2:
                queryManagerLeaveInfo();
                break;
            case 3:
                queryManagerAttendance();
                break;
            case 4:
                modifyEmployee();
                break;
            case 5:
                queryEmployeeInfo();
                break;
            case 6:
                addEmployee();
                break;
            case 7:
                modifyOtherEmployee();
                break;
            case 8:
                queryManagerInfo();
                break;
            case 9:
                addDepartment();
                break;
            case 10:
                modifyDepartment();
                break;
            case 11:
                delete();
                break;
            case 12:
                checkLog();
                break;
            case 13:
                systemConfig();
                break;
            case 14:
                modifyEmployeeAttendanceStatus();
            default:
                System.out.println("No such choice!");
                break;
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

    public static void modifyEmployee() {
        while(true) {
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
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void addEmployee() {
        while(true) {

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
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void modifyOtherEmployee() {
        while(true) {
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
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }


    }

    public static void addDepartment() {
        while(true) {
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
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }


    }

    public static void modifyDepartment() {
        while(true) {
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
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void queryEmployeeInfo() {
        while(true) {
            try {
                user.queryUser();
            } catch (Exception e) {
                System.out.println("query failed!");
            }
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void queryEmployeeAttendance() {
        while(true) {
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
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void queryEmployeeLeaveInfo() {
        while(true) {
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
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void queryEmployeeBusinessTrip() {
        while(true) {
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
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void queryManagerInfo() {
        while(true) {
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
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void queryManagerAttendance() {
        while(true) {
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
            int departmentId;
            if(user instanceof DepartmentManager) {
                departmentId = ((DepartmentManager)user).departmentID;
            } else {
                System.out.println("Please choose a department id(-1 for all): ");
                departmentId = Integer.parseInt(sc.nextLine());
            }

            System.out.println("Please input a employee(id or name, none if not necessary): ");
            int employeeId = -1;
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
                    order = "attendance.employee_id ";
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
                ((Manager)user).queryAttendance(employeeId, departmentId, begin, end, status, order, "", "", -1);
            } catch (PermisionDeniedException e) {
                System.out.println("Sorry, your permission is insufficient!");
            } catch (Exception e) {
                System.out.println("query failed!");
            }
            System.out.println("Do you want to continue this query?(yes, no): ");
            if("no".equals(sc.nextLine())) return;
            System.out.println("Please choose the type you want to query \n" +
                    "1.number of days\t2.number of employee");
            type = Integer.parseInt(sc.nextLine());
            String groupBy = "department_id";
            if(type == 1) {
                System.out.println("group by(1.department 2.employee): ");
                int groupType = Integer.parseInt(sc.nextLine());
                if(groupType == 2) groupBy = "attendance.employee_id";
            }
            System.out.println("Please choose the order(0 for asc, 1 for desc): ");
            int orderBy = Integer.parseInt(sc.nextLine());
            if(orderBy < 0 || orderBy > 1) orderBy = -1;
            switch (type) {
                case 1:
                    try {
                        if(departmentId != -1) ((Manager)user).queryAttendance(employeeId, departmentId, begin, end, status, order, groupBy, "*", orderBy);
                        else ((Manager)user).queryAttendance(employeeId, departmentId, begin, end, status, order, groupBy, "*", orderBy);
                    } catch (PermisionDeniedException e) {
                        System.out.println("Sorry, your permission is insufficient!");
                    } catch (Exception e) {
                        System.out.println("query failed!");
                    }
                    break;
                case 2:
                    try {
                        if(departmentId != -1) ((Manager)user).queryAttendance(employeeId, departmentId, begin, end, status, order, groupBy, "attendance.employee_id", orderBy);
                        else ((Manager)user).queryAttendance(employeeId, departmentId, begin, end, status, order, groupBy, "attendance.employee_id", orderBy);
                    } catch (PermisionDeniedException e) {
                        System.out.println("Sorry, your permission is insufficient!");
                    } catch (Exception e) {
                        System.out.println("query failed!");
                    }
                    break;
                default:
                    System.out.println("No such choice!");
                    break;
            }
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void queryManagerLeaveInfo() {
        while(true) {
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
            int departmentId;
            if(user instanceof DepartmentManager) {
                departmentId = ((DepartmentManager)user).departmentID;
            } else {
                System.out.println("Please choose a department id(-1 for all): ");
                departmentId = Integer.parseInt(sc.nextLine());
            }
            System.out.println("Please input a employee(id or name, none if not necessary): ");
            int employeeId = -1;
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
            for(int i = 0; i < transactionStatus.length; i ++) {
                System.out.println(i + "." + transactionStatus[i] + " ");
            }
            int status = Integer.parseInt(sc.nextLine());
            if(status < 0 || status > 2) status = -1;

            System.out.println("Please choose leave type: \n" +
                    "0.sick leave\t1.personal leave\t2.maternity leave\t3.wedding leave\t4.all\n");
            int leaveType = Integer.parseInt(sc.nextLine());
            if(leaveType < 0 || leaveType > 3) leaveType = -1;

            String order = "";
            System.out.println("Please input the property you want to order by(0 for default, \n" +
                    "1 for begin, 2 for department, 3 for employee, 4 for status, 5 for leaveType):");
            int orderNum = Integer.parseInt(sc.nextLine());
            switch (orderNum) {
                case 0:
                    break;
                case 1:
                    order = "begin ";
                    break;
                case 2:
                    order = "department_id ";
                    break;
                case 3:
                    order = "leave_info.employee_id ";
                    break;
                case 4:
                    order = "status ";
                    break;
                case 5:
                    order = "leave_type ";
                    break;
                default:
                    System.out.println("No such choice!");
                    break;
            }
            if(orderNum >= 1 && orderNum <= 5) {
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
                ((Manager)user).queryLeave(employeeId, departmentId, begin, end, status, leaveType, order, "", "", -1);
            } catch (PermisionDeniedException e) {
                System.out.println("Sorry, your permission is insufficient!");
            } catch (Exception e) {
                System.out.println("query failed!");
            }
            System.out.println("Do you want to continue this query?(yes, no): ");
            if("no".equals(sc.nextLine())) return;
            System.out.println("Please choose the type you want to query \n" +
                    "1.number of days\t2.number of employee");
            type = Integer.parseInt(sc.nextLine());
            String groupBy = "department_id";
            if(type == 1) {
                System.out.println("group by(1.department 2.employee): ");
                int groupType = Integer.parseInt(sc.nextLine());
                if(groupType == 2) groupBy = "employee_id";
            }
            System.out.println("Please choose the order(0 for asc, 1 for desc): ");
            int orderBy = Integer.parseInt(sc.nextLine());
            if(orderBy < 0 || orderBy > 1) orderBy = -1;
            switch (type) {
                case 1:
                    try {
                        if(departmentId != -1) ((Manager)user).queryLeave(employeeId, departmentId, begin, end, status, leaveType, order, groupBy, "sum(day)", orderBy);
                        else ((Manager)user).queryLeave(employeeId, departmentId, begin, end, status, leaveType, order, groupBy, "sum(day)", orderBy);
                    } catch (PermisionDeniedException e) {
                        System.out.println("Sorry, your permission is insufficient!");
                    } catch (Exception e) {
                        System.out.println("query failed!");
                    }
                    break;
                case 2:
                    try {
                        if(departmentId != -1) ((Manager)user).queryLeave(employeeId, departmentId, begin, end, status, leaveType, order, groupBy, "count(distinct employee_id)", orderBy);
                        else ((Manager)user).queryLeave(employeeId, departmentId, begin, end, status, leaveType, order, groupBy, "count(distinct employee_id)", orderBy);
                    } catch (PermisionDeniedException e) {
                        System.out.println("Sorry, your permission is insufficient!");
                    } catch (Exception e) {
                        System.out.println("query failed!");
                    }
                    break;
                default:
                    System.out.println("No such choice!");
                    break;
            }
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void queryManagerBusinessTrip() {
        while(true) {
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
            int departmentId;
            if(user instanceof DepartmentManager) {
                departmentId = ((DepartmentManager)user).departmentID;
            } else {
                System.out.println("Please choose a department id(-1 for all): ");
                departmentId = Integer.parseInt(sc.nextLine());
            }
            System.out.println("Please input a employee(id or name, none if not necessary): ");
            int employeeId = -1;
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
            for(int i = 0; i < transactionStatus.length; i ++) {
                System.out.println(i + "." + transactionStatus[i] + " ");
            }
            int status = Integer.parseInt(sc.nextLine());
            if(status < 0 || status > 2) status = -1;

            System.out.println("Please input trip type: \n" +
                    "0.company assigned\t1.personal application\t2.all");
            int tripType = Integer.parseInt(sc.nextLine());
            if(tripType < 0 || tripType > 1) tripType = -1;

            String order = "";
            System.out.println("Please input the property you want to order by(0 for default, \n" +
                    "1 for begin, 2 for department, 3 for employee, 4 for status, 5 for tripType):");
            int orderNum = Integer.parseInt(sc.nextLine());
            switch (orderNum) {
                case 0:
                    break;
                case 1:
                    order = "begin ";
                    break;
                case 2:
                    order = "department_id ";
                    break;
                case 3:
                    order = "trip.employee_id ";
                    break;
                case 4:
                    order = "status ";
                    break;
                case 5:
                    order = "trip_type ";
                    break;
                default:
                    System.out.println("No such choice!");
                    break;
            }
            if(orderNum >= 1 && orderNum <= 5) {
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
                ((Manager)user).queryTrip(employeeId, departmentId, begin, end, status, tripType, order, "", "", -1);
            } catch (PermisionDeniedException e) {
                System.out.println("Sorry, your permission is insufficient!");
            } catch (Exception e) {
                System.out.println("query failed!");
            }
            System.out.println("Do you want to continue this query?(yes, no): ");
            if("no".equals(sc.nextLine())) return;
            System.out.println("Please choose the type you want to query \n" +
                    "1.number of days\t2.number of employee");
            type = Integer.parseInt(sc.nextLine());
            String groupBy = "department_id";
            if(type == 1) {
                System.out.println("group by(1.department 2.employee): ");
                int groupType = Integer.parseInt(sc.nextLine());
                if(groupType == 2) groupBy = "employee_id";
            }
            System.out.println("Please choose the order(0 for asc, 1 for desc): ");
            int orderBy = Integer.parseInt(sc.nextLine());
            if(orderBy < 0 || orderBy > 1) orderBy = -1;
            switch (type) {
                case 1:
                    try {
                        if(departmentId != -1) ((Manager)user).queryTrip(employeeId, departmentId, begin, end, status, tripType, order, groupBy, "sum(day)", orderBy);
                        else ((Manager)user).queryTrip(employeeId, departmentId, begin, end, status, tripType, order, groupBy, "sum(day)", orderBy);
                    } catch (PermisionDeniedException e) {
                        System.out.println("Sorry, your permission is insufficient!");
                    } catch (Exception e) {
                        System.out.println("query failed!");
                    }
                    break;
                case 2:
                    try {
                        if(departmentId != -1) ((Manager)user).queryTrip(employeeId, departmentId, begin, end, status, tripType, order, groupBy, "count(distinct employee_id)", orderBy);
                        else ((Manager)user).queryTrip(employeeId, departmentId, begin, end, status, tripType, order, groupBy, "count(distinct employee_id)", orderBy);
                    } catch (PermisionDeniedException e) {
                        System.out.println("Sorry, your permission is insufficient!");
                    } catch (Exception e) {
                        System.out.println("query failed!");
                    }
                    break;
                default:
                    System.out.println("No such choice!");
                    break;
            }
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void delete() {
        while(true) {
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
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void tripEmployee() {
        while(true) {
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
                ((Employee)user).askTrip(begin, end, type - 1, reason);
                System.out.println("apply successfully!");
            } catch (Exception e) {
                System.out.println("apply failed!");
            }
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void tripDepartmentManager() {
        try {
            ((DepartmentManager)user).checkTripApply();
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

    public static void leaveEmployee() {
        while(true) {
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
                ((Employee)user).askLeave(begin, end, type - 1, reason);
                System.out.println("apply successfully!");
            } catch (Exception e) {
                System.out.println("apply failed!");
            }
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void leaveDepartmentManager() {
        try {
            ((DepartmentManager)user).checkLeaveApply();
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

    public static void checkLog() {
        while(true) {
            try {
                System.out.println("Please input begin date(YYYY-mm-dd, eg, 2018-01-10): ");
                String begin = sc.nextLine();
                System.out.println("Please input end date(YYYY-mm-dd, eg, 2018-01-11): ");
                String end = sc.nextLine();
                ((SystemManager)user).showLog(begin, end);
            } catch (Exception e) {
                System.out.println("Query failed!");
            }
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }

    }

    public static void systemConfig() {
        while(true) {
            System.out.println("Please choose what you want to set: \n" +
                    "1.office hour\t2.holiday");
            int type = Integer.parseInt(sc.nextLine());
            switch (type) {
                case 1:
                    try {
                        System.out.println("Please input begin date(YYYY-mm-dd, eg, 2018-01-10): ");
                        String begin = sc.nextLine();
                        System.out.println("Please input end date(YYYY-mm-dd, eg, 2018-01-11): ");
                        String end = sc.nextLine();
                        ((SystemManager)user).setOfficeHour(begin, end);
                    } catch (Exception e) {
                        System.out.println("set failed!");
                    }
                    break;
                case 2:
                    try {
                        System.out.println("Please input date(YYYY-mm-dd, eg, 2018-01-10): ");
                        String day = sc.nextLine();
                        System.out.println("is holiday?(0 for not, 1 for yes): ");
                        int flag = Integer.parseInt(sc.nextLine());
                        ((SystemManager)user).setHoliday(day, flag);
                    } catch (Exception e) {
                        System.out.println("set failed!");
                    }
                    break;
                default:
                    System.out.println("No such choice!");
                    break;
            }
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
        }
    }

    public static void modifyEmployeeAttendanceStatus() {
        while(true) {
            System.out.println("Please input employee id: ");
            int employeeId = Integer.parseInt(sc.nextLine());
            System.out.println("Please input date(YYYY-mm-dd, eg, 2018-01-11): ");
            String day = sc.nextLine();
            System.out.println("Please input status(1.normal 2.late 3.early_quit 4.absent 5.leave 6.trip): ");
            int newStatus = Integer.parseInt(sc.nextLine()) - 1;
            if(newStatus >= 0 && newStatus <= 5) {
                try {
                    ((SystemManager)user).setAttendanceStatus(employeeId, day, newStatus);
                } catch (Exception e) {
                    System.out.println("set failed!");
                }
            } else {
                System.out.println("No such status!");
            }
            System.out.println("Continue or quit(c/q): ");
            String os = sc.nextLine();
            if("q".equals(os)) break;
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
