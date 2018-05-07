package team.coc.test;

import team.coc.dao.CommonDao;
import team.coc.pojo.Campus;
import team.coc.pojo.GoodFriend;
import team.coc.pojo.GoodFriendRequest;

public class HibernateTest {

    public static void main(String[]args) {
        CommonDao<Campus> campusCommonDao = new CommonDao<Campus>();
        Campus campus = new Campus();
        campus.setCampusAccount("cdvtc");
        campus.setCampusName("成都职业技术学院");
        campus.setIntroduction("简介");
        campus.setPassword("123456");
        campusCommonDao.save(campus);

        CommonDao<GoodFriend> goodFriendCommonDao = new CommonDao<GoodFriend>();
        GoodFriend goodFriend = new GoodFriend();
        goodFriend.setU1Id(1);
        goodFriend.setU2Id(2);
        goodFriendCommonDao.save(goodFriend);

        CommonDao<GoodFriendRequest> goodFriendRequestCommonDao = new CommonDao<GoodFriendRequest>();
        GoodFriendRequest goodFriendRequest = new GoodFriendRequest();
        goodFriendRequest.setU1Id(1);
        goodFriendRequest.setU2Id(2);
        goodFriendRequest.setRequestTime("2018");
        goodFriendRequestCommonDao.save(goodFriendRequest);
    }

}
