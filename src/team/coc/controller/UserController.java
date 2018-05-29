package team.coc.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import team.coc.dao.SocietyDao;
import team.coc.dao.UserDao;
import team.coc.pojo.Society;
import team.coc.pojo.User;

import java.util.List;

/**
 * 用户控制器
 */
@Controller
public class UserController {

    /**
     * 获取某用户的相关信息<br>
     * 请求地址URL: http://ip:8080/coc/getUserInfo.do<br>
     * 请求方式: POST <br>
     * @param strJson - json数据 <br>
     * 请求参数：uId : int - 用户ID <br>
     * @return - 返回的json对象示例 <br>
     * {result:'success/error', userId: 1, userName: 'jaye', email:'jayevip@163.com', gender:'male',
     * campusId:1, campusName: '成都职业技术学院',facultyId:1, facultyName:'软件分院'} <br>
     * result : String - 请求结果状态(success/error) <br>
     * userId : int - 用户ID <br>
     * userName : String - 用户名 <br>
     * email : String - 用户邮箱 <br>
     * gender : String - 用户性别 <br>
     * campusId : int - 学校ID <br>
     * campusName : String - 学校名称 <br>
     * facultyId : int - 院系ID <br>
     * facultyName : String - 院系名称 <br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getUserInfo"}, method = {RequestMethod.POST})
    public String getUserInfo(@RequestBody String strJson) throws JSONException {

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        int uId = jsonParam.getInt("uId");

        // 返回数据的JSON数组
        JSONObject json = new JSONObject();

        UserDao userDao = new UserDao();
        User user = userDao.getById(uId);
        if (user != null) {
            json.put("result", "success");
            json.put("uId", user.getUserId());
            json.put("userName", user.getUserName());
            json.put("email", user.getEmail());
            json.put("gender", user.getGender());
            json.put("campusId", user.getFaculty().getCampus().getCampusId());
            json.put("campusName", user.getFaculty().getCampus().getCampusName());
            json.put("facultyId", user.getFaculty().getFacultyId());
            json.put("facultyName", user.getFaculty().getFacultyName());
        } else {
            json.put("result", "error");
        }

        return json.toString();
    }

}
