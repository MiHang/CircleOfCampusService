package team.coc.test;

import org.json.JSONException;
import org.json.JSONObject;
import team.coc.util.MailUtil;

import java.util.Date;

public class MailTest {

    public static void main(String[]args) {
//        String str = MailUtil.sendVerificationCode("jayevip@163.com", 6666);
//        try {
//            JSONObject json = new JSONObject(str);
//            if ("success".equals(json.getString("result"))) {
//                System.out.println("验证码发送成功 - " + json.toString());
//            } else {
//                System.out.println("验证码发送失败 - " + json.toString());
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        // 随机生成6位验证码
        String verificationCode = "";
        for (int i = 0; i < 6; i++) {
            verificationCode += (int)(Math.random() * 9);
        }
        System.out.println("verificationCode - " + verificationCode);

        new Thread(() -> {

            try {
                Long startTime = System.currentTimeMillis();
                System.out.println("startTime - " + startTime);
                Thread.sleep(2000);
                long endTime = System.currentTimeMillis();
                System.out.println("endTime - startTime" + (endTime - startTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

    }

}
