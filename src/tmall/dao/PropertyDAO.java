package tmall.dao;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAO {
    // CRUD
    // C
    public void add(Property bean) {
        String sql = "insert into Property values(null, ?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bean.getCategory().getId());
            ps.setString(2, bean.getName());
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
            String sql = "delete from property where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // U
    public void update(Property bean) {
        String sql = "update property set cid = ?, name = ? where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bean.getCategory().getId());
            ps.setString(2, bean.getName());
            ps.setInt(3, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // R
    // 根据cid获取对应的记录数（cid：该属性所属分类的id）
    public int getTotal(int cid) {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select count(*) from property where cid =" + cid;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    // 根据id获取这条记录 返回一个实体类
    public Property get(int id) {
        Property bean = new Property();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select from property where id = " + id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                int cid = rs.getInt("cid");
                String name = rs.getString("name");
                // 根据这条属性所属分类对应的id 返回这个category类
                Category category = new CategoryDAO().get(cid);
                bean.setId(id);
                bean.setName(name);
                bean.setCategory(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    // 分页查询指定cid的记录（给定开始索引和数量）
    public List<Property> list(int cid, int start, int count) {
        List<Property> beans = new ArrayList<>();
        String sql = "select * from property where cid = ? order by id desc limit ? ,?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Property bean = new Property();
                int id = rs.getInt(1);  // id
                String name = rs.getString("name");  // name
                Category category = new CategoryDAO().get(cid); // 所属的property

                bean.setId(id);
                bean.setName(name);
                bean.setCategory(category);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    // 查询指定cid的所有记录
    public List<Property> list(int cid) {
        return list(cid, 0, Short.MAX_VALUE);
    }

}
