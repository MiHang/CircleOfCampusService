package team.coc.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import team.coc.dao.GoodFriendDao;
import team.coc.dao.UserDao;
import team.coc.pojo.GoodFriend;

/**
 * 好友控制器
 */
@Controller
public class FriendController {

    /**
     * 获取好友备注<br>
     * 请求地址URL: http://ip:8080/coc/getFriendNote.do<br>
     * 请求方式: POST <br>
     * @param strJson - json数据 <br>
     * 请求参数：userId : int - 用户ID <br>
     * 请求参数：friendId : int - 好友ID <br>
     * @return - 返回的json对象示例 <br>
     * {result:'success/error', note: "好友备注"} <br>
     * result : String - 请求结果状态(success/error) <br>
     * note : String - 好友备注 <br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getFriendNote"}, method = {RequestMethod.POST})
    public String getFriendNote(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 getFriendNote #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        int userId = jsonParam.getInt("userId");
        int friendId = jsonParam.getInt("friendId");

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        // 获取好友备注信息
        GoodFriendDao goodFriendDao = new GoodFriendDao();
        GoodFriend goodFriend = goodFriendDao.getGoodFriendById(userId, friendId);
        if (goodFriend != null) {
            json.put("result", "success");
            json.put("note", goodFriend.getU2Notice());
        } else if ((goodFriend = goodFriendDao.getGoodFriendById(friendId, userId)) != null) {
            json.put("result", "success");
            json.put("note", goodFriend.getU1Notice());
        } else {
            json.put("result", "error");
        }
        System.out.println("############# getFriendNote return:"+ json.toString() + " #############");
        return json.toString();
    }

    /**
     * 更新好友备注<br>
     * 请求地址URL: http://ip:8080/coc/updateFriendNote.do<br>
     * 请求方式: POST <br>
     * @param strJson - json数据 <br>
     * 请求参数：userId : int - 用户ID <br>
     * 请求参数：friendId : int - 好友ID <br>
     * 请求参数：note : String - 好友备注 <br>
     * @return - 返回的json对象示例 <br>
     * {result:'success/error'} <br>
     * result : String - 请求结果状态(success/error) <br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/updateFriendNote"}, method = {RequestMethod.POST})
    public String updateFriendNote(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 updateFriendNote #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        UserDao userDao=new UserDao();

        int userId =  userDao.getUserByAccount(jsonParam.getString("user1")).getUserId();
        int friendId = userDao.getUserByAccount(jsonParam.getString("user2")).getUserId();
        String note = jsonParam.getString("note");

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        boolean isSuccess = false;
        GoodFriendDao goodFriendDao = new GoodFriendDao();
        GoodFriend goodFriend = goodFriendDao.getGoodFriendById(userId, friendId);
        if (goodFriend != null) {
            goodFriend.setU2Notice(note);
            isSuccess = goodFriendDao.update(goodFriend);
        } else if ((goodFriend = goodFriendDao.getGoodFriendById(friendId, userId)) != null) {
            goodFriend.setU1Notice(note);
            isSuccess = goodFriendDao.update(goodFriend);
        }

        if (isSuccess) {
            json.put("result", "success");
        } else {
            json.put("result", "error");
        }
        System.out.println("############# updateFriendNote return:"+ json.toString() + " #############");
        return json.toString();
    }

}
