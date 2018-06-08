package team.coc.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import rasencrypt.encrypt.RSAEncrypt;
import team.coc.dao.CampusDao;
import team.coc.dao.UserDao;
import team.coc.data.Cookie;
import team.coc.pojo.User;
import team.coc.util.MailUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户找回密码控制器
 */
@Controller
public class ForgotPwdController {

    /**
     * 用于保存验证码有效期的相关数据
     */
    private static Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();

    /**
     * 用户重置密码<br>
     * 请求地址URL: http://ip:8080/coc/resetPassword.do<br>
     * @param strJson - json数据<br>
     * 请求参数: email : String - 邮箱<br>
     * 请求参数: pwd : String - 密码<br>
     * 请求参数: verificationCode : String - 用户输入的验证码<br>
     * @return 返回的json数据示例 {result:'success/error/no_code/code_error/timeout'}
     * result : String - success 重置密码成功<br>
     * result : String - error 重置密码失败<br>
     * result : String - no_code 未获取重置密码验证码<br>
     * result : String - code_error 验证码错误, 请重新获取验证码<br>
     * result : String - timeout 验证码超时（10分钟）<br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/resetPassword"}, method = {RequestMethod.POST})
    public String resetPassword(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 resetPassword #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        String email = jsonParam.getString("email");
        String pwd = jsonParam.getString("pwd");
        String verificationCode = jsonParam.getString("verificationCode");

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        // 验证验证码可用并正确
        Cookie cookie = cookieMap.get(email);
        if (cookie != null) { // 有该用户的动态验证数据

            // 验证时已超出验证码有效期（10分钟）
            if (System.currentTimeMillis() - cookie.getTimeStamp() > 10 * 60000) {
                json.put("result", "timeout");
            } else if (cookie.getName().equals(email) &&
                    cookie.getValue().equals(verificationCode)) { // 验证码在有效期内并且邮箱和验证码正确

                // 实例化RSA加密对象
                RSAEncrypt rsaEncrypt = new RSAEncrypt();

                // 实例化用户操作对象
                UserDao userDao = new UserDao();

                // 通过email获取相关用户
                User user = userDao.getUserByAccount(email);

                if (user != null) {
                    // 重置用户密码
                    user.setPwd(rsaEncrypt.encrypt(pwd));

                    // 更新用户数据
                    if (userDao.update(user)) {
                        json.put("result", "success");
                    } else {
                        json.put("result", "error");
                    }
                } else {
                    json.put("result", "error");
                }

            } else { // 验证失败
                json.put("result", "code_error");
            }

            // 移除该cookie
            cookieMap.remove(email);

        } else { // 未获取注册验证码
            json.put("result", "no_code");
        }
        System.out.println("############# resetPassword return:"+ json.toString() + " #############");
        return json.toString();
    }


    /**
     * 获取忘记密码的邮箱验证码 <br>
     * 请求地址URL: http://ip:8080/coc/getForgotPwdCode.do<br>
     * 请求方式: POST <br>
     * @param strJson - json数据<br>
     * 请求参数：email : String -邮箱<br>
     * @return 返回的json数据示例 {result:'success/invalid/error'}
     * result : String - success 发送成功 <br>
     * result : String - invalid 收件人邮箱地址无效 <br>
     * result : String - error 发送失败 <br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getForgotPwdCode"}, method = {RequestMethod.POST})
    public String getForgotPwdCode(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 getForgotPwdCode #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        String email = jsonParam.getString("email");

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        // 随机生成6位验证码
        String verificationCode = "";
        for (int i = 0; i < 6; i++) {
            verificationCode += (int)(Math.random() * 9);
        }

        // 邮件正文（可以使用html标签）
        String content = "<p>尊敬的用户 <a href='#'>" + email +"</a> 您好!</p>" +
                "<p>您已经请求了重置密码，请将重置密码的验证码填入忘记密码页面。<p>" +
                "<p>验证码: <a href='#'>" + verificationCode +"</a>，此验证码在10分钟内有效。<p>" +
                "<p>如果您没有请求重置密码，请忽略这封邮件。 </p>" +
                "<p>在您修改密码之前，您的密码将会保持不变。 </p>";

        // 发送验证码并返回发送结果
        String str = MailUtil.sendEmail(email,"校园圈重置密码验证", content);

        // 返回数据的JSON对象
        json = new JSONObject(str);
        if ("success".equals(json.getString("result"))) { // 邮件发送成功

            // 将邮箱和验证码保存到cookieMap中
            Cookie cookie = new Cookie(email, verificationCode, System.currentTimeMillis());
            cookieMap.put(email, cookie);
        }
        System.out.println("############# getForgotPwdCode return:"+ json.toString() + " #############");
        return json.toString();
    }


    /**
     * 是否存在此用户(该用户是否已注册) <br>
     * 请求地址URL: http://ip:8080/coc/isExistUser.do<br>
     * 请求方式: POST <br>
     * @param strJson - json数据<br>
     * 请求参数：account : String - 账号(邮箱/用户名)<br>
     * @return 返回的json数据示例 {result:'registered/unregistered'} <br>
     * result : String - registered  此用户已注册<br>
     * result : String - unregistered 此用户未注册<br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/isExistUser"}, method = {RequestMethod.POST})
    public String isExistUser(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 isExistUser #############");

        // 用户请求时上传的参数
        JSONObject jsonParam = new JSONObject(strJson);
        String account = jsonParam.getString("account"); // 账号

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        // 实例化Campus数据表操作对象
        CampusDao campusDao = new CampusDao();

        // 实例化User数据表操作对象
        UserDao userDao = new UserDao();

        if (campusDao.hasCampus(account) || userDao.hasUser(account)) { // 该账号已注册
            json.put("result", "registered");
        } else { // 该用户未注册
            json.put("result", "unregistered");
        }

        System.out.println("############# isExistUser return:"+ json.toString() + " #############");
        return json.toString();
    }
}
