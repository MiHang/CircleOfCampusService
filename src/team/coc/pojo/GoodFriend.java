package team.coc.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 好友
 */
@Entity
@Table(name = "t_good_friend")
public class GoodFriend implements Serializable {

    /**
     * 好友所属的用户信息
     * 好友与用户形成单向多对一关系
     * u1_id 与 u2_id 形成联合主键
     */
    @Id
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="u1_id")
    private User user1;
    /**
     * 好友所属的用户信息
     * 好友与用户形成单向多对一关系
     * u1_id 与 u2_id 形成联合主键
     */
    @Id
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="u2_id")
    private User user2;
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

    public GoodFriend() { }

    public GoodFriend(User user1, User user2, String u1Notice, String u2Notice) {
        this.user1 = user1;
        this.user2 = user2;
        this.u1Notice = u1Notice;
        this.u2Notice = u2Notice;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
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
