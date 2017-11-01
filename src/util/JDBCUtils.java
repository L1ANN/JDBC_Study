package util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 10:37 2017/11/1
 * @Modified By:
 */
public class JDBCUtils {
    private static String driver = null;
    private static String url = null;
    private static String username = null;
    private static String password = null;

    static {
        try {
            //读取db.properties文件中的数据库连接信息
            InputStream in = JDBCUtils.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(in);

            //读取数据库连接驱动
            driver = properties.getProperty("driver");
            //读取数据库连接URL地址
            url = properties.getProperty("url");
            //读取数据库连接用户名
            username = properties.getProperty("username");
            //读取数据库连接密码
            password = properties.getProperty("password");

            //加载数据库驱动
            Class.forName(driver);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接对象
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,username,password);
    }

    public static void release(Connection conn, Statement st, ResultSet rs){
        if(rs!=null){
            try{
                //关闭存储查询结果的ResultSet对象
                rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            rs = null;
        }

        if(st!=null){
            try {
                //关闭负责执行SQL命令的Statement对象
                st.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        if(conn!=null){
            try{
                //关闭Connection数据库连接对象
                conn.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
