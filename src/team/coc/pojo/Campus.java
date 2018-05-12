package team.coc.pojo;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 学校
 */
@Entity
@Table(name = "t_campus",
        uniqueConstraints = {@UniqueConstraint(columnNames={"campus_account", "campus_name"})})
public class Campus implements Serializable {

    /**
     * 学校ID<br>
     * 主键生成策略为自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private int campusId;
    /**
     * 学校账号， 唯一约束
     */
    @NotNull
    @Column(name = "campus_account", length = 10)
    private String campusAccount;
    /**
     * 学校名称， 唯一约束
     */
    @NotNull
    @Column(name = "campus_name", length = 20)
    private String campusName;
    /**
     * 学校简介
     */
    @Column(name = "introduction", length = 20)
    private String introduction;
    /**
     * 学校管理员密码
     */
    @Column(name = "pwd", length = 255)
    private String password;

    public Campus(){}

    public Campus(String campusAccount, String campusName, String introduction, String password) {
        this.campusAccount = campusAccount;
        this.campusName = campusName;
        this.introduction = introduction;
        this.password = password;
    }

    public int getCampusId() {
        return campusId;
    }

    public void setCampusId(int campusId) {
        this.campusId = campusId;
    }

    public String getCampusAccount() {
        return campusAccount;
    }

    public void setCampusAccount(String campusAccount) {
        this.campusAccount = campusAccount;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
