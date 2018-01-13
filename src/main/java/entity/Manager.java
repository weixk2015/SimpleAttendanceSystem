package main.java.entity;/*
 * Created by xk on 2018/1/9 19.
 */

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import main.java.Exception.DuplicateException;
import main.java.Exception.NoSuchUserException;
import main.java.Exception.PermisionDeniedException;
import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Manager extends User {
    public int addUser(int age, String name, String password,int type) throws Exception {
        if (this.type==TYPE.MANAGER)
            throw new PermisionDeniedException();
        log("add_user","");
        String sql = String.format("INSERT INTO user (age, name, password, type) " +
                "VALUES (%d, \'%s\', \'%s\', %d)",age, name, password, type);
        return DBUtils.executeIncInsert(sql);
    }
    public void deleteUser(int employee_id) throws Exception {
        if (this.type==TYPE.MANAGER)
            throw new PermisionDeniedException();
        log("del_user","");
        String sql = String.format("DELETE FROM user WHERE employee_id = %d",employee_id);
        DBUtils.executeUpdate(sql);
    }
    public void deleteEmployee(int employee_id) throws Exception {
        if (this.type==TYPE.MANAGER)
            throw new PermisionDeniedException();

        log("del_employee","");
        String sql = String.format("DELETE FROM employee WHERE employee_id = %d",employee_id);
        DBUtils.executeUpdate(sql);
    }
    public boolean addEmployee(int ID,int departmentID) throws SQLException, NoSuchUserException, DuplicateException, PermisionDeniedException {
        if (this.type==TYPE.MANAGER)
            throw new PermisionDeniedException();
        log("add_employee","");

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

    public boolean modifyUser(int age, String name, String password, int ID) {
        log("modify_user","");
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
            String sql1 = String.format("UPDATE user SET age=%d, name=\'%s\',  password=\'%s\' WHERE employee_id=%d",
                    age, name, password, ID);
            DBUtils.executeUpdate(sql1);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public boolean modifyUserType(int type, int ID) {

        log("modify_user","");
        String sql1 = String.format("UPDATE user SET type=%d WHERE employee_id=%d", type, ID);
        try {
            DBUtils.executeUpdate(sql1);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public void modifyEmployee(int ID, int departmentID) throws Exception {

        log("modify_employee","");
        String sql = String.format("UPDATE employee SET department_id = %d WHERE employee_id=%d", departmentID, ID);
        DBUtils.executeUpdate(sql);
    }
    public void showUsers() throws Exception {

    }
    public void queryEmployeeById(int id) throws Exception {

    }
    public void queryEmployeeByName(String name) throws Exception {

    }
    public void dumpUserInfo(ResultSet resultSet) throws Exception {
        System.out.printf("%-8d %-10s %-8d ", resultSet.getInt("employee_id"),
                resultSet.getString("name"), resultSet.getInt("age"));
        System.out.println(TYPE.values()[resultSet.getInt("type")]);
    }
    public void showDepartment(int departmentID) throws Exception {
        String sql = "SELECT * FROM department WHERE department_id = "+ departmentID;
        ResultSet resultSet = DBUtils.executeSql(sql);

        System.out.println("id       name       manager");
        while(resultSet.next()) {
            dumpDepartment(resultSet);
        }
    }
    public int getDepartmentID(String departmentName) throws Exception {
        String sql = String.format("SELECT department_id FROM department WHERE department_n = \'%s\'",departmentName);
        ResultSet resultSet = DBUtils.executeSql(sql);

        if (resultSet.next()) {
            return resultSet.getInt("department_id");
        }
        return -1;
    }
    public int getEmoployeeID(String userName) throws Exception {
        String sql = String.format("SELECT employee_id FROM user WHERE name = \'%s\'",userName);
        ResultSet resultSet = DBUtils.executeSql(sql);

        if (resultSet.next()) {
            return resultSet.getInt("employee_id");
        }
        return -1;
    }
    public String getEmoployeeName(int ID) throws Exception {
        String sql = String.format("SELECT name FROM user WHERE employee_id = \'%s\'",ID);
        ResultSet resultSet = DBUtils.executeSql(sql);

        if (resultSet.next()) {
            return resultSet.getString("name");
        }
        return "";
    }
    public void dumpDepartment(ResultSet resultSet) throws Exception {
        int id= resultSet.getInt("manager_id");
        String sql = String.format("SELECT name FROM user WHERE employee_id = %d",id);
        String name = "";
        ResultSet resultSet1 = DBUtils.executeSql(sql);
        if (resultSet1.next())
            name = resultSet1.getString("name");
        System.out.printf("%-8d %-10s %-15s \n", resultSet.getInt("department_id"),
                resultSet.getString("department_name"),name );
    }
    public int addDepartment(String departmentName, int managerID) throws SQLException, NoSuchUserException {
        String sql =  String.format( "SELECT * FROM user WHERE employee_id = %d",managerID);
        System.out.println(sql);
        ResultSet resultSet = null;
        try {
            resultSet = DBUtils.executeSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!resultSet.next())
            throw new NoSuchUserException();
        String sql1 = String.format("INSERT INTO department (department_name, manager_id) " +
                "VALUES (\'%s\', %d)",departmentName, managerID);
        try {
            return DBUtils.executeIncInsert(sql1);
        } catch (Exception e) {
            return -1;
        }

    }
    public boolean modifyDepartment(int departmentID, String departmentName, int managerID) throws SQLException, NoSuchUserException {
        String sql =  String.format( "SELECT * FROM user WHERE employee_id = %d",managerID);
        System.out.println(sql);
        ResultSet resultSet = null;
        try {
            resultSet = DBUtils.executeSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!resultSet.next())
            throw new NoSuchUserException();
        modifyUserType(2,managerID);
        String sql1 = String.format("UPDATE department SET department_name =  \'%s\',manager_id = %d " +
                "WHERE department_id = %d" ,departmentName, managerID, departmentID);
        try {
            DBUtils.executeIncInsert(sql1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public void dumpAttendance(ResultSet resultSet) throws Exception {
        System.out.printf("%-8d %-8s %-8d %-8s %-10s %-15s %-8s\n",
                resultSet.getInt("employee_id"),
                getEmoployeeName(resultSet.getInt("employee_id")),
                resultSet.getInt("department_id"),
                resultSet.getString("date"), resultSet.getString("sign_in_time"),
                resultSet.getString("sign_off_time"),
                AttendanceStatus.values()[resultSet.getInt("status")]);
    }
    public void dumpGroupAttendance(ResultSet resultSet,String group) throws Exception {

        System.out.printf("%-8s %-8s\n",
                resultSet.getString(group),
                resultSet.getString("count"));
    }
    public void dumpLeave(ResultSet resultSet) throws Exception {
        System.out.printf("%-8d %-8s %-8d %-8s %-10s %-15s %-8s %-15s %-15s\n",
                resultSet.getInt("employee_id"),
                getEmoployeeName(resultSet.getInt("employee_id")),
                resultSet.getInt("department_id"),
                resultSet.getString("begin"), resultSet.getString("end"),
                LeaveType.values()[resultSet.getInt("leave_type")],
                Status.values()[resultSet.getInt("status")],
                resultSet.getString("reason"),
                resultSet.getString("reject_reason"));
    }
    public void dumpGroupLeave(ResultSet resultSet,String group) throws Exception {
        System.out.printf("%-8s %8s\n",
                resultSet.getString(group),
                resultSet.getString("count"));
    }
    public void dumpTrip(ResultSet resultSet) throws Exception {
        System.out.printf("%-8d %-8s %-8d %-8s %-10s %-15s %-8s %-15s %-15s\n",
                resultSet.getInt("employee_id"),
                getEmoployeeName(resultSet.getInt("employee_id")),
                resultSet.getInt("department_id"),
                resultSet.getString("begin"), resultSet.getString("end"),
                LeaveType.values()[resultSet.getInt("trip_type")],
                Status.values()[resultSet.getInt("status")],
                resultSet.getString("business"),
                resultSet.getString("reject_reason"));
    }
    public void dumpGroupTrip(ResultSet resultSet,String group) throws Exception {
        System.out.printf("%-8s %8s",
                resultSet.getString(group),
                resultSet.getString("count"));
    }
    public int queryAttendance(int ID, int departID, String dateBegin, String dateEnd,int status,
                               String order, String groupBy, String countArg, int oderByCount) throws Exception {
        String sqlDayBegin = "";
        String sqlDayEnd = "";
        String sqlEmployeeID = "";
        String sqlDepartmentID = "";
        String sqlStatus = "";
        String sqlOrder = "";
        if (!dateBegin.equals(""))
            sqlDayBegin = "AND date >= \'"+dateBegin+"\'";
        if (!dateEnd.equals(""))
            sqlDayEnd = "AND date <= \'"+dateEnd+"\'";
        if (ID!=-1)
            sqlEmployeeID = "AND attendance.employee_id = "+ID;
        if (departID!=-1)
            sqlDepartmentID = "AND department_id = "+departID;
        if (status!=-1)
            sqlStatus = "AND status = "+status;

        if (!order.equals(""))
            sqlOrder = "ORDER BY "+order;
        if (!groupBy.equals("")) {
            if (oderByCount==0){
                sqlOrder = "ORDER BY count asc";
            }else if (oderByCount==1){
                sqlOrder = "ORDER BY count desc";
            }
            if (!countArg.equals("*")){
                countArg = "DISTINCT "+countArg;
            }
            String sql = String.format("SELECT %s, COUNT(%s) AS count FROM attendance, employee WHERE attendance.employee_id = employee.employee_id" +
                            " %s %s %s %s %s GROUP BY %s  %s",
                    groupBy, countArg, sqlEmployeeID, sqlDepartmentID, sqlDayBegin, sqlDayEnd, sqlStatus, groupBy, sqlOrder);
            ResultSet resultSet = DBUtils.executeSql(sql);
            System.out.println("--------------attendance_info---------------");
            System.out.printf("%s        count\n",groupBy);
            while(resultSet.next()) {
                dumpGroupAttendance(resultSet, groupBy);
            }
            return 0;
        }
        String sql = String.format("SELECT * FROM attendance, employee WHERE attendance.employee_id = employee.employee_id" +
                        " %s %s %s %s %s %s ",
                sqlEmployeeID, sqlDepartmentID, sqlDayBegin, sqlDayEnd, sqlStatus, sqlOrder);
        System.out.println(sql);
        ResultSet resultSet = DBUtils.executeSql(sql);
        System.out.println("--------------attendance_info---------------");
        System.out.println("employee_id employee_name  department_id  date  sign_in  sign_off  status");
        while(resultSet.next()) {
            dumpAttendance(resultSet);
        }
        return 0;
    }
    public int queryLeave(int ID, int departID, String dateBegin, String dateEnd,int status,int leaveType,
                               String order, String groupBy, String agFunc, int oderByCount) throws Exception {
        String sqlDayBegin = "";
        String sqlDayEnd = "";
        String sqlEmployeeID = "";
        String sqlDepartmentID = "";
        String sqlLeaveType = "";
        String sqlStatus = "";
        String sqlOrder = "";
        if (!dateBegin.equals("")&&!dateEnd.equals("")) {
            sqlDayBegin = "AND begin <= \'" + dateEnd + "\'";
            sqlDayEnd = "OR end >= \'" + dateBegin + "\'";
        }
        if (leaveType!=-1){
            sqlLeaveType = "AND leave_type = "+leaveType;
        }
        if (ID!=-1)
            sqlEmployeeID = "AND leave_info.employee_id = "+ID;
        if (departID!=-1)
            sqlDepartmentID = "AND department_id = "+departID;
        if (status!=-1)
            sqlStatus = "AND status = "+status;

        if (!order.equals(""))
            sqlOrder = "ORDER BY "+order;
        if (!groupBy.equals("")) {
            if (!dateBegin.equals("")&&!dateEnd.equals("")) {
                sqlDayEnd = "AND end <= \'" + dateEnd + "\'";
                sqlDayBegin = "AND begin >= \'" + dateBegin + "\'";
            }
            if (oderByCount==0){
                sqlOrder = "ORDER  BY count asc";
            }else if (oderByCount==1){
                sqlOrder = "ORDER BY count desc";
            }
            String sql = String.format("SELECT %s, %s AS count FROM (SELECT %s, leave_info.employee_id, end-begin AS day" +
                            "FROM leave_info, employee WHERE leave_info.employee_id = employee.employee_id" +
                            " %s %s %s %s %s %s )  GROUP BY %s  %s",
                    groupBy, agFunc, groupBy, sqlEmployeeID, sqlDepartmentID, sqlLeaveType, sqlDayBegin, sqlDayEnd, sqlStatus,
                    groupBy, sqlOrder);
            ResultSet resultSet = DBUtils.executeSql(sql);
            System.out.println("--------------leave_info---------------");
            System.out.printf("%s        count\n",groupBy);
            while(resultSet.next()) {
                dumpGroupLeave(resultSet, groupBy);
            }
            return 0;
        }
        String sql = String.format("SELECT * FROM leave_info, employee WHERE leave_info.employee_id = employee.employee_id" +
                        " %s %s %s %s %s %s %s",
                sqlEmployeeID, sqlDepartmentID, sqlLeaveType, sqlDayBegin, sqlDayEnd, sqlStatus, sqlOrder);
        ResultSet resultSet = DBUtils.executeSql(sql);
        System.out.println("--------------leave_info---------------");
        System.out.println("employee_id employee_name  department_id  begin end leave_type status           reason          reject_reason");
        while(resultSet.next()) {
            dumpLeave(resultSet);
        }
        return 0;
    }
    public int queryTrip(int ID, int departID, String dateBegin, String dateEnd,int status,int TripType,
                          String order, String groupBy, String agFunc, int oderByCount) throws Exception {
        String sqlDayBegin = "";
        String sqlDayEnd = "";
        String sqlEmployeeID = "";
        String sqlDepartmentID = "";
        String sqlTripType = "";
        String sqlStatus = "";
        String sqlOrder = "";
        if (!dateBegin.equals("")&&!dateEnd.equals("")) {
            sqlDayBegin = "AND begin <= \'" + dateEnd + "\'";
            sqlDayEnd = "OR end >= \'" + dateBegin + "\'";
        }
        if (TripType!=-1){
            sqlTripType = "AND trip_type = "+TripType;
        }
        if (ID!=-1)
            sqlEmployeeID = "AND trip.employee_id = "+ID;
        if (departID!=-1)
            sqlDepartmentID = "AND department_id = "+departID;
        if (status!=-1)
            sqlStatus = "AND status = "+status;

        if (!order.equals(""))
            sqlOrder = "ORDER BY "+order;
        if (!groupBy.equals("")) {
            if (!dateBegin.equals("")&&!dateEnd.equals("")) {
                sqlDayEnd = "AND end <= \'" + dateEnd + "\'";
                sqlDayBegin = "AND begin >= \'" + dateBegin + "\'";
            }
            if (oderByCount==0){
                sqlOrder = "ORDER  BY count asc";
            }else if (oderByCount==1){
                sqlOrder = "ORDER BY count desc";
            }
            String sql = String.format("SELECT %s, %s AS count FROM (SELECT %s, trip.employee_id, end-begin AS day" +
                            "FROM trip, employee WHERE trip.employee_id = employee.employee_id" +
                            " %s %s %s %s %s %s ) GROUP BY %s  %s",
                    groupBy, agFunc, groupBy, sqlEmployeeID, sqlDepartmentID, sqlTripType, sqlDayBegin, sqlDayEnd, sqlStatus,
                    groupBy, sqlOrder);
            ResultSet resultSet = DBUtils.executeSql(sql);
            System.out.println("--------------trip_info---------------");
            System.out.printf("%s        count\n",groupBy);
            while(resultSet.next()) {
                dumpGroupTrip(resultSet, groupBy);
            }
            return 0;
        }
        String sql = String.format("SELECT * FROM trip, employee WHERE trip.employee_id = employee.employee_id" +
                        " %s %s %s %s %s %s %s",
                sqlEmployeeID, sqlDepartmentID, sqlTripType, sqlDayBegin, sqlDayEnd, sqlStatus, sqlOrder);
        ResultSet resultSet = DBUtils.executeSql(sql);
        System.out.println("--------------trip_info---------------");
        System.out.println("employee_id employee_name  department_id  begin end Trip_type status           business          reject_reason");
        while(resultSet.next()) {
            dumpTrip(resultSet);
        }
        return 0;
    }
}
