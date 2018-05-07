package team.coc.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 好友
 */
@Entity
@Table(name = "t_good_friend")
public class GoodFriend implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * u1_id
     */
    @Column(name = "u1_id")
    private int u1Id;

    /**
     * u2_id
     */
    @Column(name = "u2_id")
    private int u2Id;

    /**
     * 好友1备注
     */
    @Column(name = "u1_notice", length = 20)
    private String u1Notice;

    /**
     * 好友2备注
     */
    @Column(name = "u2_notice", length = 20)
    private String u2Notice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getU1Id() {
        return u1Id;
    }

    public void setU1Id(int u1Id) {
        this.u1Id = u1Id;
    }

    public int getU2Id() {
        return u2Id;
    }

    public void setU2Id(int u2Id) {
        this.u2Id = u2Id;
    }

    public String getU1Notice() {
        return u1Notice;
    }

    public void setU1Notice(String u1Notice) {
        this.u1Notice = u1Notice;
    }

    public String getU2Notice() {
        return u2Notice;
    }

    public void setU2Notice(String u2Notice) {
        this.u2Notice = u2Notice;
    }
}
