package team.coc.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import team.coc.dao.CampusDao;
import team.coc.dao.GoodFriendRequestDao;
import team.coc.dao.UserDao;
import team.coc.pojo.GoodFriendRequest;
import team.coc.pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;


/**
 * 连接测试类<br/>
 * 用户测试与服务器的连接
 */
@Controller
public class DemoController {

    /**
     * 获取json数据测试方法
     * @ResponseBody 表明将返回的结果输出到Response的输出对象out中
     * @return String
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/test"})
    public String test1(HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("msg", "success");

        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = {"/coc/demo"}, method = {RequestMethod.POST})
    public String demo(@RequestBody String strJson) throws JSONException {
        System.out.println("strJson");
        return "{result:'666666'}";
    }

    /**
     * 好友搜索模糊查询
     * @param strJson 搜索关键字 用户名 或 账号 与 查询模式
     * @return result 返回结果 条数 Account  账号信息 UserName 用户名 Sex 性别 ;
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/unclearSearch"}, method = {RequestMethod.POST})
    public String unclearSearch(@RequestBody String strJson) throws JSONException {

        JSONObject jsonObject = new JSONObject(strJson); // 用户请求时上传的参数
        UserDao dao=new UserDao();
        //好友查询
        List<User> data=dao.getUserInfoByUserNameOrAccount(jsonObject.getString("Search"));
        JSONObject js=new JSONObject();
        JSONArray ja=new JSONArray();
        js.put("Result",0);
            if (data!=null&&data.size()>0){
                js.put("Result", data.size());
                for(User u:data){
                    JSONObject json = new JSONObject();
                    json.put("Account",u.getEmail());
                    json.put("UserName",u.getUserName());
                    json.put("Sex",u.getGender());
                    ja.put(json.toString());
                }
            }
            js.put("Info",ja.toString());

        return js.toString();
    }
    /**
     * 请求添加好友
     * @param strJson user1用户1 user2用户2 reason 申请理由
     * {result:'success/error',deal } deal 0 重复提交
     * @return result
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/requestAddFriend"}, method = {RequestMethod.POST})
    public String requestAddFriend(@RequestBody String strJson) throws JSONException {

        JSONObject jsonObject = new JSONObject(strJson); // 用户请求时上传的参数
        JSONObject js=new JSONObject();
        GoodFriendRequest goodFriendRequest=new GoodFriendRequest();
        UserDao userDao=new UserDao();

        js.put("result", "error");
        if (jsonObject.has("reason")&&jsonObject.getString("reason")!=null){
            User user1=userDao.getUserByAccount(jsonObject.getString("user1"));
            goodFriendRequest.setUser1(user1);

            goodFriendRequest.setRequestReason(jsonObject.getString("reason"));
            goodFriendRequest.setRequestTime(new Date());

            User user2=userDao.getUserByAccount(jsonObject.getString("user2"));
            goodFriendRequest.setUser2(user2);
            GoodFriendRequestDao goodFriendRequestDao=new GoodFriendRequestDao();

            if (!goodFriendRequestDao.hasRequest(user1.getEmail(),user2.getEmail())){
                goodFriendRequestDao.save(goodFriendRequest);//储存申请信息
                js.put("result", "success");
                js.put("deal",1);
            }else{
                js.put("deal",0);
            }
        }

        return js.toString();
    }

    /**
     * 查询是否有用户请求添加好友
     * @param strJson account
     * {result: result
     * @return result 申请结果条数 request 申请者 reason 申请理由 time 申请时间
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/QueryRequestAddFriendInfo"}, method = {RequestMethod.POST})
    public String QueryRequestAddFriendInfo(@RequestBody String strJson) throws JSONException {

        JSONObject jsonObject = new JSONObject(strJson); // 用户请求时上传的参数
        GoodFriendRequestDao dao=new GoodFriendRequestDao();
        JSONObject js=new JSONObject();
        JSONArray ja=new JSONArray();
        List<GoodFriendRequest> data= dao.getRequestAddFriendInfo(jsonObject.getString("account"));
        if (data.size()==0){
            System.out.println("无申请信息");
            js.put("result",0);
        } else{
            js.put("result",data.size());
            for(GoodFriendRequest g:data ){
                JSONObject json = new JSONObject();
                json.put("request",g.getUser1());
                json.put("reason",g.getRequestReason());
                json.put("time",g.getRequestTime());
                ja.put(json.toString());
//                System.out.println(g.getUser1()+"向您发起请求信息");
//                System.out.println("申请理由"+g.getRequestReason()+"申请时间"+g.getRequestTime());
//                System.out.println("您的处理结果"+g.getResult());
            }
            js.put("Info",ja.toString());
        }


        return js.toString();
    }


    /**
     * 根据账号获取用户表中的用户名与性别
     * @param strJson 账号
     * @return UserName 用户名 Sex 性别
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/UserNameAndSexQuery"}, method = {RequestMethod.POST})
    public String getUserNameAndSex(@RequestBody String strJson) throws JSONException {
        JSONObject jsonObject = new JSONObject(strJson); // 用户请求时上传的参数
        UserDao dao=new UserDao();
        User u= dao.getUserByAccount(jsonObject.getString("Account"));

        JSONObject js=new JSONObject();
        js.put("UserName",u.getUserName());
        js.put("Sex",u.getGender());
        System.out.println("用户名"+js.getString("UserName")+"性别"+js.getString("Sex"));
        return js.toString();
    }

    /**
     * 获取字节数组测试方法
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/testByte"})
    public byte[] test2() throws JSONException, UnsupportedEncodingException {

        /*
        File file = new File("D:/Picture/Saved Pictures/000.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);

        int size = fileInputStream.available(); // 得到文件大小
        byte data[] = new byte[size];
        fileInputStream.read(data); // 读数据
        fileInputStream.close();
        return data;
        */

        JSONObject json = new JSONObject();
        json.put("msg", "success");
        return json.toString().getBytes("UTF-8");
    }

}
