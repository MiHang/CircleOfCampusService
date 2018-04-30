package team.coc.util;

import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件工具类<br/>
 * 主要用于邮件发送，如验证码的发送
 */
public class MailUtil {

    /**
     * 发件人邮箱
     */
    private static final String SENDER_ACCOUNT = "abslj@foxmail.com";
    /**
     * 发件人邮箱授权码
     */
    private static final String SENDER_LICENSE = "dkbvubpwtaygdiaj";
    /**
     * 发件人邮箱的 SMTP 服务器地址
     */
    private static final String SMTP_HOST = "smtp.qq.com";
    /**
     * 发件人邮箱的 SMTP 服务器端口
     */
    private static final String SMTP_PORT = "465";

    /**
     * 向指定用户发送验证码
     * @param sendee - 收件人邮箱地址
     * @param verificationCode - 验证码
     * @return 返回一个json对象, 包括发送结果,验证码和发送时间,
     * 返回数据示例 <br/>
     * &emsp;发送成功 - {result:'success'} <br/>
     * &emsp;发送失败 - {result:"error", code:501}; 其中501为无效的地址
     */
    public static String sendVerificationCode(String sendee, String verificationCode) {

        // 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", SMTP_HOST); // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true"); // 需要请求认证

        // 开启 SSL 安全连接
        // (某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证, 为了提高安全性, SSL连接也可以自己开启)
        props.setProperty("mail.smtp.port", SMTP_PORT);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", SMTP_PORT);

        // 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        session.setDebug(true); // 设置为debug模式, 可以查看详细的发送日志

        // 用于返回数据的json对象
        JSONObject json = new JSONObject();
        try {
            // 创建一封邮件
            MimeMessage message = new MimeMessage(session);

            // 发件人
            message.setFrom(new InternetAddress(SENDER_ACCOUNT, "校园圈开发组", "UTF-8"));

            // 收件人
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(sendee, "UTF-8"));

            // 邮件主题（标题如有广告嫌疑会被邮件服务器认为是滥发广告以至发送失败）
            message.setSubject("注册校园圈邮箱验证", "UTF-8");

            // 邮件正文（可以使用html标签）
            String msg = "<p>尊敬的用户 <a>" + sendee +"</a> 您好!</p>" +
                    "<p>感谢您注册校园圈，请将验证码填写到注册页面。<p>" +
                    "<p>验证码: <a>" + verificationCode +"</a>，此验证码在5分钟内有效。<p>" +
                    "<p>这是一张用于测试的图片，请查收。 " +
                    "<img src='http://www.jayeli.top:8080/test/test.jpg' style='width:auto;height:100px'></p>";
            message.setContent(msg, "text/html;charset=UTF-8");

            // 设置发件时间
            message.setSentDate(new Date());

            // 保存设置
            message.saveChanges();

            // 根据 Session 获取邮件传输对象
            Transport transport = session.getTransport();
            transport.connect(SENDER_ACCOUNT, SENDER_LICENSE);

            // 发送邮件
            // message.getAllRecipients() 获取到的是在创建邮件对象时添加的收件人
            transport.sendMessage(message, message.getAllRecipients());

            // 关闭连接
            transport.close();

            json.put("result", "success");

        } catch (Exception e) {
            try {
                json.put("result", "error");
                if ("Invalid Addresses".equals(e.getMessage())) {
                    json.put("code", 501); // 无效的地址
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return json.toString();
    }

}
