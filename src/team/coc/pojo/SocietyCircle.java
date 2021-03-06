package team.coc.pojo;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 社团圈
 */
@Entity
@Table(name = "t_society_circle")
public class SocietyCircle implements Serializable {

    /**
     * 社团圈ID
     * 主键生成策略为自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 标题
     */
    @NotNull
    @Column(length = 100)
    private String title;

    /**
     * 文本内容
     */
    @Column(name = "content", length = 500)
    private String content;

    /**
     * 图片地址
     */
    @Column(name = "images_url", length = 1000)
    private String imagesUrl;

    /**
     * 发布时间
     */
    @Column(name = "publish_time")
    @Temporal(TemporalType.TIMESTAMP) // 时间戳 - 'yyyy-MM-dd hh:MM:ss'
    private Date publishTime;

    /**
     * 活动地点
     */
    @Column(length = 100)
    private String venue;

    /**
     * 活动时间
     */
    @Column(name = "activity_time", length = 50)
    private String activityTime;

    /**
     * 审核状态，1 - 已审核，0 - 未审核
     */
    @NotNull
    private int auditing;

    /**
     * 发布社团圈的用户信息
     * 社团圈与用户形成单向多对一关系
     */
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="u_id")
    private User user;

    public SocietyCircle() { }

    public SocietyCircle(String title, String content,
                         String imagesUrl, Date publishTime,
                         String venue, String activityTime,
                         int auditing, User user) {
        this.title = title;
        this.content = content;
        this.imagesUrl = imagesUrl;
        this.publishTime = publishTime;
        this.venue = venue;
        this.activityTime = activityTime;
        this.auditing = auditing;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public int getAuditing() {
        return auditing;
    }

    public void setAuditing(int auditing) {
        this.auditing = auditing;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
