package main.java.entity;

import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by devilpi on 06/01/2018.
 */
public class DepartmentManager extends Manager {
    public enum Status{waiting, permited, rejected};
    void checkApply() throws Exception {
        String sql = "SELECT * FROM leave_info WHERE status = 0";
        ResultSet resultSet = DBUtils.executeSql(sql);
        System.out.println("-------------leave_info--------------");
        System.out.println("apply_id       begin       end        employee_id      leave_type       reason");
        while(resultSet.next()) {
            dumpLeaveInfo(resultSet);
        }
        System.out.println("--------------trip_info---------------");
        String sql1 = "SELECT * FROM trip WHERE status = 0";
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
}
