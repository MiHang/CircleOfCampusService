package team.coc.test;

import team.coc.dao.CommonDao;
import team.coc.dao.GoodFriendRequestDao;
import team.coc.dao.UserDao;
import team.coc.pojo.Campus;
import team.coc.pojo.GoodFriendRequest;

import java.util.Date;
import java.util.List;

public class Demo {
    public static void main(String[]args) {
        //校园表示例数据
        UserDao dao=new UserDao();
        CommonDao<GoodFriendRequest> campusCommonDao = new CommonDao<GoodFriendRequest>();
        GoodFriendRequest campus = new GoodFriendRequest();
        campus.setRequestTime(new Date());
        campus.setRequestReason("认识一下");
        campus.setUser1(dao.getUserByAccount("1"));
        campus.setUser2(dao.getUserByAccount("2"));
        campus.setResult(0);
        campusCommonDao.save(campus);

        GoodFriendRequestDao goodFriendRequest=new GoodFriendRequestDao();
        List<GoodFriendRequest> data=goodFriendRequest.getUserByAccount("1");
        if (data!=null){

        }
    }

}
