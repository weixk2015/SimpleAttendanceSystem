package main.java.entity;

import main.java.Exception.NoSuchUserException;
import main.java.Exception.PermisionDeniedException;
import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by devilpi on 06/01/2018.
 */
public class DepartmentManager extends Manager {
    int departmentID;
    public DepartmentManager(User user) {
        this.age = user.age;
        this.employeeId = user.employeeId;
        this.name = user.name;
        this.password = user.password;
        this.type = TYPE.EMPLOYEE;
        try {
            this.departmentID = queryDepartmentID();
        } catch (NoSuchUserException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void checkApply() throws Exception {
        String sql = String.format("SELECT leave_info.apply_id,leave_info.begin, leave_info.end, leave_info.employee_id," +
                "leave_info.leave_type, leave_info.reason FROM leave_info,employee,department" +
                " WHERE employee.department_id = department.department_id AND department.manager_id = %d" +
                "AND leave_info.status = 0 AND employee.employee_id = leave_info.employee_id ",employeeId);
        ResultSet resultSet = DBUtils.executeSql(sql);
        System.out.println("-------------leave_info--------------");
        System.out.println("apply_id       begin       end        employee_id      leave_type       reason");
        while(resultSet.next()) {
            dumpLeaveInfo(resultSet);
        }
        System.out.println("--------------trip_info---------------");
        String sql1 = String.format("SELECT trip.apply_id,trip.begin, trip.end, trip.employee_id," +
                "trip.trip_type, trip.business FROM trip,employee,department" +
                " WHERE employee.department_id = department.department_id AND department.manager_id = %d" +
                "AND trip.status = 0 AND employee.employee_id = trip.employee_id ",employeeId);
        System.out.println("apply_id       begin       end        employee_id      trip_type       business");
        ResultSet resultSet1 = DBUtils.executeSql(sql1);
        while(resultSet1.next()) {
            dumpTrips(resultSet);
        }
    }
    public void dumpLeaveInfo(ResultSet resultSet) throws SQLException {
        System.out.printf("%-8d %-10s %-15s %-8d %8s %8s", resultSet.getInt("apply_id"),
                resultSet.getString("begin"), resultSet.getString("end"),
                resultSet.getInt("employee_id"), resultSet.getInt("leave_type"),
                resultSet.getString("reason"));
    }
    public void dumpTrips(ResultSet resultSet) throws SQLException {
        System.out.printf("%-8d %-10s %-15s %-8d %8s %8s", resultSet.getInt("apply_id"),
                resultSet.getString("begin"), resultSet.getString("end"),
                resultSet.getInt("employee_id"), resultSet.getInt("trip_type"),
                resultSet.getString("business"));
    }
    public void permitLeave(int apply_id) throws Exception {
        String sql =  String.format("UPDATE leave_info SET status = 1" +
                "WHERE apply_id = %d ",apply_id);
        DBUtils.executeUpdate(sql);
    }
    public void permitTrip(int apply_id) throws Exception {
        String sql =  String.format("UPDATE trip SET status = 1" +
                "WHERE apply_id = %d ",apply_id);
        DBUtils.executeUpdate(sql);
    }
    public void rejectLeave(int apply_id, String reason) throws Exception {
        String sql =  String.format("UPDATE leave_info SET status = 2,reject_reason = %s" +
                "WHERE apply_id = %d ",reason, apply_id);
        DBUtils.executeUpdate(sql);
    }
    public void rejectTrip(int apply_id, String reason) throws Exception {
        String sql =  String.format("UPDATE trip SET status = 2,reject_reason = %s" +
                "WHERE apply_id = %d ",reason, apply_id);
        DBUtils.executeUpdate(sql);
    }
    public void queryEmployeeById(int id) throws Exception {

        String sql = "SELECT * FROM user WHERE employee_id = "+id;
        ResultSet resultSet = DBUtils.executeSql(sql);
        int type = resultSet.getInt("type");
        if (type!=0)
            throw new PermisionDeniedException();
        String employeeSql = "SELECT * FROM employee WHERE employee_id = "+id;
        ResultSet resultSet1 = DBUtils.executeSql(employeeSql);
        if (resultSet1.getInt("department_id")!=departmentID)
            throw new PermisionDeniedException();
        System.out.println("id       name        age      type");
        while(resultSet.next()) {
            dumpUserInfo(resultSet);
        }
    }
    public void queryEmployeeByName(String name) throws Exception {
        String sql = String.format("SELECT * FROM user WHERE name = \'%s\'",name );
        ResultSet resultSet = DBUtils.executeSql(sql);
        int type = resultSet.getInt("type");
        if (type!=0)
            throw new PermisionDeniedException();
        String employeeSql = "SELECT * FROM employee WHERE employee_id = "+resultSet.getInt("employee_id");
        ResultSet resultSet1 = DBUtils.executeSql(employeeSql);
        if (resultSet1.getInt("department_id")!=departmentID)
            throw new PermisionDeniedException();
        System.out.println("id       name        age      type");
        while(resultSet.next()) {
            dumpUserInfo(resultSet);
        }
    }
    private int queryDepartmentID() throws SQLException, NoSuchUserException {

        String sql =  String.format( "SELECT department_id FROM department WHERE manager_id = %d",employeeId);
        ResultSet resultSet = null;

        try {
            resultSet = DBUtils.executeSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resultSet.next())
            return resultSet.getInt(1);
        else
            throw new NoSuchUserException();

    }

    public int queryAttendance(int ID, int departID, String dateBegin, String dateEnd,int status,
                               String order, String groupBy, String countArg, int oderByCount) throws Exception {
        if (departID!=-1&&departID!=departmentID)
            throw new PermisionDeniedException();
        else
            departID = departmentID;
        if (ID!=-1){
            String employeeSql = "SELECT * FROM employee WHERE employee_id = "+ID;
            ResultSet resultSet1 = DBUtils.executeSql(employeeSql);
            if (resultSet1.getInt("department_id")!=departmentID)
                throw new PermisionDeniedException();
        }
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
                sqlOrder = "ORDER BY "+groupBy+" asc";
            }else if (oderByCount==1){
                sqlOrder = "ORDER BY "+groupBy+" desc";
            }
            if (!countArg.equals("*")){
                countArg = "DISTINCT "+countArg;
            }
            String sql = String.format("SELECT %s, count FROM (SELECT %s, COUNT(%s) FROM attendance, employee WHERE attendance.employee_id = employee.employee_id" +
                            " %s %s %s %s %s GROUP BY %s ) AS %s_count(%s,count) ORDER BY %s",
                    groupBy, groupBy, countArg, sqlEmployeeID, sqlDepartmentID, sqlDayBegin, sqlDayEnd, sqlStatus, groupBy,
                    groupBy, groupBy, sqlOrder);
            ResultSet resultSet = DBUtils.executeSql(sql);
            System.out.println("--------------attendances_info---------------");
            System.out.printf("%s        count\n",groupBy);
            while(resultSet.next()) {
                dumpGroupAttendance(resultSet, groupBy);
            }
            return 0;
        }
        String sql = String.format("SELECT * FROM attendance, employee WHERE attendance.employee_id = employee.employee_id" +
                        " %s %s %s %s %s %s ",
                sqlEmployeeID, sqlDepartmentID, sqlDayBegin, sqlDayEnd, sqlStatus, sqlOrder);
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
        if (departID!=-1&&departID!=departmentID)
            throw new PermisionDeniedException();
        else
            departID = departmentID;
        if (ID!=-1){
            String employeeSql = "SELECT * FROM  employee WHERE employee_id = "+ID;
            ResultSet resultSet1 = DBUtils.executeSql(employeeSql);
            if (resultSet1.getInt("department_id")!=departmentID)
                throw new PermisionDeniedException();
        }
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
                sqlOrder = "ORDER BY "+groupBy+" asc";
            }else if (oderByCount==1){
                sqlOrder = "ORDER BY "+groupBy+" desc";
            }
            String sql = String.format("SELECT %s, count FROM (SELECT %s, %s FROM (SELECT %s, leave_info.employee_id, end-begin " +
                            "FROM leave_info, employee WHERE leave_info.employee_id = employee.employee_id" +
                            " %s %s %s %s %s %s ) AS days(%s, employee_id, day) GROUP BY %s ) AS %s_count(%s,count) ORDER BY %s",
                    groupBy, groupBy, agFunc, groupBy, sqlEmployeeID, sqlDepartmentID, sqlLeaveType, sqlDayBegin, sqlDayEnd, sqlStatus,
                    groupBy, groupBy, groupBy, groupBy, sqlOrder);
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
        if (departID!=-1&&departID!=departmentID)
            throw new PermisionDeniedException();
        else
            departID = departmentID;
        if (ID!=-1){
            String employeeSql = "SELECT * FROM employee  WHERE employee_id = "+ID;
            ResultSet resultSet1 = DBUtils.executeSql(employeeSql);
            if (resultSet1.getInt("department_id")!=departmentID)
                throw new PermisionDeniedException();
        }
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
                sqlOrder = "ORDER BY "+groupBy+" asc";
            }else if (oderByCount==1){
                sqlOrder = "ORDER BY "+groupBy+" desc";
            }
            String sql = String.format("SELECT %s, count FROM (SELECT %s, %s FROM (SELECT %s, trip.employee_id, end-begin " +
                            "FROM trip, employee WHERE trip.employee_id = employee.employee_id" +
                            " %s %s %s %s %s %s ) AS days(%s, employee_id, day) GROUP BY %s ) AS %s_count(%s,count) ORDER BY %s",
                    groupBy, groupBy, agFunc, groupBy, sqlEmployeeID, sqlDepartmentID, sqlTripType, sqlDayBegin, sqlDayEnd, sqlStatus,
                    groupBy, groupBy, groupBy, groupBy, sqlOrder);
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
