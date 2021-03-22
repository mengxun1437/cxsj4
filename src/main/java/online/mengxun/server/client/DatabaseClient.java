package online.mengxun.server.client;

import com.alibaba.fastjson.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseClient {

    public static Connection getConnection(String dbIp,String dbPort,String dbName,String dbUser,String dbPwd,String dbDriver,String dbType) {
        Connection con = null; // 声明连接
        try {
            switch(dbType){
                case "mysql":
                case "postgresql":
                    Class.forName(dbDriver);
                    con = DriverManager.getConnection("jdbc:"+dbType+"://"+dbIp+":"+dbPort+"/"+dbName, dbUser, dbPwd);
                    break;
                case "sqlserver":
                    Class.forName(dbDriver);
                    con = DriverManager.getConnection("jdbc:"+dbType+"://"+dbIp+":"+dbPort+";DatabaseName="+dbName,dbUser,dbPwd);
                    break;
                case "oracle":
                    Class.forName(dbDriver);
                    con = DriverManager.getConnection("jdbc:"+dbType+":thin:@"+dbIp+":"+dbPort+"/"+dbName,dbUser,dbPwd);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }


    public static List selectData(Connection con,String sql){
        Statement st = null;
        ResultSet rs = null;
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            return mapper(rs);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
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

    /**
     *
     * @param rSet
     * @return
     * @throws SQLException
     */
    public static List mapper(ResultSet rSet){
        try{
            List list = new ArrayList();
            while(rSet.next()){
                JSONObject jsonObject = new JSONObject();
                ResultSetMetaData metaData = rSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnType = metaData.getColumnTypeName(i);
                    switch (columnType){
                        case "INT":
                            Integer valueInt = rSet.getInt(columnName);
                            jsonObject.put(columnName, valueInt);
                            break;
                        case "BIT":
                            Boolean valueBoolen = rSet.getBoolean(columnName);
                            jsonObject.put(columnName,valueBoolen);
                            break;
                        default:
                            String valueString = rSet.getString(columnName);
                            jsonObject.put(columnName,valueString);
                            break;
                    }
                }
                list.add(jsonObject);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList();
        }

    }


}
