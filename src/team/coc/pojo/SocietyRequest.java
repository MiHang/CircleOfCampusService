package team.coc.pojo;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 社团权限申请
 */
@Entity
@Table(name = "t_society_request")
public class SocietyRequest implements Serializable {

    /**
     * 社团权限申请ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Temporal(TemporalType.TIMESTAMP) // 时间戳 - 'yyyy-MM-dd hh:MM:ss'
    private Date requestTime;

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
    @Temporal(TemporalType.TIMESTAMP) // 时间戳 - 'yyyy-MM-dd hh:MM:ss'
    private Date dealTime;

    /**
     * 社团权限申请
     * 社团与社团权限申请表形成单向一对一
     */
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="s_id")
    private Society society;

    /**
     * 用户信息
     * 用户与社团权限申请表形成单向一对一
     */
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="u_id")
    private User user;

    public SocietyRequest() { }

    public SocietyRequest(String requestReason, Date requestTime, int result,
                          String dealReason, Date dealTime, Society society, User user) {
        this.requestReason = requestReason;
        this.requestTime = requestTime;
        this.result = result;
        this.dealReason = dealReason;
        this.dealTime = dealTime;
        this.society = society;
        this.user = user;
    }

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

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
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
