package demo;

import org.junit.Test;
import util.JDBCUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author:L1ANN
 * @Description:
 * @Date:Created in 11:05 2017/11/1
 * @Modified By:
 */
public class JdbcOperaClob {
    /**
     * 使用数据库操作大文本数据
     */
    @Test
    public void add(){
        Connection conn =null ;
        PreparedStatement st = null;
        ResultSet rs = null;
        Reader reader = null;
        try{
            conn = JDBCUtils.getConnection();
            String sql = "insert into testclob(resume) values(?)";
            st = conn.prepareStatement(sql);
            //获取文本的字符输入流
            File file = new File(JdbcOperaClob.class.getClassLoader().getResource("thework.txt").getFile());
            reader = new FileReader(file);
            //设置SQL的参数
            st.setCharacterStream(1,reader,(int)file.length());
            int num = st.executeUpdate();
            if(num>0){
                System.out.println("插入成功！");
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            JDBCUtils.release(conn,st,rs);
        }
    }

    /**
     * 读取数据库中的大文本数据
     */
    @Test
    public void read(){
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs= null;
        try{
            conn = JDBCUtils.getConnection();
            String sql ="select resume from testclob where id=1";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            String contentStr ="";
            String content = "";
            if(rs.next()){
                //使用resultSet.getString("字段名")获取大文本数据的内容
                content = rs.getString("resume");
                //使用resultSet.getCharacterStream("字段名")获取大文本数据的内容
                Reader reader = rs.getCharacterStream("resume");
                char buffer[] = new char[1024];
                int len=0;
                FileWriter writer = new FileWriter(JdbcOperaClob.class.getClassLoader().getResource("1.txt").getFile());
                while((len=reader.read(buffer))!=-1){
                    contentStr+=new String(buffer);
                    writer.write(buffer,0,len);
                }
                writer.close();
                reader.close();
            }
            System.out.println(content);
            System.out.println("--------------------------------");
            System.out.println(contentStr);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
