package tmall.dao;

import tmall.bean.Category;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // 返回数据库中一个表里的记录条数
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s =  c.createStatement()) {
            String sql = "select count(*) from Category";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // 增 （向数据库增加一条记录，并获得相应的id返回给bean）
    public void add(Category bean) {
        String sql = "insert into category values(null,?)";  // id自动生成不用手动插入
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getName());  // 获取需要新增的category项的名称
            ps.execute();  // 执行预处理过的PS对象语句
            ResultSet rs = ps.getGeneratedKeys();  // 获取此ps对象创建的的所有记录
            if (rs.next()) {
                int id = rs.getInt(1);  // 获取刚刚新建记录的id
                bean.setId(id);  //  在实体类里设置它
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 改
    public void update(Category bean) {
        String sql = "update category set name = ? where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getName());
            ps.setInt(2, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删
    public void delete(int id) {
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "delete from Category where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 根据ID获取该条记录信息
    public Category get(int id) {
        Category bean = null;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select * from Category where id = " + id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                bean = new Category();
                String name = rs.getString(2);  //根据id返回结果集，取出name
                bean.setId(id);
                bean.setName(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    // 分页查询记录（给定开始索引和数量）
    public List<Category> list(int start, int count) {
        List<Category> beans = new ArrayList<Category>();
        String sql = "select * from Category by id desc limit ?,?";  // 选择指定范围内的行按id降序排列
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category bean = new Category();  // 逐行读取结果集 取出id/name 构建实体类 加入List
                int id = rs.getInt(1);
                String name = rs.getString(2);
                bean.setId(id);
                bean.setName(name);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    // 查询所有
    public List<Category> list() {
        return list(0, Short.MAX_VALUE);  // 调用上一个方法
    }


}
