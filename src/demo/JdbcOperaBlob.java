package demo;

import org.junit.Test;
import util.JDBCUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 12:06 2017/11/1
 * @Modified By:
 */
public class JdbcOperaBlob {
    /**
     * 向数据库中插入二进制数据
     */
    @Test
    public void add() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into testblob(image) values(?)";
            st = conn.prepareStatement(sql);
            //获取image的输入流
            File file = new File(JdbcOperaBlob.class.getClassLoader().getResource("time.png").getFile());
            InputStream in = new FileInputStream(file);
            st.setBinaryStream(1, in, file.length());
            int num = st.executeUpdate();
            if (num > 0) {
                System.out.println("插入成功！");
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.release(conn, st, rs);
        }
    }

    /**
     * 读取数据库中的二进制数据
     */
    @Test
    public void read() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select image from testblob where id=2";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            if (rs.next()) {
                //InputStream in = rs.getBlob("image").getBinaryStream();这种方法也可以
                InputStream in = rs.getBinaryStream("image");
                byte[] buffer = new byte[1024];
                int len = 0;

                File file = new File("2.png");
                if (!file.exists()) {
                    file.createNewFile();
                }
                OutputStream out = new FileOutputStream(file);
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                System.out.println("读取成功！");
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.release(conn, st, rs);
        }
    }
}
