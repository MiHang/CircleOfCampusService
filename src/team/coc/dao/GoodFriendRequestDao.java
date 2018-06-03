package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import team.coc.pojo.GoodFriendRequest;
import team.coc.pojo.User;
import team.coc.util.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友申请表操作类
 */
public class GoodFriendRequestDao extends CommonDao<GoodFriendRequest> {


    /**
     * 通过账号检查两人是否提交过好友申请
     * @param A_user - 账号(用户名/邮箱)
     * @return 存在返回true， 不存在返回false
     */
    public boolean hasRequest(String A_user,String B_user) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<Object> objectList;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = " from GoodFriendRequest where  user1.email= ? and user2.email = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, A_user);
            query.setParameter(1, B_user);
            objectList = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (objectList != null && objectList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据账号查询好友申请信息
     * @param account - 账号
     * @return List<GoodFriendRequest>
     */
    public List<GoodFriendRequest> getRequestAddFriendInfo(String account) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<GoodFriendRequest> userList= new ArrayList<GoodFriendRequest>();
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from GoodFriendRequest where user2.email = ? and result = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, account);
            query.setParameter(1, 0);
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
}
