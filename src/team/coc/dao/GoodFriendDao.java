package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import team.coc.pojo.GoodFriend;
import team.coc.pojo.GoodFriendRequest;
import team.coc.pojo.User;
import team.coc.util.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友申请表操作类
 */
public class GoodFriendDao extends CommonDao<GoodFriend> {

    /**
     * 通过ID获取GoodFriend对象
     * @param id1
     * @param id2
     * @return
     */
    public GoodFriend getGoodFriendById(int id1, int id2) {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<GoodFriend> goodFriends;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from GoodFriend where user1.id = ? and user2.id = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, id1);
            query.setParameter(1, id2);
            goodFriends = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (goodFriends != null && goodFriends.size() > 0) {
            return goodFriends.get(0);
        }
        return null;
    }

    /**
     * 通过账号检查两人是否是好友
     * @param A_user - 账号(用户名/邮箱)
     * @return 存在返回true， 不存在返回false
     */
    public boolean isFriend(String A_user,String B_user) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<GoodFriend> goodFriends;
        try {
            tx = session.beginTransaction();

            UserDao userDao = new UserDao();
            User userA = userDao.getUserByAccount(A_user);
            User userB = userDao.getUserByAccount(B_user);

            // 查询数据库
            String hql = " from GoodFriend where (user1.id= ? and user2.id = ?) " +
                    "or (user2.id = ? and user1.id= ? )";
            Query query = session.createQuery(hql);
            query.setParameter(0, userA.getUserId());
            query.setParameter(1, userB.getUserId());
            query.setParameter(2, userA.getUserId());
            query.setParameter(3, userB.getUserId());
            goodFriends = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (goodFriends != null && goodFriends.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据账号查询好友备注
     * @param account - 账号
     * @return List<GoodFriend>
     */
    public List<GoodFriend> getFriendRelative(String account) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<GoodFriend> userList=new ArrayList<GoodFriend>();
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from GoodFriend where user1.email = ? or user2.email = ? ";
            Query query = session.createQuery(hql);
            query.setParameter(0, account);
            query.setParameter(1, account);
            userList = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        return userList;

    }

    /**
     * 修改备注
     * @param user1 - 账号
     * @return List<GoodFriend>
     */
    public boolean updateLabel(String user1,String label_1,String user2) {
        UserDao userDao=new UserDao();
        User u1=userDao.getUserByAccount(user1);
        User u2=userDao.getUserByAccount(user2);
        GoodFriend goodFriend=new GoodFriend();
        goodFriend.setUser1(u1);
        goodFriend.setU1Notice(label_1);
        goodFriend.setUser2(u2);
        goodFriend.setU2Notice("飒飒");
        if (update(goodFriend)){
            return true;
        }
        return false;
    }
}
