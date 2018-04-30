package team.coc.data;

/**
 * 用于保存动态登录数据
 */
public class Cookie {

    private String name; // 键
    private String value; // 值
    private long timeStamp; // 时间戳

    public Cookie() {}

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Cookie(String name, String value, long timeStamp) {
        this.name = name;
        this.value = value;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
