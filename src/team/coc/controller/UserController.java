package team.coc.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.coc.dao.SocietyDao;
import team.coc.dao.UserDao;
import team.coc.pojo.Society;
import team.coc.pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * 用户控制器
 */
@Controller
public class UserController {

    /**
     * 获取用户头像<br>
     * 请求地址URL: http://ip:8080/coc/getAvatar.do<br>
     * 请求方式: POST <br>
     * @param strJson - json数据 <br>
     * 请求参数：uId : String - 用户ID <br>
     * @return - 返回的json对象示例 <br>
     * {result:'success/error', url : ""} <br>
     * result : String - 请求结果状态(success/error) <br>
     * url : String - 用户头像路径 <br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getAvatar"}, method = {RequestMethod.POST})
    public String getAvatar(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入getAvatar #############");

        // 接收用户传来的参数
        JSONObject jsonParam = new JSONObject(strJson);
        int uId = jsonParam.getInt("uId"); // 用户ID

        // 返回数据用JSON对象
        JSONObject json = new JSONObject();
        json.put("result", "error");

        UserDao userDao = new UserDao();
        User user = userDao.getById(uId);
        if (user != null) {
            json.put("result", "success");
            json.put("url", "res/img/" + user.getUserName());
        }

        System.out.println("###### getAvatar return:"+ json.toString() + " ######");
        return json.toString();
    }

    /**
     * 修改用户头像<br>
     * 请求地址URL: http://ip:8080/coc/alterAvatar.do<br>
     * 请求方式: POST <br>
     * @param param - json数据 <br>
     * 请求参数：uId : String - 用户ID <br>
     * @param avatar File - 图片文件
     * @return - 返回的json对象示例 <br>
     * {result:'success/error', url : ""} <br>
     * result : String - 请求结果状态(success/error) <br>
     * url : String - 用户头像路径 <br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/alterAvatar"}, method = {RequestMethod.POST})
    public String alterAvatar(@RequestParam String param,
                              @RequestParam MultipartFile avatar,
                              HttpServletRequest request) throws JSONException, IOException {

        System.out.println("############# 进入alterAvatar #############");

        // 接收用户传来的参数
        JSONObject jsonParam = new JSONObject(param);
        int uId = jsonParam.getInt("uId"); // 用户ID

        // 返回数据用JSON对象
        JSONObject json = new JSONObject();
        json.put("result", "error");

        // 项目部署的根路径(绝对路径)
        String path = request.getSession().getServletContext().getRealPath("/");

        // 使用idea启动tomcat，请使用此项目绝对路径
        //String path = "C:/develop/Workspaces/IDEA/CircleOfCampusService/web/";

        // 如果存放用户头像的文件夹不存在则创建
        path += "res/img/";
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
            System.out.println("创建路径：" + path);
        }

        UserDao userDao = new UserDao();
        User user = userDao.getById(uId);
        if (user != null) {

            // 将InputStream转为byte[]
            InputStream is = avatar.getInputStream();
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            is.close();

            // 写入文件
            FileOutputStream fos = new FileOutputStream(path + user.getUserName());
            fos.write(bytes);
            fos.close();
            json.put("result", "success");
            json.put("url", "res/img/" + user.getUserName());
        }

        System.out.println("###### alterAvatar return:"+ json.toString() + " ######");
        return json.toString();
    }

    /**
     * 更新用户信息<br>
     * 请求地址URL: http://ip:8080/coc/updateUserInfo.do<br>
     * 请求方式: POST <br>
     * @param strJson - json数据 <br>
     * 请求参数：uId : String - 用户ID <br>
     * 请求参数：userName : String - 用户名 <br>
     * 请求参数：gender : String - 用户性别 <br>
     * @return - 返回的json对象示例 <br>
     * {result:'success/error/occupy'} <br>
     * result : String - 请求结果状态(success/error) <br>
     * result - occupy : String - 用户名已被占用 <br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/updateUserInfo"}, method = {RequestMethod.POST})
    public String updateUserInfo(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 updateUserInfo #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        int uId = jsonParam.getInt("uId");
        String userName = jsonParam.getString("userName");
        String gender = jsonParam.getString("gender");

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        UserDao userDao = new UserDao();
        User user = userDao.getById(uId);
        if (user != null) {
            if (user.getUserName().equals(userName) || !userDao.hasUser(userName)) {
                user.setUserName(userName);
                user.setGender(gender);
                boolean isSuccess = userDao.saveOrUpdate(user);
                if (isSuccess) {
                    json.put("result", "success");
                } else {
                    json.put("result", "error");
                }
            } else {
                json.put("result", "occupy");
            }
        } else {
            json.put("result", "error");
        }

        System.out.println("############# updateUserInfo return:"+ json.toString() + " #############");
        return json.toString();
    }

    /**
     * 通过用户账号获取某用户的相关信息<br>
     * 请求地址URL: http://ip:8080/coc/getUserInfo.do<br>
     * 请求方式: POST <br>
     * @param strJson - json数据 <br>
     * 请求参数：Account : String - 用户账号 <br>
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
    @RequestMapping(value = {"/coc/getUserInfoByAccount"}, method = {RequestMethod.POST})
    public String getUserAllInfo(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 getUserInfoByAccount #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        String account = jsonParam.getString("account");

        // 返回数据的JSON数组
        JSONObject json = new JSONObject();

        UserDao userDao = new UserDao();
        User user = userDao.getUserByAccount(account);
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

        System.out.println("############# getUserInfoByAccount return:"+ json.toString() + " #############");
        return json.toString();
    }


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

        System.out.println("############# 进入 getUserInfo #############");

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

        System.out.println("############# getUserInfo return:"+ json.toString() + " #############");
        return json.toString();
    }

}
