package main.java.controller;/*
 * Created by xk on 2018/1/10 14.
 */

import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendanceExaminer {

    static String office_begin;
    static String office_end;
    public static void main(String[] args) throws Exception {
        String officeTimeSql = "SELECT * FROM config";
        ResultSet timeResult = DBUtils.executeSql(officeTimeSql);
        if (timeResult.next()){
            office_begin = timeResult.getString("office_begin");
            office_end = timeResult.getString("office_end");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
        Date date = new Date();
        String sql = String.format("SELECT * FROM holidays  " +
                "WHERE day = \'%s\'",sdf.format(date));
        // System.out.println(sql);
        ResultSet resultSet = null;
        try {
            resultSet = DBUtils.executeSql(sql);
            if (resultSet.next()){
                int isHoliday = resultSet.getInt("is_holiday");
                if (isHoliday==0){
                    String sql1 = "SELECT * FROM employee";
                    ResultSet resultSet1 = DBUtils.executeSql(sql1);
                    while (resultSet1.next()){
                        examineEmployee(resultSet1.getInt("employee_id"), sdf.format(date));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void examineEmployee(int employee_id, String date) throws Exception {
        boolean isLeave = false;
        boolean isTrip = false;
        String leave_sql = String.format("SELECT * FROM leave_info WHERE employee_id = %d AND status = 1 AND begin <= \'%s\' AND end >= \'%s\'",employee_id,date,date);
        ResultSet resultSet = DBUtils.executeSql(leave_sql);
        if (resultSet.next())
            isLeave = true;
        else {
            String trip_sql = String.format("SELECT * FROM trip WHERE employee_id = %d AND status = 1 AND begin <= \'%s\' AND end >= \'%s\'",employee_id,date,date);
            ResultSet resultSet1 = DBUtils.executeSql(trip_sql);
            if (resultSet1.next())
                isTrip = true;
        }
        String attendance_sql = String.format("SELECT * FROM attendance WHERE employee_id = %d AND date = \'%s\'",employee_id,date);
        ResultSet attendance_result = DBUtils.executeSql(attendance_sql);
        if (attendance_result.next()){
            int status = 0;
            if (isLeave)
                status = 4;
            if (isTrip)
                status = 5;
            String sign_in = attendance_result.getString("sign_in_time");
            String sign_off = attendance_result.getString("sign_off_time");
            if (sign_in.equals("null")||sign_in.equals("")||java.sql.Time.valueOf(sign_in).after(java.sql.Time.valueOf(office_begin)))
                status = 1;
            else if (sign_off.equals("null")||sign_off.equals("")||java.sql.Time.valueOf(office_end).after(java.sql.Time.valueOf(sign_off)))
                status = 2;
            String updateSql = String.format("UPDATE attendance SET status = %d WHERE employee_id = %d AND " +
                    "date = \'%s\'",status,employee_id,date);
            DBUtils.executeUpdate(updateSql);
        }else {
            int status = 3;
            if (isLeave)
                status = 4;
            if (isTrip)
                status = 5;
            String insert_sql = String.format("INSERT INTO attendance (employee_id, date, status) VALUES (%d, \'%s\', %d)",employee_id,date,status);
            DBUtils.executeUpdate(insert_sql);
        }

    }

}
