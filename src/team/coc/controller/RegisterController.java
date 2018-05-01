package team.coc.controller;

import com.sun.deploy.net.HttpResponse;
import org.json.JSONArray;
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
 * 用户注册控制器
 */
@Controller
public class RegisterController {

    /**
     * 用于保存注册验证码有效期的相关数据
     */
    private static Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();

    /**
     * 用户注册<br>
     * 访问地址URL: http://ip:8080/coc/register.do<br>
     * @param strJson - json数据<br>
     *      参数: username : String - 用户名<br>
     *      参数: gender : String - 性别 (男 - male, 女 - female)<br>
     *      参数: email : String - 邮箱<br>
     *      参数: verificationCode : String - 用户输入的验证码<br>
     *      参数: campusId : int - 学校ID<br>
     *      参数: facultyId : int - 院系ID<br>
     * @return 返回的json数据示例 {result:'success/error', msg:"error/timeout"}
     *      result : String - success 注册成功<br>
     *      result : String - error 注册失败<br>
     *      注册失败时有msg:"error/timeout"信息<br>
     *      msg : String - error 验证码错误<br>
     *      msg : String - timeout 验证码超时（30分钟）
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/register"}, method = {RequestMethod.POST})
    public String register(@RequestBody String strJson) throws JSONException {

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        String username = jsonParam.getString("username");
        String gender = jsonParam.getString("gender");
        String email = jsonParam.getString("email");
        String verificationCode = jsonParam.getString("verificationCode");
        int campusId = jsonParam.getInt("campusId");
        int facultyId = jsonParam.getInt("facultyId");


        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        // 验证验证码可用并正确
        Cookie cookie = cookieMap.get(email);
        if (cookie != null) { // 有该用户的动态验证数据

            // 验证时已超出验证码有效期（30分钟）
            if (System.currentTimeMillis() - cookie.getTimeStamp() > 30 * 60000) {
                json.put("msg", "timeout");
                json.put("result", "error");
            } else if (cookie.getName().equals(email) &&
                    cookie.getValue().equals(verificationCode)) { // 验证码在有效期内并且邮箱和验证码正确

                // 注册相关操作

                json.put("result", "success");

            } else { // 验证失败
                json.put("result", "error");
                json.put("msg", "error");
            }

            // 移除该cookie
            cookieMap.remove(email);

        } else { // 验证失败
            json.put("result", "error");
        }

        return json.toString();
    }

    /**
     * 判断用户名是否可用/唯一<br>
     * 访问地址URL: http://ip:8080/coc/usableUserName.do<br>
     * @param strJson - json数据<br>
     *      参数: username : String - 用户名<br>
     * @return json数据示例{result:'success/error'}<br>
     *      result : String - success 此用户名可用<br>
     *      result : String - error 此用户名已被占用<br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/usableUserName"}, method = {RequestMethod.POST})
    public String usableUserName(@RequestBody String strJson) throws JSONException {

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        String username = jsonParam.getString("username");

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();
        json.put("result", "error");

        return json.toString();
    }

    /**
     * 获取邮箱注册验证码<br>
     * 访问地址URL: http://ip:8080/coc/getRegisterCode.do<br>
     * @param strJson - json数据<br>
     *      参数：email : String - 用户注册邮箱<br>
     *      示例：{"email":"jayevip@163.com"}<br>
     * @return 返回的json数据示例 {result:'success/error/registered', code:'0000'}
     *      result : String - success表示验证码获取成功<br>
     *      result : String - error 表示验证码获取失败<br>
     *      result : String - registered表示该邮箱已被注册<br>
     *      code : String - 验证码<br>
     *      注：当 result - error 时 code 为错误代码。如{result:'error', code:'501'}; <br>
     *      501：无效的地址
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getRegisterCode"}, method = {RequestMethod.POST})
    public String getRegisterCode(@RequestBody String strJson) throws JSONException {

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        String email = jsonParam.getString("email");

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        // 验证邮箱是否已被注册
        boolean isRegistered = false;
        if (isRegistered) {  // 邮箱已被注册
            json.put("result", "registered");
            return json.toString();
        }

        // 随机生成6位验证码
        String verificationCode = "";
        for (int i = 0; i < 6; i++) {
            verificationCode += (int)(Math.random() * 9);
        }

        // 发送验证码并返回发送结果
        String str = MailUtil.sendVerificationCode(email, verificationCode);

        // 返回数据的JSON对象
        json = new JSONObject(str);
        if ("success".equals(json.getString("result"))) {

            json.put("code", verificationCode);

            // 将邮箱和验证码保存到cookieMap中
            Cookie cookie = new Cookie(email, verificationCode, System.currentTimeMillis());
            cookieMap.put(email, cookie);
        }

        return json.toString();
    }

    /**
     * 获取支持注册的所有学校的名称<br>
     * 访问地址URL: http://ip:8080/coc/getCampuses.do<br>
     * @return - 返回的json数组示例 [{"campusName":"成都职业技术学院","campusId":1}]
     *      campusId : int - 学校ID<br>
     *      campusName : String - 学校名称<br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getCampuses"}, method = {RequestMethod.POST})
    public String getCampuses() throws JSONException {

        // 相关查找操作

        // 返回数据的JSON数组
        JSONArray json = new JSONArray();

        // 示例数据
        JSONObject json1 = new JSONObject();
        json1.put("campusId", 1);
        json1.put("campusName", "成都职业技术学院");
        json.put(json1);

        return json.toString();
    }

    /**
     * 通过学校ID获取该学校的所有院系名称<br>
     * 访问地址URL: http://ip:8080/coc/getFaculties.do<br>
     * @param strJson - json数据<br>
     *     参数：campusId : int - 学校ID<br>
     * @return 返回的json数组示例 [{"facultyName":"软件分院计算机系","facultyId":1}]
     *      facultyId : int - 院系ID<br>
     *      facultyName : String - 院系名称<br>
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getFaculties"}, method = {RequestMethod.POST})
    public String getFaculties(@RequestBody String strJson) throws JSONException {

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        int campusId = jsonParam.getInt("campusId");

        // 相关查找操作

        // 返回数据的JSON数组
        JSONArray json = new JSONArray();

        // 示例数据
        JSONObject json1 = new JSONObject();
        json1.put("facultyId", 1);
        json1.put("facultyName", "软件分院计算机系");
        json.put(json1);

        return json.toString();
    }


}
