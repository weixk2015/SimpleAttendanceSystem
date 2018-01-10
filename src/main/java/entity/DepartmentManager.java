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
            dumpTrip(resultSet);
        }
    }
    public void dumpLeaveInfo(ResultSet resultSet) throws SQLException {
        System.out.printf("%-8d %-10s %-15s %-8d %8s %8s", resultSet.getInt("apply_id"),
                resultSet.getString("begin"), resultSet.getString("end"),
                resultSet.getInt("employee_id"), resultSet.getInt("leave_type"),
                resultSet.getString("reason"));
    }
    public void dumpTrip(ResultSet resultSet) throws SQLException {
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
}
