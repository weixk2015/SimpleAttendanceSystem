package main.java.entity;

import main.java.Exception.DuplicateException;
import main.java.Exception.NoSuchUserException;
import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by devilpi on 06/01/2018.
 */
public class Employee extends User {
    int departmentID;
    public Employee(User user) throws SQLException {
        this.age = user.age;
        this.employeeId = user.employeeId;
        this.name = user.name;
        this.password = user.password;
        this.type = TYPE.EMPLOYEE;
        try {
            this.departmentID = queryDepartmentID();
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }
    }
    private int queryDepartmentID() throws SQLException, NoSuchUserException {

        String sql =  String.format( "SELECT department_id FROM employee WHERE employee_id = %d",employeeId);
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
    public boolean checkIn() throws DuplicateException {
        log("checkin","");
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String sql = String.format("INSERT INTO attendance (employee_id, date, sign_in_time) " +
                "VALUES (%d, \'%s\', \'%s\')",employeeId,sdf.format(date),sdf1.format(date));
        //System.out.println(sql);
        try {
            DBUtils.executeUpdate(sql);
        } catch (Exception e) {
            throw new DuplicateException();
        }
        return true;
    }
    public boolean checkOff() throws SQLException {
        log("checkout","");
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String sql = String.format("SELECT * FROM attendance  " +
                "WHERE employee_id = %d AND date = \'%s\'",employeeId,sdf.format(date));
       // System.out.println(sql);
        ResultSet resultSet = null;
        try {
            resultSet = DBUtils.executeSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql1;
        if (!resultSet.next()){
            sql1 = String.format("INSERT INTO attendance (employee_id, date, sign_off_time) " +
                    "VALUES (%d, \'%s\', \'%s\')",employeeId,sdf.format(date),sdf1.format(date));
        }else {
            sql1 =  String.format("UPDATE attendance SET sign_off_time = " +
                    "\'%s\' WHERE employee_id = %d AND date = \'%s\'",sdf1.format(date),employeeId,sdf.format(date));
        }
        //System.out.println(sql1);
        try {
            DBUtils.executeUpdate(sql1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public int askLeave(String begin, String end, int type, String reason){
        log("submit_leave","");
        String sql = String.format("INSERT INTO leave_info ( begin, end, leave_type, reason, employee_id) " +
                "VALUES (\'%s\', \'%s\', %d, \'%s\', %d)", begin, end, type, reason, employeeId);
        //System.out.println(sql);

        try {
            return DBUtils.executeIncInsert(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;

    }
    public int askTrip(String begin, String end, int type, String business){
        log("submit_trip","");
        String sql = String.format("INSERT INTO trip ( begin, end, trip_type, business, employee_id) " +
                "VALUES (\'%s\', \'%s\', %d, \'%s\', %d)", begin, end, type, business, employeeId);
        //System.out.println(sql);


        try {
            return DBUtils.executeIncInsert(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
    public int queryLeave(int order, int dayUP, int dayLow) throws Exception {
        if (dayUP<=dayLow)
            return 0;
        String sqlOrder;
        String sqlDayUp = "";
        String sqlDayLow = "";
        if (order==0)
            sqlOrder = "asc";
        else
            sqlOrder = "desc";
        if (dayUP!=-1)
            sqlDayUp = "AND end - begin <= "+dayUP;
        if (dayLow!=-1)
            sqlDayLow = "AND end - begin >= "+dayLow;
        String sql = String.format("SELECT * FROM leave_info WHERE employee_id = %d %s %s ORDER BY begin %s",
                employeeId, sqlDayUp, sqlDayLow, sqlOrder);
        ResultSet resultSet = DBUtils.executeSql(sql);
        System.out.println("-------------leave_info--------------");
        System.out.println("apply_id       begin       end     leave_type       status       reject_reason       reason ");
        while(resultSet.next()) {
            dumpLeaveInfo(resultSet);
        }
        return 0;
    }
    public int queryTrip(int order, int dayUP, int dayLow) throws Exception {
        if (dayUP<=dayLow)
            return 0;
        String sqlOrder;
        String sqlDayUp = "";
        String sqlDayLow = "";
        if (order==0)
            sqlOrder = "asc";
        else
            sqlOrder = "desc";
        if (dayUP!=-1)
            sqlDayUp = "AND end - begin <= "+dayUP;
        if (dayLow!=-1)
            sqlDayLow = "AND end - begin >= "+dayLow;
        String sql = String.format("SELECT * FROM trip WHERE employee_id = %d %s %s ORDER BY begin %s",
                employeeId, sqlDayUp, sqlDayLow, sqlOrder);
        ResultSet resultSet = DBUtils.executeSql(sql);
        System.out.println("--------------trip_info---------------");
        System.out.println("apply_id       begin       end      trip_type       status       reject_reason       business");
        while(resultSet.next()) {
            dumpTrip(resultSet);
        }
        return 0;
    }
    public void dumpLeaveInfo(ResultSet resultSet) throws SQLException {
        System.out.printf("%-8d %-10s %-15s %8s %8s %8s %8s", resultSet.getInt("apply_id"),
                resultSet.getString("begin"), resultSet.getString("end"),
                resultSet.getInt("leave_type"),
                Status.values()[resultSet.getInt("status")],
                resultSet.getString("reject_reason"),
                resultSet.getString("reason"));
    }
    public void dumpTrip(ResultSet resultSet) throws SQLException {
        System.out.printf("%-8d %-10s %-15s %8s %8s %8s %8s", resultSet.getInt("apply_id"),
                resultSet.getString("begin"), resultSet.getString("end"),
                Status.values()[resultSet.getInt("status")],
                resultSet.getString("reject_reason"),
                resultSet.getInt("trip_type"),
                resultSet.getString("business"));
    }

}
