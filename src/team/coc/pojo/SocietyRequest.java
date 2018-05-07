package team.coc.pojo;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 社团权限申请
 */
@Entity
@Table(name = "t_society_request")
public class SocietyRequest implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * 申请原因
     */
    @NotNull
    @Column(name = "request_reason", length = 100)
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

    /**
     * 处理原因
     */
    @NotNull
    @Column(name = "deal_reason", length = 50)
    private String dealReason;

    /**
     * 处理时间
     */
    @NotNull
    @Column(name = "deal_time")
    private String dealTime;

    /**
     * 社团信息
     * 社团与社团权限申请表形成单向一对一
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="s_id")
    private Society society;

    /**
     * 用户信息
     * 用户与社团权限申请表形成单向一对一
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="u_id")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDealReason() {
        return dealReason;
    }

    public void setDealReason(String dealReason) {
        this.dealReason = dealReason;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
