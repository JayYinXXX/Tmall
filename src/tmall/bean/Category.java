package tmall.bean;

import java.util.List;

// Category除了基本属性id和name的getter和setter
// 提供了一对多关系 products的 getter与setter
public class Category {
    private int id;
    private String name;
    List<Product> products; // 产品子类
    List<List<Product>> productsByRow;  // 大类（一列显示的）

    // 获取/设置id/name/products/productsByRow
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public List<Product> getProducts(){
        return products;
    }
    public void setProducts(List<Product> products){
        this.products = products;
    }

    public List<List<Product>> getProductsByRow(){
        return productsByRow;
    }
    public void setProductsByRow(List<List<Product>> productsByRow){
        this.productsByRow = productsByRow;
    }

    @Override
    public String toString(){
        return "Category [name = " + name + "]";
    }
}
