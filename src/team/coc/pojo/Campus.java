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
     * 主键生成策略为自动，如果数据库支持自增长则为自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Column(name = "pwd", length = 16)
    private String password;

    /**
     * 院系集合
     * 学校与院系形成双向一对多关系
     * 一所学校拥有多个院系，一个院系只属于一个学校（一方持有多方的集合）
     * 获取方式为懒加载 FetchType.LAZY
     */
    @OneToMany(cascade= {CascadeType.ALL}, fetch=FetchType.LAZY)
    @JoinColumn(name="c_id")
    private Set<Faculty> facultySet;

    /**
     * 社团集合
     * 学校与社团形成双向一对多关系
     */
    @OneToMany(cascade= {CascadeType.ALL}, fetch=FetchType.LAZY)
    @JoinColumn(name="c_id")
    private Set<Society> societySet;


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

    public Set<Faculty> getFacultySet() {
        return facultySet;
    }

    public void setFacultySet(Set<Faculty> facultySet) {
        this.facultySet = facultySet;
    }

    public Set<Society> getSocietySet() {
        return societySet;
    }

    public void setSocietySet(Set<Society> societySet) {
        this.societySet = societySet;
    }
}
