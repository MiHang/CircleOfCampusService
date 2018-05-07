package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import team.coc.pojo.User;
import team.coc.util.HibernateUtils;

import java.util.List;

/**
 * 用户表操作类
 */
public class UserDao extends CommonDao {

    /**
     * 验证用户身份
     * @param account - 账号（邮箱/用户名）
     * @param pwd - 密码
     * @return 验证成功返回User对象， 失败返回null
     */
    public User isValidity(String account, String pwd) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<User> userList = null;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from User where userName = ? or email = ? and pwd = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, account);
            query.setParameter(1, account);
            query.setParameter(2, pwd);
            userList = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 通过账号检查是否存在此用户
     * @param account - 账号(用户名/邮箱)
     * @return 存在返回true， 不存在返回false
     */
    public boolean hasUser(String account) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<User> userList = null;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from User where userName = ? or email = ?";
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

        // 存在结果
        if (userList != null && userList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
