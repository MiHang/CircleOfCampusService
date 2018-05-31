package team.coc.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import team.coc.dao.CommonDao;
import team.coc.dao.GoodFriendRequestDao;
import team.coc.dao.UserDao;
import team.coc.pojo.Campus;
import team.coc.pojo.GoodFriendRequest;
import team.coc.pojo.User;

import java.util.Date;
import java.util.List;

public class Demo {
    public static void main(String[]args) throws JSONException {
        //校园表示例数据
        UserDao dao=new UserDao();

        //好友查询
        List<User> l=dao.getUserInfoByUserNameOrAccount("ja");
        JSONObject js=new JSONObject();
        JSONArray ja=new JSONArray();
        js.put("Result",0);
        js.put("Info",ja.toString());
        if (l!=null){
            if (l.size()>0){
                js.put("Result", 1);
            }

            for(int i=0;i<l.size();i++){
                JSONObject json = new JSONObject();

                json.put("Account",l.get(i).getEmail());
                json.put("UserName",l.get(i).getUserName());
                ja.put(json.toString());


            }
            js.put("Info",ja.toString());
        }

        System.out.println("好友查询"+js.getString("Info"));
        JSONObject j=new JSONObject(js.toString());



            JSONArray json = new JSONArray(j.getString("Info"));
            System.out.println("好友查询"+json.length());
            for(int i=0;i<json.length();i++){
                JSONObject jsonObject= new JSONObject(json.get(i).toString());
                System.out.println(jsonObject.toString());
            }
//
           User u= dao.getUserByAccount("jaye@163.com");
            System.out.println(u.getUserName()+u.getGender());


            //请求添加好友
        GoodFriendRequest goodFriendRequest=new GoodFriendRequest();
        UserDao userDao=new UserDao();
        User user1=userDao.getUserByAccount("jaye@163.com");

        goodFriendRequest.setUser1(user1);
        goodFriendRequest.setRequestReason("你好");
        goodFriendRequest.setRequestTime(new Date());
        User user2=userDao.getUserByAccount("demo@163.com");
        goodFriendRequest.setUser2(user2);
        GoodFriendRequestDao goodFriendRequestDao=new GoodFriendRequestDao();

        if (goodFriendRequestDao.hasRequest(user1.getEmail(),user2.getEmail())){
            System.out.println("你已向该用户发起申请");
        }else{
            goodFriendRequestDao.save(goodFriendRequest);
            System.out.println("发起成功");
        }


//         //查询是否有用户请求添加好友
//        List<GoodFriendRequest> goodFriendRequests= goodFriendRequestDao.getRequestAddFriendInfo("demo@163.com");
//        if (goodFriendRequests.size()==0){
//            System.out.println("无申请信息");
//
//        } else{
//            for(GoodFriendRequest g:goodFriendRequests ){
//                System.out.println(g.getUser1()+"向您发起请求信息");
//                System.out.println("申请理由"+g.getRequestReason()+"申请时间"+g.getRequestTime());
//                System.out.println("您的处理结果"+g.getResult());
//            }
//
//        }
    }

}
