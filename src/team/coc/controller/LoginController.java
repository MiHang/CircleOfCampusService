package team.coc.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import team.coc.dao.CampusDao;
import team.coc.dao.CommonDao;
import team.coc.dao.UserDao;
import team.coc.data.Cookie;
import team.coc.pojo.Campus;
import team.coc.pojo.User;
import team.coc.util.MailUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录控制器
 */
@Controller
public class LoginController {

    /**
     * 用户账号密码登录<br>
     * 请求地址URL: http://ip:8080/coc/login.do<br>
     * 请求方式: POST<br>
     * @param strJson - json数据<br>
     * 请求参数：account : String - 账号(邮箱/用户名)<br>
     * 请求参数：pwd : String - 密码<br>
     * 示例：{"account":"jayevip@163.com", "pwd":"123456"}<br>
     * @return 返回的json数据示例 {result:'success/error/unknown', id: 1, type:"admin/user"}
     * result : String - success 登录验证成功<br>
     * result : String - error 登录验证失败<br>
     * result : String - unknown 该用户未注册<br>
     * id : int 用户id<br>
     * type : String - admin 管理员用户<br>
     * type : String - user 普通用户
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/login"}, method = {RequestMethod.POST})
    public String login(@RequestBody String strJson) throws JSONException {

        // 用户请求时上传的参数
        JSONObject jsonParam = new JSONObject(strJson);
        String account = jsonParam.getString("account"); // 账号
        String pwd = jsonParam.getString("pwd"); // 密码

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        // 实例化Campus数据表操作对象
        CampusDao campusDao = new CampusDao();

        // 实例化User数据表操作对象
        UserDao userDao = new UserDao();

        if (campusDao.hasCampus(account)) { // 该账号为学校账号

            json.put("type", "admin"); // 用户类型 - 管理员

            int campusId = campusDao.isValidity(account, pwd);
            if (campusId != -1) { // 验证账号密码成功

                json.put("id", campusId); // 管理员/校园ID
                json.put("result", "success"); // 验证结果 - 成功

            } else {
                json.put("result", "error"); // 验证结果 - 失败
            }

        } else if (userDao.hasUser(account)) { // 该账号为用户账号

            json.put("type", "user"); // 用户类型 - 管理员

            int userId = userDao.isValidity(account, pwd);
            if (userId != -1) {
                json.put("id", userId); // 用户ID
                json.put("result", "success"); // 验证结果 - 成功
            } else {
                json.put("result", "error");
            }

        } else { // 该用户未注册
            json.put("result", "unknown");
        }

        return json.toString();
    }

}
