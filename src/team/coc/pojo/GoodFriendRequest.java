package team.coc.pojo;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 好友申请
 */
@Entity
@Table(name = "t_good_friend_request")
public class GoodFriendRequest implements Serializable {

    /**
     * 好友申请id
     * 主键生成策略为自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 申请理由
     */
    @Column(name = "request_reason")
    private String requestReason;

    /**
     * 申请时间
     */
    @NotNull
    @Column(name = "request_time")
    @Temporal(TemporalType.TIMESTAMP) // 时间戳 - 'yyyy-MM-dd hh:MM:ss'
    private Date requestTime;

    /**
     * 好友申请所属的用户信息
     * 好友申请与用户形成单向多对一关系
     */
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="u1_id")
    private User user1;

    /**
     * 好友申请所属的用户信息
     * 好友申请与用户形成单向多对一关系
     */
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="u2_id")
    private User user2;

    /**
     * 处理结果 0未处理 1 申请成功 2 申请失败
     */
    private int result;

    public GoodFriendRequest() { }

    public GoodFriendRequest(String requestReason, Date requestTime, User user1, User user2, int result) {
        this.requestReason = requestReason;
        this.requestTime = requestTime;
        this.user1 = user1;
        this.user2 = user2;
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
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
}
