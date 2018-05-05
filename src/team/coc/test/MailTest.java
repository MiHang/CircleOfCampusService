package team.coc.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import team.coc.util.MailUtil;

import java.util.Date;

public class MailTest {

    public static void main(String[]args) throws JSONException {

        String str = MailUtil.sendVerificationCode("610926067@qq.com", "6666");
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
