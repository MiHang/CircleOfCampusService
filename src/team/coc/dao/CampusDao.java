package team.coc.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import team.coc.pojo.Campus;
import team.coc.util.HibernateUtils;

import java.util.List;

/**
 * 学校表操作类
 * hibernate 获取查询结构集合时，查询全部字段可将集合封装为javabean，查询两个及以上的字段为object[], 查询一个字段为object
 */
public class CampusDao extends CommonDao<Campus> {

    /**
     * 获取所有学校的ID和名称
     * @return List<Object[]> <br>
     * object[0] - id <br>
     * object[1] - name <br>
     */
    public List<Object[]> getAllCampusNameAndId() {
        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<Object[]> objects;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "select campusId, campusName from Campus";
            Query query = session.createQuery(hql);
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
            return objects;
        } else {
            return null;
        }
    }

    /**
     * 验证用户身份
     * @param account - 账号（用户名）
     * @param pwd - 密码
     * @return 验证成功返回campusId， 失败返回-1
     */
    public int isValidity(String account, String pwd) {

        Session session = HibernateUtils.openSession();
        Transaction tx = null;
        List<Object> objects;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "select campusId from Campus where campusAccount = ? and password = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, account);
            query.setParameter(1, pwd);
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
            return (int)objects.get(0);
        } else {
            return -1;
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
        List<Object> objectList;
        try {
            tx = session.beginTransaction();

            // 查询数据库
            String hql = "select campusId from Campus where campusAccount = ?";
            Query query = session.createQuery(hql);
            query.setParameter(0, account);
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
