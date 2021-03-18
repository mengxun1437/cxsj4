package online.mengxun.server.client;

import java.sql.*;
import java.util.List;

public class DatabaseClient {
    public static Connection getConnection(String dbIp,String dbPort,String dbName,String dbUser,String dbPwd,String dbDriver,String dbType) {
        Connection con = null; // 声明连接
        try {
            Class.forName(dbDriver);
            // 声明驱动
            con = DriverManager.getConnection("jdbc:"+dbType+"://"+dbIp+":"+dbPort+"/"+dbName, dbUser, dbPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }


    public static List selectData(String dbIp,String dbPort,String dbName,String dbUser,String dbPwd,String dbDriver,String dbType,String sql) {
        Connection con = DatabaseClient.getConnection(dbIp,dbPort,dbName,dbUser, dbPwd, dbDriver, dbType);
        Statement st = null;
        ResultSet rs = null;
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            List list = null;
            while (rs.next()) {
                list.add(rs);
            }
            return list;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
