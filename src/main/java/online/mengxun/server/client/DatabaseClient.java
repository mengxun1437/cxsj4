package online.mengxun.server.client;

import com.alibaba.fastjson.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseClient {

    public static Connection getConnection(String dbIp, String dbPort, String dbName, String dbUser, String dbPwd, String dbDriver, String dbType) {
        Connection con = null; // 声明连接
        try {
            switch (dbType) {
                case "mysql":
                case "postgresql":
                    Class.forName(dbDriver);
                    con = DriverManager.getConnection("jdbc:" + dbType + "://" + dbIp + ":" + dbPort + "/" + dbName, dbUser, dbPwd);
                    break;
                case "sqlserver":
                    Class.forName(dbDriver);
                    con = DriverManager.getConnection("jdbc:" + dbType + "://" + dbIp + ":" + dbPort + ";DatabaseName=" + dbName, dbUser, dbPwd);
                    break;
                case "oracle":
                    Class.forName(dbDriver);
                    con = DriverManager.getConnection("jdbc:" + dbType + ":thin:@" + dbIp + ":" + dbPort + "/" + dbName, dbUser, dbPwd);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }


    public static List selectData(Connection con, String sql) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            return mapper(rs);
        }
        catch (SQLException e) {
            e.printStackTrace();
            String tableReg = "Table (.*) doesn't exist";
            String columnReg = "Unknown column (.*) in (.*)";
            String nearReg = "You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near (.*)";
            if (e.getMessage().matches(tableReg)) {
                Pattern pt1 = Pattern.compile(tableReg);
                Matcher m1 = pt1.matcher(e.getMessage());
                if (m1.find()) {
                    throw new SQLException(m1.group(1) + "表不存在;推荐选择:"+getAllTableFromDatabase(con).toString());
                }
            }
            else if (e.getMessage().matches(columnReg)) {
                Pattern pt2 = Pattern.compile(columnReg);
                Matcher m2 = pt2.matcher(e.getMessage());
                if (m2.find()) {
                    throw new SQLException(m2.group(1) + "列不存在;推荐选择:"+getAllColFromTable(con,"user").toString());
                }
            }else if(e.getMessage().matches(nearReg)){
                Pattern pt3 = Pattern.compile(nearReg);
                Matcher m3 = pt3.matcher(e.getMessage());
                if (m3.find()) {
                    throw new SQLException(m3.group(1).split(" ")[0] + "附近有错误");
                }
            }
            return new ArrayList();
        }
        finally {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取数据库中某个表中所有字段
     */
    public static List getAllColFromTable(Connection conn,String tableName) throws Exception {
        try {
            List list = new ArrayList();
            ResultSet rs = conn.getMetaData().getColumns(null,"%", tableName,"%");
            while(rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                list.add(columnName);
            }


            return list;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList();
        }
    }

    /**
     * 获取数据库中所有的表名
     * @param
     * @return
     */
    public static List getAllTableFromDatabase(Connection conn){
        try{
            List list = new ArrayList();
            String catalog = conn.getCatalog(); //catalog 其实也就是数据库名
            ResultSet tablesResultSet = conn.getMetaData().getTables(catalog,null,null,new String[]{"TABLE"});
            while(tablesResultSet.next()){
                String tableName = tablesResultSet.getString("TABLE_NAME");
                list.add(tableName);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList();
        }
    }

        /**
         *
         * @param rSet
         * @return
         * @throws SQLException
         */
        public static List mapper (ResultSet rSet){
            try {
                List list = new ArrayList();
                while (rSet.next()) {
                    JSONObject jsonObject = new JSONObject();
                    ResultSetMetaData metaData = rSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        String columnType = metaData.getColumnTypeName(i);
                        switch (columnType) {
                            case "INT":
                                Integer valueInt = rSet.getInt(columnName);
                                jsonObject.put(columnName, valueInt);
                                break;
                            case "BIT":
                                Boolean valueBoolen = rSet.getBoolean(columnName);
                                jsonObject.put(columnName, valueBoolen);
                                break;
                            default:
                                String valueString = rSet.getString(columnName);
                                jsonObject.put(columnName, valueString);
                                break;
                        }
                    }
                    list.add(jsonObject);
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList();
            }

        }



}
