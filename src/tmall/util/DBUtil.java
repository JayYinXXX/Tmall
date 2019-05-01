package tmall.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    static String ip = "127.0.0.1";
    static int port = 3306;
    static String database = "tmall";
    static String encoding = "UTF-8";
    static String loginName = "root";
    static String password = "admin";
    // 加载这个类进入内存
    // Class.forName是把这个类加载到JVM中，创建自己的实例静态初始化并向DriverManager注册（也有其他方法可以实现）
    // 加载的时候就会执行其中的静态初始化块（静态初始化块中包含了向DriverManager注册的代码），完成驱动的初始化的相关工作
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    // 连接数据库
    public static Connection getConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d:%s?characterEncoding=%s",
                ip, port, database, encoding);
        return DriverManager.getConnection(url, loginName, password);
    }

    public static void main(String[] args)throws SQLException {
        System.out.println(getConnection());
    }
}
