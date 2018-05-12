package team.coc.pojo;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 用户
 */
@Entity
@Table(name = "t_user",
        uniqueConstraints = {@UniqueConstraint(columnNames={"user_name", "email"})})
public class User implements Serializable {

    /**
     * 性别 - 男
     */
    @Transient // 此属性非表字段
    public static final String MALE = "male"; // 性别 - 男
    /**
     * 性别 - 女
     */
    @Transient // 此属性非表字段
    public static final String FEMALE = "female"; // 性别 - 女
    /**
     * 用户ID
     * 主键生成策略为自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_id")
    private int userId;
    /**
     * 用户名
     */
    @Column(name = "user_name", length = 10)
    private String userName;
    /**
     * 用户邮箱
     */
    @Column(name = "email", length = 50)
    private String email;
    /**
     * 用户密码
     */
    @Column(name = "pwd", length = 255)
    private String pwd;
    /**
     * 用户性别
     */
    @Column(name = "gender", length = 6)
    private String gender;
    /**
     * 出生日期
     */
    @Column(name = "birthday", length = 10)
    private String birthday;
    /**
     * 籍贯
     */
    @Column(name = "native_place")
    private String nativePlace;
    /**
     * 用户所属的院系信息
     * 用户与院系形成单向多对一关系
     */
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="f_id")
    private Faculty faculty;

    public User() { }

    public User(String userName, String email, String pwd, String gender, String birthday, String nativePlace, Faculty faculty) {
        this.userName = userName;
        this.email = email;
        this.pwd = pwd;
        this.gender = gender;
        this.birthday = birthday;
        this.nativePlace = nativePlace;
        this.faculty = faculty;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
