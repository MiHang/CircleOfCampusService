package team.coc.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 群组成员
 */
@Entity
@Table(name = "t_group_member")
public class GroupMember implements Serializable {

    /**
     * 群组成员表ID
     * 主键生成策略为自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 群组成员所属的群组信息
     * 群组成员与群组形成单向多对一关系
     * 既，该群组成员(特定的群组成员)属于该群组，该群组可有多个群组成员
     */
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="group_id")
    private Group group;

    /**
     * 群组成员所属的用户信息
     * 群组成员与用户形成单向多对一关系
     * 既，该群组成员(特定的群组成员)是该用户，该用户可以是多个群组的成员
     */
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="u_id")
    private User user;

    public GroupMember() {}

    public GroupMember(Group group, User user) {
        this.group = group;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
