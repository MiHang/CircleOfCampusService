package team.coc.pojo;

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
     * 文本内容
     */
    @Column(name = "content", length = 200)
    private String content;

    /**
     * 图片地址，多个图片之间用分号(';')隔开
     */
    @Column(name = "images_url", length = 200)
    private String imagesUrl;

    /**
     * 视频地址
     */
    @Column(name = "video_url")
    private String videoUrl;

    /**
     * 发布时间
     */
    @Column(name = "publish_time")
    @Temporal(TemporalType.TIMESTAMP) // 时间戳 - 'yyyy-MM-dd hh:MM:ss'
    private Date publishTime;

    /**
     * 社团圈所属的社团信息
     * 社团圈与社团形成单向多对一关系
     * 既，该社团圈属于该社团，该社团可以拥有多个社团圈
     */
    @ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name="s_id")
    private Society society;

    public SocietyCircle() { }

    public SocietyCircle(String content, String imagesUrl, String videoUrl, Date publishTime, Society society) {
        this.content = content;
        this.imagesUrl = imagesUrl;
        this.videoUrl = videoUrl;
        this.publishTime = publishTime;
        this.society = society;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}
