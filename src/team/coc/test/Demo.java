package team.coc.test;

import team.coc.dao.CommonDao;
import team.coc.dao.GoodFriendRequestDao;
import team.coc.dao.UserDao;
import team.coc.pojo.Campus;
import team.coc.pojo.GoodFriendRequest;
import team.coc.pojo.User;

import java.util.Date;
import java.util.List;

public class Demo {
    public static void main(String[]args) {
        //校园表示例数据
        UserDao dao=new UserDao();

        GoodFriendRequestDao goodFriendRequestDao = new GoodFriendRequestDao();
        GoodFriendRequest campus = new GoodFriendRequest();
        campus.setRequestTime(new Date());
        campus.setRequestReason("认识一下");
        campus.setUser1(dao.getUserByAccount("87654321@qq.com"));
        campus.setUser2(dao.getUserByAccount("jayevip@163.com"));
        campus.setResult(0);


        GoodFriendRequestDao goodFriendRequest=new GoodFriendRequestDao();
       if(! goodFriendRequest.hasRequest("87654321@qq.com","jayevip@163.com")){
           goodFriendRequestDao.save(campus);
        }else{
                   System.out.println("您已发起过申请信息");
               }
        List<GoodFriendRequest> data=goodFriendRequest.getRequest("jayevip@163.com");
        if (data!=null){
            System.out.println(data.get(0).getUser1().getUserName()+"请求添加"+data.get(0).getUser2().getUserName()+"为好友");
        }else{
            System.out.println("无好友申请信息");
        }


        //好友查询
        List<User> l=dao.getRequest("ja");
        System.out.println("好友查询"+l.get(0).getUserName());
    }

}
