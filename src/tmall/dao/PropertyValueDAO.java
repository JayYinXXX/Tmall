package tmall.dao;

import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyValueDAO {
    // CRUD
    // C
    public void add(PropertyValue bean) {
        String sql = "insert into PropertyValues values(null, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bean.getProduct().getId()); // pid 产品id
            ps.setInt(2, bean.getProperty().getId()); // ptid 属性id
            ps.setString(3, bean.getValue()); // 属性值
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // D
    public void delete(int id) {
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()
        ) {
            String sql = "delete from PropertyValue where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // U
    public void update(PropertyValue bean) {
        String sql = "update PropertyValue set pid=?, ptid=?, value=? where id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bean.getProduct().getId());
            ps.setInt(2, bean.getProperty().getId());
            ps.setString(3, bean.getValue());
            ps.setInt(4, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // R
    // 获取记录总数
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select count(*) from PropertyValue";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    // 根据id返回该条记录
    public PropertyValue get(int id) {
        PropertyValue bean = new PropertyValue();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select from PropertyValue where id = " + id;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                int pid = rs.getInt("pid");
                Product p = new ProductDAO().get(pid);
                int ptid = rs.getInt("ptid");
                Property pt = new PropertyDAO().get(ptid);
                String value = rs.getString("value");

                bean.setId(id);
                bean.setProduct(p);
                bean.setProperty(pt);
                bean.setValue(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    // 根据ptid和pid返回相应的记录
    public PropertyValue get(int ptid, int pid) {
        PropertyValue bean = new PropertyValue();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select from PropertyValue where pid = " + pid +"and ptid =" + ptid;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Product p = new ProductDAO().get(pid);
                Property pt = new PropertyDAO().get(ptid);
                String value = rs.getString("value");
                int id = rs.getInt(1);

                bean.setId(id);
                bean.setProduct(p);
                bean.setProperty(pt);
                bean.setValue(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    // 根据pid 返回相应的多个记录
    public List<PropertyValue> list(int pid) {
        List<PropertyValue> beans = new ArrayList<>();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            String sql = "select * from Property order by ptid desc where pid = " + pid;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt(1);
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product p = new ProductDAO().get(pid);
                Property pt = new PropertyDAO().get(ptid);

                bean.setId(id);
                bean.setProduct(p);
                bean.setProperty(pt);
                bean.setValue(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    // 分页查询记录 返回多个记录
    public List<PropertyValue> list(int start, int count) {
        List<PropertyValue> beans = new ArrayList<>();
        String sql = "select * from PropertyValue limit ?,? order by id desc";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt(1);
                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product p = new ProductDAO().get(pid);
                Property pt = new PropertyDAO().get(ptid);

                bean.setId(id);
                bean.setProduct(p);
                bean.setProperty(pt);
                bean.setValue(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    // 返回所有记录
    public List<PropertyValue> list() {
        return list(0, Short.MAX_VALUE);
    }

    // 非CRUD
    // 初始化某产品对应的属性
    public void init(Product p) {
        // 根据产品所在分类的id 列出该分类包含的所有属性
        List<Property> pts = new PropertyDAO().list(p.getCategory().getId());
        for (Property pt : pts) {
            // 给定产品pid 和给定属性ptid 对应一个属性值pv
            // 若不存在则新建一个 value为null
            PropertyValue pv = get(pt.getId(), p.getId());  // ptid pid
            if (null == pv) {
                pv = new PropertyValue();
                pv.setProduct(p);
                pv.getProperty();
                this.add(pv);
            }
        }
    }

}
