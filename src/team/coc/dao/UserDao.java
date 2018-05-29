package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import team.coc.pojo.GoodFriendRequest;
import team.coc.pojo.User;
import team.coc.util.HibernateUtils;

import java.util.List;

/**
 * 用户表操作类
 */
public class UserDao extends CommonDao<User> {
    /**
     * 好友查询 模糊查询
     * @param key - 账号或用户名
     * @return List<User>
     */
    public List<User> getUserInfoByUserNameOrAccount(String key) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<User> userList;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from User where userName like ? or email like ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, "%"+key+"%");
            query.setParameter(1, "%"+key+"%");
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
            return userList;
        } else {
            return null;
        }
    }
    /**
     * 通过账号获取用户信息
     * @param account - 账号
     * @return User
     */
    public User getUserByAccount(String account) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<User> userList;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from User where email = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, account);
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
     * 验证用户身份
     * @param account - 账号（邮箱/用户名）
     * @param pwd - 密码
     * @return 验证成功返回userId， 失败返回 -1
     */
    public int isValidity(String account, String pwd) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<Object> objects;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "select userId from User where userName = ? or email = ? and pwd = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, account);
            query.setParameter(1, account);
            query.setParameter(2, pwd);
            objects = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (objects != null && objects.size() > 0) {
            return Integer.parseInt((String)objects.get(0));
        } else {
            return -1;
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
        List<Object> objectList;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "select userId from User where userName = ? or email = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, account);
            query.setParameter(1, account);
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
}
