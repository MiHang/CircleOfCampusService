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
import team.coc.dao.GoodFriendDao;
import team.coc.dao.GoodFriendRequestDao;
import team.coc.dao.UserDao;
import team.coc.pojo.GoodFriend;
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
     * 一键添加好友,无需申请
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/addFriendByQr"})
    public String addFriend(@RequestBody String strJson) throws JSONException, UnsupportedEncodingException {
        JSONObject js = new JSONObject(strJson);
        JSONObject json = new JSONObject();
        UserDao userDao=new UserDao();
        GoodFriendDao dao=new GoodFriendDao();
        if (dao.isFriend(js.getString("user1"),js.getString("user2"))){
            System.out.println("已是好友");
            json.put("result","error");
        }else{
            GoodFriend goodFriend=new GoodFriend();
            goodFriend.setUser1(userDao.getUserByAccount(js.getString("user1")));
            goodFriend.setUser2(userDao.getUserByAccount(js.getString("user2")));
            dao.save(goodFriend);
            System.out.println("添加成功");
            json.put("result","success");
        }
        return json.toString();
    }
    /**
     * 获取json数据测试方法
     * @ResponseBody 表明将返回的结果输出到Response的输出对象out中
     * @return String
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/test"})
    public String test1(HttpServletRequest request) throws JSONException {
        System.out.println("############# 进入 test #############");
        JSONObject json = new JSONObject();
        json.put("msg", "success");
        System.out.println("############# test return:"+ json.toString() + " #############");
        return json.toString();
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

        System.out.println("############# 进入 unclearSearch #############");

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

        System.out.println("############# unclearSearch return:"+ js.toString() + " #############");
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

        System.out.println("############# 进入 requestAddFriend #############");

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

        System.out.println("############# requestAddFriend return:"+ js.toString() + " #############");
        return js.toString();
    }


    /**
     * 查询两人是否是好友
     * @param strJson user1,user2
     * {result: result
     * @return result yes 是好友 no 不是好友
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/queryIsFriend"}, method = {RequestMethod.POST})
    public String QueryIsFriend(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 queryIsFriend #############");
        JSONObject jsonObject = new JSONObject(strJson); // 用户请求时上传的参数
        JSONObject js=new JSONObject();
        GoodFriendDao dao1=new GoodFriendDao();
        if (dao1.isFriend(jsonObject.getString("user1"),jsonObject.getString("user2"))){
            js.put("result","yes");
        }else{
            js.put("result","no");
        }

        System.out.println("############# queryIsFriend return:"+ js.toString() + " #############");
        return js.toString();
    }


    /**
     * 查询好友信息
     * @param strJson account 账号
     * result: result 好友数量, account 账号 ,nickName 绰号/用户名 sex 性别
     * @return
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/queryFriendInfo"}, method = {RequestMethod.POST})
    public String queryFriendInfo(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 queryFriendInfo #############");
        JSONObject jsonObject = new JSONObject(strJson); // 用户请求时上传的参数
        JSONObject js=new JSONObject();
        GoodFriendDao dao=new GoodFriendDao();
        JSONArray ja=new JSONArray();
        //好友查询
        String account=jsonObject.getString("account");
        List<GoodFriend> data=dao.getFriendRelative(account);
        if(data.size()==0){
            js.put("result",0);//无好友
        } else{
            js.put("result",data.size());//无好友
            for(GoodFriend g : data){
                JSONObject json = new JSONObject();
                if (account.equals(g.getUser1().getEmail()) || account.equals(g.getUser1().getUserName())){

                    if (g.getU2Notice()==null||g.getU2Notice().equals("")){//备注名称为空
                        json.put("nickName",g.getUser2().getUserName());
                    }else{
                        json.put("nickName",g.getU2Notice());
                    }
                    json.put("sex",g.getUser2().getGender());
                    json.put("account",g.getUser2().getEmail());

                }else  if (account.equals(g.getUser2().getEmail()) || account.equals(g.getUser2().getUserName())){

                    if (g.getU1Notice()==null||g.getU1Notice().equals("")){//备注名称为空
                        json.put("nickName",g.getUser1().getUserName());
                    }else{
                        json.put("nickName",g.getU1Notice());
                    }
                    json.put("sex",g.getUser1().getGender());
                    json.put("account",g.getUser1().getEmail());

                }
                ja.put(json.toString());

            }
        }
        js.put("Info",ja.toString());
        System.out.println("############# queryFriendInfo return:"+ js.toString() + " #############");
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

        System.out.println("############# 进入 QueryRequestAddFriendInfo #############");
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

        System.out.println("############# QueryRequestAddFriendInfo return:"+ js.toString() + " #############");
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

        System.out.println("############# 进入 UserNameAndSexQuery #############");

        JSONObject jsonObject = new JSONObject(strJson); // 用户请求时上传的参数
        UserDao dao=new UserDao();
        User u= dao.getUserByAccount(jsonObject.getString("Account"));

        JSONObject js=new JSONObject();
        js.put("UserName",u.getUserName());
        js.put("Sex",u.getGender());

        System.out.println("############# UserNameAndSexQuery return:"+ js.toString() + " #############");
        return js.toString();
    }

    /**
     * 获取字节数组测试方法
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/testByte"})
    public byte[] test2() throws JSONException, UnsupportedEncodingException {

        System.out.println("############# 进入 testByte #############");
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
        System.out.println("############# testByte return:"+ json.toString() + " #############");
        return json.toString().getBytes("UTF-8");
    }

}
