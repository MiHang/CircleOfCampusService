package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import team.coc.pojo.Campus;
import team.coc.util.HibernateUtils;

import java.util.List;

/**
 * 学校表操作类
 */
public class CampusDao extends CommonDao {

    /**
     * 验证用户身份
     * @param account - 账号（用户名）
     * @param pwd - 密码
     * @return 验证成功返回campus对象， 失败返回null
     */
    public Campus isValidity(String account, String pwd) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<Campus> campusList = null;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from Campus where campusAccount = ? and password = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, account);
            query.setParameter(1, pwd);
            campusList = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (campusList != null && campusList.size() > 0) {
            return campusList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 通过账号检查是否存在此学校
     * @param account - 账号/用户名
     * @return 存在返回true， 不存在返回false
     */
    public boolean hasCampus(String account) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<Campus> campusList = null;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "from Campus where campusAccount = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, account);
            campusList = query.list();

            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }

        // 存在结果
        if (campusList != null && campusList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
