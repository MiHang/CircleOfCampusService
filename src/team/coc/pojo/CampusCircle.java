package team.coc.pojo;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 校园圈
 */
@Entity
@Table(name = "t_campus_circle")
public class CampusCircle implements Serializable {

    /**
     * 校园圈表ID
     * 主键生成策略为自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 标题
     */
    @NotNull
    @Column(length = 50)
    private String title;

    /**
     * 文本内容
     */
    @Column(name = "content", length = 500)
    private String content;

    /**
     * 图片地址，多个图片之间用分号(';')隔开
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
    @Column(length = 50)
    private String venue;

    /**
     * 活动时间
     */
    @Column(name = "activity_time", length = 50)
    private String activityTime;

    /**
     * 校园圈所属的校园信息
     * 校园圈与校园形成单向多对一关系
     * 既，该校园圈属于该校园，该校园可以拥有多个校园圈
     */
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="c_id")
    private Campus campus;

    public CampusCircle() { }

    public CampusCircle(String title, String content,
                        String imagesUrl, Date publishTime,
                        String venue, String activityTime,
                        Campus campus) {
        this.title = title;
        this.content = content;
        this.imagesUrl = imagesUrl;
        this.publishTime = publishTime;
        this.venue = venue;
        this.activityTime = activityTime;
        this.campus = campus;
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

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
}
