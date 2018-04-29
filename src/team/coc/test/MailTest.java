package team.coc.test;

import org.json.JSONException;
import org.json.JSONObject;
import team.coc.util.MailUtil;

public class MailTest {

    public static void main(String[]args) {
        String str = MailUtil.sendVerificationCode("jayevip@163.com", 6666);
        try {
            JSONObject json = new JSONObject(str);
            if ("success".equals(json.getString("result"))) {
                System.out.println("验证码发送成功 - " + json.toString());
            } else {
                System.out.println("验证码发送失败 - " + json.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
