package team.coc.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 群组
 */
@Entity
@Table(name = "t_group",
        uniqueConstraints = {@UniqueConstraint(columnNames="group_account")})
public class Group implements Serializable {

    /**
     * 群组ID
     * 主键生成策略为自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private int groupId;

    /**
     * 群组账号
     * 唯一
     */
    @Column(name = "group_account", length = 10)
    private String groupAccount;

    /**
     * 群组名称
     */
    @Column(name = "group_name", length = 20)
    private String groupName;

    public Group() { }

    public Group(String groupAccount, String groupName) {
        this.groupAccount = groupAccount;
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupAccount() {
        return groupAccount;
    }

    public void setGroupAccount(String groupAccount) {
        this.groupAccount = groupAccount;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
