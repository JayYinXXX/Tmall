package tmall.dao;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductImageDAO {
    // 两个静态属性 图片类型
    public static final String type_single = "type_single";
    public static final String type_detail = "type_detail";

    // C
    public void add(ProductImage bean) {
        String sql = "insert into productimage values(null, ?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bean.getProduct().getId());
            ps.setString(2, bean.getType());
            ps.execute();
            ResultSet rs =ps.getGeneratedKeys();
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
            String sql = "delete from productimage where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // U
    public void update(ProductImage bean) {
    }
    // Retrieve
    // 获取记录总数
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select count(*) from productimage";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    // 根据id获取记录
    public ProductImage get(int id) {
        ProductImage bean = new ProductImage();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select * from productimage where id = " + id;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                int pid = rs.getInt("pid");
                String type = rs.getString("type");
                bean.setProduct(new ProductDAO().get(pid));
                bean.setType(type);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    // 根据所属产品 图片类型 分页查询
    public List<ProductImage> list(Product p, String type, int start, int count) {
        List<ProductImage> beans = new ArrayList<>();
        String sql = "select * from productimage order by id desc where pid = ? and type = ? limit ?, ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setString(2, type);
            ps.setInt(3, start);
            ps.setInt(4, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductImage bean = new ProductImage();
                int id = rs.getInt(1);

                bean.setId(id);
                bean.setType(type);
                bean.setProduct(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    // 根据所属产品 图片类型 查询所有
    public List<ProductImage> list(Product p, String type) {
        return list(p, type, 0, Short.MAX_VALUE);
    }
}
