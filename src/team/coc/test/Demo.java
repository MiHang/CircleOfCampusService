package team.coc.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import team.coc.dao.CommonDao;
import team.coc.dao.GoodFriendDao;
import team.coc.dao.GoodFriendRequestDao;
import team.coc.dao.UserDao;
import team.coc.pojo.Campus;
import team.coc.pojo.GoodFriend;
import team.coc.pojo.GoodFriendRequest;
import team.coc.pojo.User;

import java.util.Date;
import java.util.List;

public class Demo {
    public static void main(String[]args) throws JSONException {
//        //校园表示例数据
//        UserDao dao=new UserDao();
//        GoodFriendDao d=new GoodFriendDao();
//
//        //好友查询
//        String account="jaye@163.com";
//        List<GoodFriend> data=d.getFriendRelative(account);
//        if(data.size()==0){
//            System.out.println("无好友");
//        } else{
//            for(GoodFriend g:data){
//                if (account.equals(g.getUser1().getEmail())){
//                    System.out.println("你的好友"+g.getUser2().getUserName());
//                    System.out.println("绰号"+g.getU2Notice());
//                }else  if (account.equals(g.getUser2().getEmail())){
//                    System.out.println("你的好友"+g.getUser1().getUserName());
//                    System.out.println("绰号"+g.getU1Notice());
//
//                }
//
//            }
//        }


            //请求添加好友
        GoodFriendRequest goodFriendRequest=new GoodFriendRequest();
        UserDao userDao=new UserDao();
        User user1=userDao.getUserByAccount("123@163.com");

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

//        //查询好友关系
//        GoodFriendDao dao1=new GoodFriendDao();
//        if (dao1.isFriend("jaye@163.com","5085")){
//            System.out.println("已是好友");
//        }else{
//            System.out.println("不是好友");
//        }
//        //修改好友备注
//            dao1.updateLabel("jaye@163.com","demo@163.com","巴萨是");
    }

}
