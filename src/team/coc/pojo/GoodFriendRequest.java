package team.coc.pojo;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 好友申请
 */
@Entity
@Table(name = "t_good_friend_request")
public class GoodFriendRequest implements Serializable {

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
     * 申请理由
     */
    @Column(name = "request_reason")
    private String requestReason;

    /**
     * 申请时间
     */
    @NotNull
    @Column(name = "request_time")
    private String requestTime;

    /**
     * 处理结果 0未处理 1 申请成功 2 申请失败
     */
    private int result;

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

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
