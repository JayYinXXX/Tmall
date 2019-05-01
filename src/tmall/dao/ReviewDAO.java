package tmall.dao;

import tmall.bean.Product;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ReviewDAO {
    // CRUD
    // C
    public void add(Review bean) {
        String sql = "insert into Review values(null,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, bean.getContent());
            ps.setInt(2, bean.getUser().getId());
            ps.setInt(3, bean.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));

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
    // D
    public void delete(int id) {
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "delete from Review where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // U
    public void update(Review bean) {
        String sql = "update Review set content= ?, uid=?, pid=? , createDate = ? where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getContent());
            ps.setInt(2, bean.getUser().getId());
            ps.setInt(3, bean.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t( bean.getCreateDate()) );
            ps.setInt(5, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    // R
    // 根据pid 统计某一Product的评论总数
    public int getCount(int pid) {
        int count = 0;
        String sql = "select count(*) from Review where pid = ? ";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // 统计所有评论数
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select count(*) from review";
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // 根据id 返回某条Review记录
    public Review get(int id) {
        Review bean = new Review();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select * from Review where id = " + id;
            ResultSet rs = s.executeQuery(sql);

            if (rs.next()) {
                String content = rs.getString("content");
                int uid = rs.getInt("uid");
                User user = new UserDAO().get(uid);
                int pid = rs.getInt("pid");
                Product product = new ProductDAO().get(pid);
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                bean.setId(id);
                bean.setContent(content);
                bean.setUser(user);
                bean.setProduct(product);
                bean.setCreateDate(createDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    // 根据pid 返回多条Review
    public List<Review> list(int pid, int start, int count) {
        List<Review> beans = new ArrayList<>();
        String sql = "select * from Review where pid = ? order by id desc limit ?,? ";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, pid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review bean = new Review();

                int id = rs.getInt(1);
                String content = rs.getString("content");
                int uid = rs.getInt("uid");
                User user = new UserDAO().get(uid);
                Product product = new ProductDAO().get(pid);
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                bean.setId(id);
                bean.setContent(content);
                bean.setUser(user);
                bean.setProduct(product);
                bean.setCreateDate(createDate);

                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }
    // 根据pid 返回相应的所有Review记录
    public List<Review> list(int pid) {
        return list(pid, 0, Short.MAX_VALUE);
    }

    // 根据content pid 看是否存在对应的记录
    public boolean isExist(String content, int pid) {
        String sql = "select * from Review where content = ? and pid = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setInt(2, pid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
