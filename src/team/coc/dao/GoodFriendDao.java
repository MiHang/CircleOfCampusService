package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import team.coc.pojo.GoodFriend;
import team.coc.util.HibernateUtils;

import java.util.List;

/**
 * 院系表操作类
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

}
