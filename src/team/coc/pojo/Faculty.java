package team.coc.pojo;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 院系
 */
@Entity
@Table(name = "t_faculty")
public class Faculty implements Serializable {

    /**
     * 院系ID
     * 主键生成策略为自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "f_id")
    private int facultyId;
    /**
     * 院系名
     */
    @NotNull
    @Column(name = "faculty_name", length = 20)
    private String facultyName;
    /**
     * 院系所属的学校信息
     * 院系与学校形成双向多对一关系
     */
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="c_id")
    private Campus campus;
    /**
     * 用户集合
     * 院系与用户形成双向一对多关系
     */
    @OneToMany(cascade= {CascadeType.ALL}, fetch=FetchType.LAZY)
    @JoinColumn(name="f_id")
    private Set<User> userSet;

    public Faculty() {}

    public Faculty(String facultyName, Campus campus) {
        this.facultyName = facultyName;
        this.campus = campus;
    }

    public Faculty(String facultyName) {
        this.facultyName = facultyName;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }
}
