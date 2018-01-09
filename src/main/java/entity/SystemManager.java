package main.java.entity;

import main.java.Exception.IllegalParameterException;
import main.java.dao.DBUtils;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by devilpi on 06/01/2018.
 */
public class SystemManager extends User {
    public void showLog(String start, String end) throws Exception, IllegalParameterException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Timestamp startTime = null, endTime = null;
        try {
            startTime = new Timestamp(sdf.parse(start).getTime());
            endTime = new Timestamp(sdf.parse(end).getTime());
        } catch (Exception e) {
            throw new IllegalParameterException();
        }

        String sql = String.format("SELECT * FROM log WHERE time BETWEEN \'%s\' and \'%s\'", sdf.format(startTime), sdf.format(endTime));
        ResultSet resultSet = DBUtils.executeSql(sql);
        while(resultSet.next()) {
            dumpLog(resultSet);
        }
    }

    public void dumpLog(ResultSet resultSet) throws Exception {
        System.out.printf("%-5d %s %-10s %-10s", resultSet.getInt("employee_id"),
                resultSet.getString("time"), resultSet.getString("action"), resultSet.getString("description"));
    }
}
