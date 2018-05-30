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

//        GoodFriendRequestDao goodFriendRequestDao = new GoodFriendRequestDao();
//        GoodFriendRequest campus = new GoodFriendRequest();
//        campus.setRequestTime(new Date());
//        campus.setRequestReason("认识一下");
//        campus.setUser1(dao.getUserByAccount("87654321@qq.com"));
//        campus.setUser2(dao.getUserByAccount("jayevip@163.com"));
//        campus.setResult(0);
//
//
//        GoodFriendRequestDao goodFriendRequest=new GoodFriendRequestDao();
//       if(! goodFriendRequest.hasRequest("87654321@qq.com","jayevip@163.com")){
//           goodFriendRequestDao.save(campus);
//        }else{
//                   System.out.println("您已发起过申请信息");
//               }
//        List<GoodFriendRequest> data=goodFriendRequest.getRequest("jayevip@163.com");
//        if (data!=null){
//            System.out.println(data.get(0).getUser1().getUserName()+"请求添加"+data.get(0).getUser2().getUserName()+"为好友");
//        }else{
//            System.out.println("无好友申请信息");
//        }


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
        goodFriendRequest.setUser1(user2);
        GoodFriendRequestDao goodFriendRequestDao=new GoodFriendRequestDao();

        if (goodFriendRequestDao.hasRequest(user1.getEmail(),user2.getEmail())){
            System.out.println("你们已是好友");
        }else{
            goodFriendRequestDao.save(goodFriendRequest);
            System.out.println("发起成功");
        }

    }

}
