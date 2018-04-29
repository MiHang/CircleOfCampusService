package team.coc.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity // 标明实体类
@Table(name = "t_demo") // 标明表名， 默认和类名一致
public class Demo implements Serializable {

    @Id
    // 主键生成策略为自动，如果数据库支持自增长则为自增长
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "[Demo: id=" + id + ", name=" + name + "]";
    }
}
