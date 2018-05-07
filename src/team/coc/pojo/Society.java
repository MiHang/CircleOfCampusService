package team.coc.pojo;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 社团
 */
@Entity
@Table(name = "t_society")
public class Society implements Serializable {

    /**
     * 社团ID<br>
     * 主键生成策略为自动，如果数据库支持自增长则为自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "s_id")
    private int societyId;

    /**
     * 社团名称
     */
    @NotNull
    @Column(name = "society_name", length = 20)
    private String societyName;

    /**
     * 社团所属的学校信息
     * 社团与学校形成双向多对一关系
     */
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="c_id")
    private Campus campus;

    public Society() {}

    public Society(String societyName) {
        this.societyName = societyName;
    }

    public int getSocietyId() {
        return societyId;
    }

    public void setSocietyId(int societyId) {
        this.societyId = societyId;
    }

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
}
