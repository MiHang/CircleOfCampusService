package team.coc.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import rasencrypt.encrypt.RSAEncrypt;
import team.coc.dao.CampusDao;
import team.coc.dao.FacultyDao;
import team.coc.dao.UserDao;
import team.coc.data.Cookie;
import team.coc.pojo.Faculty;
import team.coc.pojo.User;
import team.coc.util.MailUtil;

import java.util.HashMap;
import java.util.List;
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
     * 请求地址URL: http://ip:8080/coc/register.do<br>
     * @param strJson - json数据<br>
     * 请求参数: username : String - 用户名<br>
     * 请求参数: gender : String - 性别(male or female)<br>
     * 请求参数: email : String - 邮箱<br>
     * 请求参数: pwd : String - 密码<br>
     * 请求参数: verificationCode : String - 用户输入的验证码<br>
     * 请求参数: facultyId : int - 院系ID<br>
     * @return 返回的json数据示例 {result:'success/error/no_code/code_error/timeout'}
     * result : String - success 注册成功<br>
     * result : String - error 注册失败<br>
     * result : String - no_code 未获取注册验证码<br>
     * result : String - code_error 验证码错误, 请重新获取验证码<br>
     * result : String - timeout 验证码超时（10分钟）<br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/register"}, method = {RequestMethod.POST})
    public String register(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 register #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        String username = jsonParam.getString("username");
        String gender = jsonParam.getString("gender");
        String email = jsonParam.getString("email");
        String pwd = jsonParam.getString("pwd");
        String verificationCode = jsonParam.getString("verificationCode");
        int facultyId = jsonParam.getInt("facultyId");

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

                // 获取院系信息
                FacultyDao facultyDao = new FacultyDao();
                Faculty faculty = facultyDao.getById(facultyId);

                // 用户表操作对象
                UserDao userDao = new UserDao();

                // 生成用户对象
                User user = new User(username, email, rsaEncrypt.encrypt(pwd), gender, faculty);

                // 将用户对象保存到数据库中
                if (userDao.save(user)) {
                    json.put("result", "success");
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

        System.out.println("############# register return:"+ json.toString() + " #############");
        return json.toString();
    }


    /**
     * 通过学校ID获取该学校的所有院系<br>
     * 请求地址URL: http://ip:8080/coc/getFaculties.do<br>
     * 请求方式: POST <br>
     * @param strJson - json数据<br>
     * 请求参数：campusId : int - 学校ID<br>
     * @return 返回的json数组示例 [{"facultyName":"软件分院","facultyId":1}]
     * facultyId : int - 院系ID<br>
     * facultyName : String - 院系名称<br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getFaculties"}, method = {RequestMethod.POST})
    public String getFaculties(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 getFaculties #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        int campusId = jsonParam.getInt("campusId");

        // 返回数据的JSON数组
        JSONArray json = new JSONArray();

        // 实例化 Faculty 数据表操作对象
        FacultyDao facultyDao = new FacultyDao();

        // 获取相关的院系信息
        List<Object[]> objects = facultyDao.getAllFacultiesByCampusId(campusId);
        if (objects != null && objects.size() > 0) {
            for (Object[] objArr : objects) { // 遍历结果集
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("facultyId", objArr[0]);
                jsonObject.put("facultyName", objArr[1]);
                json.put(jsonObject);
            }
        }

        System.out.println("############# getFaculties return:"+ json.toString() + " #############");
        return json.toString();
    }


    /**
     * 获取支持注册的所有学校<br>
     * 请求地址URL: http://ip:8080/coc/getCampuses.do<br>
     * 请求方式: POST <br>
     * @return - 返回的json数组示例 [{"campusName":"成都职业技术学院","campusId":1}]
     * campusId : int - 学校ID<br>
     * campusName : String - 学校名称<br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getCampuses"}, method = {RequestMethod.POST})
    public String getCampuses() throws JSONException {

        System.out.println("############# 进入 getCampuses #############");

        // 返回数据的JSON数组
        JSONArray json = new JSONArray();

        // 实例化Campus数据表操作对象
        CampusDao campusDao = new CampusDao();

        // 获取相关的学校信息
        List<Object[]> objects = campusDao.getAllCampusNameAndId();
        if (objects != null && objects.size() > 0) {
            for (Object[] objArr : objects) { // 遍历结果集
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("campusId", objArr[0]);
                jsonObject.put("campusName", objArr[1]);
                json.put(jsonObject);
            }
        }

        System.out.println("############# getCampuses return:"+ json.toString() + " #############");
        return json.toString();
    }


    /**
     * 获取邮箱注册验证码 <br>
     * 请求地址URL: http://ip:8080/coc/getRegisterCode.do <br>
     * 请求方式: POST <br>
     * 使用此请求请设置连接超时和读取超时时间(建议：20s，30s) <br>
     * @param strJson - json数据 <br>
     * 请求参数：email : String - 用户注册邮箱 <br>
     * 请求示例：{"email":"jayevip@163.com"} <br>
     * @return 返回的json数据示例 {result:'success/invalid/error/registered'}
     * result : String - success 发送成功 <br>
     * result : String - invalid 收件人邮箱地址无效 <br>
     * result : String - error 发送失败 <br>
     * result : String - registered 该邮箱已被注册 <br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getRegisterCode"}, method = {RequestMethod.POST})
    public String getRegisterCode(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 getRegisterCode #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        String email = jsonParam.getString("email");

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        // 实例化User数据表操作对象
        UserDao userDao = new UserDao();

        // 此邮箱已被注册
        if (userDao.hasUser(email)) {
            json.put("result", "registered");
            return json.toString();
        }

        // 随机生成6位验证码
        String verificationCode = "";
        for (int i = 0; i < 6; i++) {
            verificationCode += (int)(Math.random() * 9);
        }

        // 邮件正文（可以使用html标签）
        String content = "<p>尊敬的用户 <a href='#'>" + email +"</a> 您好!</p>" +
                "<p>感谢您注册校园圈，请将验证码填写到注册页面。<p>" +
                "<p>验证码: <a href='#'>" + verificationCode +"</a>，此验证码在5分钟内有效。<p>" +
                "<p>如果您没有请求注册校园圈，请忽略这封邮件。 </p>";

        // 发送验证码并返回发送结果
        String str = MailUtil.sendEmail(email,"注册校园圈邮箱验证", content);

        // 返回数据的JSON对象
        json = new JSONObject(str);
        if ("success".equals(json.getString("result"))) { // 邮件发送成功

            // 将邮箱和验证码保存到cookieMap中
            Cookie cookie = new Cookie(email, verificationCode, System.currentTimeMillis());
            cookieMap.put(email, cookie);
        }

        System.out.println("############# getRegisterCode return:"+ json.toString() + " #############");
        return json.toString();
    }


    /**
     * 判断用户名是否可用/唯一<br>
     * 请求地址URL: http://ip:8080/coc/isUsableName.do<br>
     * 请求方式: POST<br>
     * @param strJson - json数据<br>
     * 请求参数: username : String - 用户名<br>
     * @return 返回的json数据示例{result:'free/occupy'}<br>
     * result : String - free 此用户名可用<br>
     * result : String - occupy 此用户名已被占用<br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/isUsableName"}, method = {RequestMethod.POST})
    public String isUsableName(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 isUsableName #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        String username = jsonParam.getString("username");

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        // 实例化Campus数据表操作对象
        CampusDao campusDao = new CampusDao();

        // 实例化User数据表操作对象
        UserDao userDao = new UserDao();

        if (campusDao.hasCampus(username) || userDao.hasUser(username)) { // 用户名已被占用
            json.put("result", "occupy");
        } else {
            json.put("result", "free");
        }

        System.out.println("############# isUsableName return:"+ json.toString() + " #############");
        return json.toString();
    }

}
