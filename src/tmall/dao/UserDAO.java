package tmall.dao;

import tmall.bean.User;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    // 获取记录总数
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select count(*) from User";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // C
    public void add(User bean) {
        String sql = "insert into user valus(null, ?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getName());
            ps.setString(2, bean.getPassword());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // R
    public User get(int id) {
        User bean = null;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select * from user where id = " + id;
            ResultSet rs = s.executeQuery(sql);
            if(rs.next()) {
                bean = new User();
                String name = rs.getString("name");
                String password = rs.getString("password");
                bean.setName(name);
                bean.setPassword(password);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public List<User> list(int start, int count) {
        List<User> beans = new ArrayList<User>();
        String sql = "select * from user order by id desc limit ?, ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User bean = new User();
                int id = rs.getInt(1);
                String name = rs.getString("name");
                String password = rs.getString("password");
                bean.setId(id);
                bean.setName(name);
                bean.setPassword(password);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public List<User> list() {
        return list(0, Short.MAX_VALUE);
    }

    // U
    public void update (User bean) {
        String sql = "update user set name = ?, password = ? where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getName());
            ps.setString(2, bean.getPassword());
            ps.setInt(3, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // D
    public void delete(int id) {
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "delete from user where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 非CRUD方法
    // 根据用户名获取对象
    public User get(String name) {
        User bean = null;
        String sql = "select from user where name = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bean = new User();
                int id = rs.getInt(1);
                String password = rs.getString("password");
                bean.setId(id);
                bean.setPassword(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    // 根据用户名/密码获取对象
    public User get(String name, String password) {
        User bean = null;
        String sql = "select * from user where name = ? and password = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bean = new User();
                int id = rs.getInt(1);
                bean.setId(id);
                bean.setName(name);
                bean.setPassword(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    // 输入一个用户名 检查用户是否存在
    public boolean isExist(String name) {
        User bean = get(name);  // 用上面的方法获取对象 成功获取则存在
        return bean != null;
    }

}
