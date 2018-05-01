package team.coc.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import team.coc.data.Cookie;
import team.coc.util.MailUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录控制器
 */
@Controller
public class LoginController {

    /**
     * 用于保存动态登录相关数据
     */
    private static Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();

    /**
     * 用户账号密码登录<br>
     * 访问地址URL: http://ip:8080/coc/login.do<br>
     * @param strJson - json数据<br>
     *      参数：account : String - 账号<br>
     *      参数：pwd : String - 密码<br>
     *      示例：{"account":"jayevip@163.com", "pwd":"123456"}<br>
     * @return 返回的json数据示例 {result:'success/error/unknown', type:"admin/user"}
     *      result : String - success 登录验证成功<br>
     *      result : String - error 登录验证失败<br>
     *      result : String - unknown 该用户未注册<br>
     *      type : String - admin 管理员用户<br>
     *      type : String - user 普通用户
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/login"},method = {RequestMethod.POST})
    public String login(@RequestBody String strJson) throws JSONException {

        JSONObject jsonParam = new JSONObject(strJson);

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        // 判断用户类型
        json.put("type", "user");

        // 验证结果
        json.put("result", "error");

        return json.toString();
    }

    /**
     * 获取邮箱动态验证码<br>
     * 访问地址URL: http://ip:8080/coc/getLoginCode.do<br>
     * @param strJson - json数据<br>
     *      参数：email : String - 用户邮箱<br>
     *      示例：{"email":"jayevip@163.com"}<br>
     * @return 返回的json数据示例 {result:'success/error/unknown', code:'0000'}
     *      result : String - success表示验证码获取成功<br>
     *      result : String - error 表示验证码获取失败<br>
     *      result : String - unknown表示该邮箱未注册<br>
     *      code : String - 验证码<br>
     *      注：当 result - error 时 code 为错误代码。如{result:'error', code:'501'}; <br>
     *      501：无效的地址
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getLoginCode"}, method = {RequestMethod.POST})
    public String getLoginCode(@RequestBody String strJson) throws JSONException {

        JSONObject jsonParam = new JSONObject(strJson);
        String email = jsonParam.getString("email");

        // 随机生成6位验证码
        String verificationCode = "";
        for (int i = 0; i < 6; i++) {
            verificationCode += (int)(Math.random() * 9);
        }

        // 发送验证码并返回发送结果
        String str = MailUtil.sendVerificationCode(email, verificationCode);

        // 返回数据的JSON对象
        JSONObject json = new JSONObject(str);
        if ("success".equals(json.getString("result"))) {

            json.put("code", verificationCode);

            // 将邮箱和验证码保存到cookieMap中
            Cookie cookie = new Cookie(email, verificationCode, System.currentTimeMillis());
            cookieMap.put(email, cookie);
        }

        return json.toString();
    }

    /**
     * 用户动态登录验证<br>
     * 访问地址URL: http://ip:8080/coc/dynamicLogin.do<br>
     * @param strJson - json数据<br>
     *      参数：email : String - 用户邮箱<br>
     *      参数：verificationCode : String - 用户输入的验证码<br>
     *      示例：{"email":"jayevip@163.com","verificationCode":"431567"}<br>
     * @return 返回的json数据示例 {result:'success/error/timeout', type:"admin/user"}
     *      result : String - success 登录验证成功<br>
     *      result : String - error 登录验证失败<br>
     *      result : String - timeout 验证码超时<br>
     *      type : String - admin 管理员用户<br>
     *      type : String - user 普通用户
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/dynamicLogin"}, method = {RequestMethod.POST})
    public String dynamicLogin(@RequestBody String strJson) throws JSONException {

        JSONObject jsonParam = new JSONObject(strJson);
        String email = jsonParam.getString("email");
        String verificationCode = jsonParam.getString("verificationCode");

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();
        Cookie cookie = cookieMap.get(email);
        if (cookie != null) { // 有该用户的动态验证数据

            // 验证时已超出验证码有效期（5分钟）
            if (System.currentTimeMillis() - cookie.getTimeStamp() > 5 * 60000) {
                json.put("result", "timeout");
            } else if (cookie.getName().equals(email) &&
                    cookie.getValue().equals(verificationCode)) { // 验证码在有效期内并且邮箱和验证码正确

                // 判断用户类型
                json.put("type", "user");

                json.put("result", "success");

            } else { // 验证失败
                json.put("result", "error");
            }

            // 移除该cookie
            cookieMap.remove(email);

        } else { // 验证失败
            json.put("result", "error");
        }

        return json.toString();
    }

}
