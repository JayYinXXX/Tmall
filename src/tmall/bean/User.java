package tmall.bean;

public class User {
    private String name;
    private String password;
    private int id;

    // 设置/获取 ID/password/name
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    // 获得本用户的匿名名称 用*隐藏真实ID
    public String getAnonymousName(){
        if (null == name)
            return null;
        if(name.length()<=1)
            return "*";
        if(name.length() == 2)
            return name.substring(0,1) + "*";
        char[] cs = name.toCharArray();
        for(int i=0; i<cs.length-1; i++){
            cs[i] = '*';
        }
        return new String(cs);
    }
}
